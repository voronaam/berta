package ca.vorona.berta;

/**
 * Test handler that prints everything to stdout.
 * @author avorona
 *
 */
public class PrintHandler extends TraceHandler {
    
    protected volatile String testName = "UNKNOWN TEST";
    
    @Override
    public void trace(String methodDescription) {
        System.out.println(testName + " touched " + methodDescription);
    }

    @Override
    public String setTest(String name) {
        testName = name;
        return "--END--\n";
    }

}
