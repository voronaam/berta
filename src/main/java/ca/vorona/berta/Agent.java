package ca.vorona.berta;

import java.lang.instrument.Instrumentation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.vorona.berta.api.AgentApiServer;

/**
 * Main class that initializes the instrumentation of JVM classes and the API Server
 */
public class Agent {
    
    public static String arguments = null;
    
    public static void premain(String agentArgs, Instrumentation inst) {
        arguments = agentArgs;
        int port = extractPort(agentArgs);
        // Initialize the reporting system
        switch(extractParameter("handle", agentArgs, null)) {
        case "print":
            StaticLinker.registerHandler(new PrintHandler());
            break;
        case "seen":
        default:
            StaticLinker.registerHandler(new MemoryMapHandler());
            break;
        }
        try {
            // Start the API server
            final AgentApiServer apiServer = new AgentApiServer(port);
            apiServer.start();

            // Register shutdown hook to gracefully terminate Netty server
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    apiServer.stop();
                }
            });

            // register the transformer
            inst.addTransformer(new TracingClassFileTransformer(extractParameter("transform", agentArgs, null)));
        } catch(Exception e) {
            // TODO Better exception handling
            e.printStackTrace();
        }
    }

    private static int extractPort(String agentArgs) {
        Pattern p = Pattern.compile(".*port=(\\d+).*");
        Matcher m = p.matcher(agentArgs);
        if (m.matches()) {
            return Integer.parseInt(m.group(1));
        } else {
            return 8800;
        }
    }

    private static String extractParameter(String name, String agentArgs, String defaultValue) {
        Pattern p = Pattern.compile(".*" + name + "=([^:]+).*");
        Matcher m = p.matcher(agentArgs);
        if (m.matches()) {
            return m.group(1);
        } else {
            return defaultValue;
        }
    }

}
