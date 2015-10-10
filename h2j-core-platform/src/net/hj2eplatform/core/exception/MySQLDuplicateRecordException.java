/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.exception;

/**
 *
 * @author HuyHoang
 */
public class MySQLDuplicateRecordException extends RuntimeException{

    public MySQLDuplicateRecordException() {
    }

    public MySQLDuplicateRecordException(String message) {
        super(message);
    }

    public MySQLDuplicateRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public MySQLDuplicateRecordException(Throwable cause) {
        super(cause);
    }

    public MySQLDuplicateRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
