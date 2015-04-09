package ca.vorona.berta;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * The main class transformer that instruments the classes and methods to trace their execution 
 * @author avorona
 *
 */
public class TracingClassFileTransformer implements ClassFileTransformer {
    
    private final Pattern pattern;
    
    public TracingClassFileTransformer(String pattern) {
        this.pattern = Pattern.compile(pattern);
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
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(normalizedClassName);
                for(CtMethod method: cc.getDeclaredMethods()) {
                    method.insertBefore(String.format("ca.vorona.berta.StaticLinker.trace(\"%s#%s\");", normalizedClassName, method.getName()));
                }
                byteCode = cc.toBytecode();
                cc.detach();
            } catch (Throwable ex) {
                // TODO: Java agent should handle errors differently
                // ex.printStackTrace();
            }
        }
        return byteCode;
    }

    private boolean shouldInstrument(String className) {
        return pattern.matcher(className).matches();
    }

}
