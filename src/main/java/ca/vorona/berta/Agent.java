package ca.vorona.berta;

import java.lang.instrument.Instrumentation;

import ca.vorona.berta.api.AgentApiServer;

/**
 * Main class that initializes the instrumentation of JVM classes and the API Server
 */
public class Agent {
    
    public static void premain(String agentArgs, Instrumentation inst) {
        // TODO parse agentArgs
        // Initialize the reporting system
        StaticLinker.registerHandler(new MemoryMapHandler());
        try {
            // Start the API server
            final AgentApiServer apiServer = new AgentApiServer(8800);
            apiServer.start();

            // Register shutdown hook to gracefully terminate Netty server
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    apiServer.stop();
                }
            });

            // register the transformer
            inst.addTransformer(new TracingClassFileTransformer(agentArgs));
        } catch(Exception e) {
            // TODO Better exception handling
            e.printStackTrace();
        }
    }

}
