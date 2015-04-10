package ca.vorona.berta;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author avorona
 *
 */
public class MemoryMapHandler extends TraceHandler {

    // TODO: Consider custom data structure
    private ConcurrentSkipListSet<String> touched = new ConcurrentSkipListSet<>();

    @Override
    public void trace(String methodDescription) {
        touched.add(methodDescription);
    }

}
