package ca.vorona.berta;

/**
 * Interface for the tracing.
 * It is abstract class to win a few instructions.
 * @see http://shipilev.net/blog/2015/black-magic-method-dispatch/
 *
 */
public abstract class TraceHandler {
    
    public abstract void trace(String methodDescription);

}
