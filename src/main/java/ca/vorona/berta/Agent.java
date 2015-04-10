package ca.vorona.berta;

import java.lang.instrument.Instrumentation;

import ca.vorona.berta.api.AgentApiServer;

/**
 * Main class that initializes the instrumentation of JVM classes
 *
 */
public class Agent {
    
    public static void premain(String agentArgs, Instrumentation inst) {
        // Initialize the reporting system
        StaticLinker.registerHandler(new MemoryMapHandler());
        try {
            // Start the API server
            new AgentApiServer(8800).run();
            // TODO Register shutdown hook to gracefully terminate Netty server 
            // register the transformer
            inst.addTransformer(new TracingClassFileTransformer(agentArgs));
        } catch(Exception e) {
            // TODO Exception handling
            e.printStackTrace();
        }
    }

}
