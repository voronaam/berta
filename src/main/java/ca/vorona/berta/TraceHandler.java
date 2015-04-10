package ca.vorona.berta;

/**
 * Interface for the tracing.
 * It is abstract class to win a few instructions.
 * @see http://shipilev.net/blog/2015/black-magic-method-dispatch/
 *
 */
public abstract class TraceHandler {
    
    /**
     * Record a method invocation
     * @param methodDescription
     */
    public abstract void trace(String methodDescription);

    /**
     * Set the name of the currently running test.
     * @param name
     * @return Serialized description of the previous test
     */
    public abstract String setTest(String name);

}
