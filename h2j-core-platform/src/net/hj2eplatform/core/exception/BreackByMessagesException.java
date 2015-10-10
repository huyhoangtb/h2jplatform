/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.exception;

/**
 *
 * @author GiangPT
 */
public class BreackByMessagesException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>PassInvaliException</code> without detail message.
     */
    public BreackByMessagesException() {
    }

    /**
     * Constructs an instance of
     * <code>PassInvaliException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public BreackByMessagesException(String msg) {
        super(msg);
    }
}
