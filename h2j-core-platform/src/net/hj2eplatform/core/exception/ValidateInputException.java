/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.exception;

/**
 *
 * @author Huy Hoang
 */
public class ValidateInputException extends RuntimeException {

    public ValidateInputException() {
    }

    public ValidateInputException(String message) {
        super(message);
    }

    public ValidateInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateInputException(Throwable cause) {
        super(cause);
    }

    public ValidateInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
