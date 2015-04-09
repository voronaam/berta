package ca.vorona.berta;

import java.lang.instrument.Instrumentation;

/**
 * Main class that initializes the instrumentation of JVM classes
 *
 */
public class Agent {
    
    public static void premain(String agentArgs, Instrumentation inst) {
        // Initialize the reporting system
        StaticLinker.registerHandler(new PrintHandler());
        // register the transformer
        inst.addTransformer(new TracingClassFileTransformer(agentArgs));
    }

}
