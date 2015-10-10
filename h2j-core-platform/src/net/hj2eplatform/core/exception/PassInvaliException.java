/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.exception;

/**
 *
 * @author GiangPT
 */
public class PassInvaliException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>PassInvaliException</code> without detail message.
     */
    public PassInvaliException() {
    }

    /**
     * Constructs an instance of
     * <code>PassInvaliException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public PassInvaliException(String msg) {
        super(msg);
    }
}
