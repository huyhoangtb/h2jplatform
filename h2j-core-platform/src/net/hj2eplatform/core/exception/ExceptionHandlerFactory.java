/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.exception;


/**
 *
 * @author hoang_000
 */
public class ExceptionHandlerFactory extends javax.faces.context.ExceptionHandlerFactory {

    private final javax.faces.context.ExceptionHandlerFactory parent;

    public ExceptionHandlerFactory(final javax.faces.context.ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new ExceptionHandler(this.parent.getExceptionHandler());
    }
}
