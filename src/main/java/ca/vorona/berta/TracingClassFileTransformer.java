package ca.vorona.berta;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

/**
 * The main class transformer that instruments the classes and methods to trace their execution 
 * @author avorona
 *
 */
public class TracingClassFileTransformer implements ClassFileTransformer {

    private final Pattern pattern;

    // Only keep hash code to still allow user's JVM to unload class loaders
    private HashSet<Integer> seenClassLoaders; // TODO: use unboxed ints

    public TracingClassFileTransformer(String pattern) {
        this.pattern = pattern == null ? null : Pattern.compile(pattern);
        this.seenClassLoaders = new HashSet<>();
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;
        String normalizedClassName = className.replaceAll("/", ".");
        if(shouldInstrument(normalizedClassName)) {
            try {
                ByteArrayInputStream is = new ByteArrayInputStream(classfileBuffer);
                CtClass ctClass = null;
                try {
                    ensureClassLoaderInClassPath(loader);
                    ctClass = ClassPool.getDefault().makeClass(is);
                } finally {
                    is.close();
                }
                for(CtMethod method: ctClass.getDeclaredMethods()) {
                    method.insertBefore(String.format("ca.vorona.berta.StaticLinker.trace(\"%s#%s\");", normalizedClassName, method.getName()));
                }
                byteCode = ctClass.toBytecode();
                ctClass.detach();
            } catch (Throwable ex) {
                // TODO: Java agent should handle errors differently
                // ex.printStackTrace();
            }
        }
        return byteCode;
    }

    private void ensureClassLoaderInClassPath(ClassLoader loader) {
        if(!seenClassLoaders.contains(loader.hashCode())) {
            synchronized (this) {
                if(!seenClassLoaders.contains(loader.hashCode())) {
                    ClassPool.getDefault().appendClassPath(new LoaderClassPath(loader));
                    seenClassLoaders.add(loader.hashCode());
                }
            }
        }
    }

    private boolean shouldInstrument(String className) {
        return pattern == null ? true : pattern.matcher(className).matches();
    }

}
