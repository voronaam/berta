package ca.vorona.berta;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tracing Handler that keeps all touched methods inside JVM Heap
 * @author avorona
 *
 */
public class MemoryMapHandler extends TraceHandler {
    
    private static final String END_MARKER = "--END--\n";

    private AtomicReference<TestRecord> currentTest = new AtomicReference<>(new TestRecord("Java Startup"));

    @Override
    public void trace(String methodDescription) {
        TestRecord test = currentTest.get();
        if(test != null) {
            test.touched.add(methodDescription);
        }
    }

    @Override
    public String setTest(String name) {
        TestRecord test = currentTest.getAndSet(new TestRecord(name));
        return test == null ? END_MARKER : test.toString();
    }
    
    private static class TestRecord {
        final String testName;
        final ConcurrentSkipListSet<String> touched;
        
        public TestRecord(String name) {
            testName = name;
            touched = new ConcurrentSkipListSet<>();
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(touched.size()*128 + 64); // Guestimate of the average method name length plus header
            sb.append(testName).append('\n');
            for(String method: touched) {
                sb.append(method).append('\n');
            }
            sb.append(END_MARKER);
            return sb.toString();
        }
    }

}
