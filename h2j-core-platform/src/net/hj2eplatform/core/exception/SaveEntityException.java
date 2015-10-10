/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.exception;

/**
 *
 * @author GiangPT
 */
public class SaveEntityException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>SaveEntityException</code> without detail message.
     */
    public SaveEntityException() {
    }

    /**
     * Constructs an instance of
     * <code>SaveEntityException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SaveEntityException(String msg) {
        super(msg);
    }
}
