/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.servlet.jsp.el.ELException;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;


/**
 *
 * @author hoang_000
 */
public class ExceptionHandler extends ExceptionHandlerWrapper {


    private final javax.faces.context.ExceptionHandler wrapped;

    public ExceptionHandler(final javax.faces.context.ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public javax.faces.context.ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        for (final Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator(); it.hasNext();) {
            Throwable t = it.next().getContext().getException();
            while ((t instanceof FacesException ||  t instanceof ELException)
                    && t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof FileNotFoundException  || t instanceof ViewExpiredException) {
                final FacesContext facesContext = FacesContext.getCurrentInstance();
                final ExternalContext externalContext = facesContext.getExternalContext();
                final Map<String, Object> requestMap = externalContext.getRequestMap();
                try {
//                    LOG.info("{}: {}", t.getClass().getSimpleName(), t.getMessage());
                    String message;
                    if (t instanceof ViewExpiredException) {
                        final String viewId = ((ViewExpiredException) t).getViewId();
                        message = "View is expired. <a href='/ifos viewId'>Back</a>";
     } else {
      message = t.getMessage(); // beware, don't leak internal info!
                    }
                    requestMap.put("errorMsg", message);
                    try {
                        externalContext.dispatch("/error.jsp");
                    } catch (final IOException e) {
//                        LOG.error("Error view '/error.jsp' unknown!", e);
                    }
                    facesContext.responseComplete();
                } finally {
                    it.remove();
                }
            }
        }
        getWrapped().handle();
    }
}
