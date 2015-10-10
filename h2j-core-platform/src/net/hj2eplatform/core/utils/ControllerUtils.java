package net.hj2eplatform.core.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.FactoryFinder;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public class ControllerUtils {

    public final static Logger logger = Logger.getLogger(ControllerUtils.class);
    public final static String COMMONS = "commons";
    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.ITALY); //fomart số theo kiểu tiếng việt để hiển thị xâu

    public ControllerUtils() {
    }

    public static String convertDateToString(Date d) {
        return new SimpleDateFormat("dd/MM/yyyy").format(d);
    }

    public static <T> T loadService(String serviceName) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getRequest().getSession().getServletContext());
        return (T) ctx.getBean(serviceName);
    }

    public static String concatHTMLByLength(String content, Integer length) {
        String sapoClear = null;
        if (content != null) {
            sapoClear = Jsoup.clean(content, Whitelist.simpleText());
        }

        if (content == null || sapoClear.length() <= length) {
            return content;
        }
        sapoClear = sapoClear.substring(0, length - 3);
        sapoClear = sapoClear.substring(0, sapoClear.lastIndexOf(' '));

        return sapoClear + " ...";
    }

    public static String getStoreUrl() {
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(ControllerUtils.getRequest(), ControllerUtils.getResponse());
        if (savedRequest == null) {
            return null;
        }
        return savedRequest.getRedirectUrl();
    }

    public static String readingCurrency(Double amount) {
        return ConvertCurencyFromNumberToText.converterNumber(amount);
    }

    public static void setParamViewMap(String nameParam, Object value) {
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().put(nameParam, value);
    }

    public static <T> T getParamInViewMap(String nameParam) {
        return (T) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get(nameParam);
    }

    //nhận giá trị param truyền vào từ client
    public static Map<String, Object> getViewMap() {
        return FacesContext.getCurrentInstance().getViewRoot().getViewMap();
    }
    //nhận giá trị param truyền vào từ client

    public static Map<String, Object> getSessionMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }

    public static void setSessionParamester(String nameParam, Object value) {
        getSessionMap().put(nameParam, value);
    }

    public static <T> T getSessionParamester(String nameParam) {
        return (T) getSessionMap().get(nameParam);
    }
    //nhận giá trị param truyền vào từ client

    public static Map<String, Object> getRequestMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
    }
    //nhận giá trị param truyền vào từ client

    public static void setRequestParameter(String nameParam, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(nameParam, value);
    }
    //nhận giá trị param truyền vào từ client

    public static <T> T getRequestParameter(String nameParam) {
        return (T) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(nameParam);
    }
    //nhận giá trị param truyền vào từ client

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) getContext().getExternalContext().getResponse();
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) getContext().getExternalContext().getRequest();
    }

    public static FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }

    public static FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {

            FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
            LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

            facesContext = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response, lifecycle);

//            // Set using our inner class
//            InnerFacesContext.setFacesContextAsCurrentInstance(facesContext);
            // set a new viewRoot, otherwise context.getViewRoot returns null
            UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "");
            facesContext.setViewRoot(view);
        }
        return facesContext;
    }

    public static String getRootPath() {
        FacesContext f_context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) f_context.getExternalContext().getContext();
        String path = servletContext.getRealPath("/");
        return path;
    }

    //get bean's reference from context
    public static <T> T getBean(String beanName) {
        FacesContext context = getContext();
        T bean = (T) context.getApplication().getELResolver().getValue(
                context.getELContext(), null, beanName);
        return bean;
    }

    public static <T> T getDefaultLanguagle(HttpServletRequest request, HttpServletResponse response, String beanName) {
        T bean = (T) ControllerUtils.getFacesContext(request, response).getApplication().getELResolver().getValue(
                ControllerUtils.getFacesContext(request, response).getELContext(), null, beanName);
        return bean;
    }

    public static UIViewRoot getViewRoot() {
        return FacesContext.getCurrentInstance().getViewRoot();
    }

    public static <T> T getBeanViewMap(String beanName) {
        Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
        T viewScopedBean = (T) viewMap.get(beanName);
        return viewScopedBean;
    }

    public static StringBuilder forwardToPage(String page) {
        StringBuilder pageForward = new StringBuilder(page).append("?faces-redirect=true");
        return pageForward;
    }

    ////////////////////////////////////////////////////////////////////////////
    //Add a success message to message queue
    //Using this method when we know the success message
    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        getContext().addMessage(null, facesMsg);
//        getContext().addMessage("successInfo", facesMsg);
    }

    public static String getAccessIp() {
        return getRequest().getRemoteAddr();
    }

    public static String getAccessComputerName() {
        try {
            InetAddress inetAddress = InetAddress.getByName(getRequest().getRemoteAddr());
            String computerName = inetAddress.getHostName();
            if (computerName.endsWith("localhost")) {
                computerName = java.net.InetAddress.getLocalHost().getCanonicalHostName();
            }
            return computerName;
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return "UNKNOW";
    }

    //Add a success message to message queue
    //Using this method when we know the success message
    public static void addSuccessMessage(String path, String key) {
        addSuccessMessage(getString(path, key));
    }

    //Add an error message to message queue
    //Using this method when we know the error message
    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        getContext().addMessage(null, facesMsg);
    }

    public static void addAlertMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        getContext().addMessage(null, facesMsg);
    }

    public static void addAlertMessage(String msg, String key) {
        addAlertMessage(getString(msg, key));
    }

    public static void redirect(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (Exception ex) {
        }

    }

    public static void redirect(HttpServletResponse response, String page) {
        try {
            response.sendRedirect(page);
        } catch (Exception ex) {
        }

    }

    public static void sendRedirect(String page) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.sendRedirect(page);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //Add an error message to message queue
    //Using this method when we know the error message
    public static void addErrorMessage(String path, String key) {
        addErrorMessage(getString(path, key));
    }

    //load properties
    public static String getString(String path, String key) {
        try {
            return ResourceBundle.getBundle(path).getString(key);
        } catch (Exception e) {
            logger.debug(e);
            return "Resource Bundle Erreur";
        }
    }

    ///////////////////////////////////
    public static void clearMessages() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Iterator<String> clients = context.getClientIdsWithMessages();

            while (clients.hasNext()) {
                String clientId = clients.next();
                Iterator<FacesMessage> messages = context.getMessages(clientId);

                while (messages.hasNext()) {
                    messages.next();
                    messages.remove();
                }
            }
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    public static Integer generateRandomIdNumber() {
        return (int) (Math.random() * 2000000000 + 1);
    }

    public static void exportToExcel(Map beans, String template, String responseName) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            OutputStream output = ec.getResponseOutputStream();
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + AccentRemover.removeAccent(responseName) + "\"");
            try {
                InputStream is = new FileInputStream(ControllerUtils.getRootPath() + "WEB-INF/classes/report/" + template);
                XLSTransformer transformer = new XLSTransformer();
                Workbook workbook = transformer.transformXLS(is, beans);
                workbook.write(output);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            fc.responseComplete();

        } catch (Exception exx) {
            exx.printStackTrace();
        }

    }

    public static Integer converToInteger(String input) {
        if (input == null) {
            return null;
        }
        try {
            return Integer.valueOf(input);
        } catch (Exception e) {

        }
        return null;
    }

    public static Long converToLong(String input) {
        if (input == null) {
            return null;
        }
        try {
            return Long.valueOf(input);
        } catch (Exception e) {

        }
        return null;
    }

    public static void main(String[] args) {
        int random = (int) (Math.random() * 2000000000 + 1);
        System.out.println(random);
    }
}
