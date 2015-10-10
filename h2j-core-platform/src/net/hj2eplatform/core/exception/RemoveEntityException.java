/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.exception;

/**
 *
 * @author GiangPT
 */
public class RemoveEntityException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>RemoveEntityException</code> without detail message.
     */
    public RemoveEntityException() {
    }

    /**
     * Constructs an instance of
     * <code>RemoveEntityException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RemoveEntityException(String msg) {
        super(msg);
    }
}
