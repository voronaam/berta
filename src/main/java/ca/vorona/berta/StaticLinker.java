package ca.vorona.berta;

/**
 * This class holds static references for everything needed by instrumentation code. Since static calls are the fastest in JVM.
 * @author avorona
 *
 */
public class StaticLinker {
    
    // Has to be set at the very beginning, since it is not volatile
    private static TraceHandler handler;

    public static void registerHandler(TraceHandler handler) {
        StaticLinker.handler = handler;
    }
    
    public static void trace(String methodDescription) {
        handler.trace(methodDescription);
    }

}
