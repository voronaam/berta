package ca.vorona.berta;

/**
 * Test handler that prints everything to stdout.
 * @author avorona
 *
 */
public class PrintHandler extends TraceHandler {

    @Override
    public void trace(String methodDescription) {
        System.out.println("Touched " + methodDescription);
    }

}
