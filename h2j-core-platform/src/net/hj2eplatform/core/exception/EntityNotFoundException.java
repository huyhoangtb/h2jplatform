package net.hj2eplatform.core.exception;

/**
 *
 * @author GiangPT
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>EntityNotFoundException</code> without detail message.
     */
    public EntityNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>EntityNotFoundException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
