package ca.vorona.berta;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * The main class transformer that instruments the classes and methods to trace their execution 
 * @author avorona
 *
 */
public class TracingClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;
        // TODO: Instrument here
        // This code is just to test that I succeeded in java agent initialization
        System.out.println("Processing class " + className);
        String normalizedClassName = className.replaceAll("/", ".");
        if(shouldInstrument(normalizedClassName)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(normalizedClassName);
                for(CtMethod method: cc.getDeclaredMethods()) {
                    method.insertBefore(String.format("System.out.println(\"Touched %s\");", method.getName()));
                }
                byteCode = cc.toBytecode();
                cc.detach();
            } catch (Exception ex) {
                // TODO: Java agent should handle errors differently
                ex.printStackTrace();
            }
        }
        return byteCode;
    }

    private boolean shouldInstrument(String className) {
        // TODO: add ability to configure
        return !className.startsWith("java"); // Well, at least we can exclude standard library already
    }

}
