/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.utils;

import net.hj2eplatform.core.models.Log4obj;
import net.hj2eplatform.iservices.ILog4ObjServices;
import net.hj2eplatform.core.utils.ControllerUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Huy Hoang
 */
public class Log4objUtils {

    private static synchronized ILog4ObjServices instanceService() {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(ControllerUtils.getRequest().getSession().
                getServletContext());
        ILog4ObjServices log4ObjServices = (ILog4ObjServices) ctx.getBean("log4ObjServiceImpl");
        return log4ObjServices;
    }

    private Log4objUtils() {
    }

    public static Log4obj addLog(String table, String tableId, String tableField, String currentValue, String newValue, int logLevel) {
        return instanceService().addLog(table, tableId, tableField, currentValue, newValue, logLevel);
    }

    public static Log4obj addLogErr(String table, String tableId, String tableField, String currentValue, String newValue) {
        return instanceService().addLogErr(table, tableId, tableField, currentValue, newValue);
    }

    public static Log4obj addLogWarning(String table, String tableId, String tableField, String currentValue, String newValue) {
        return instanceService().addLogWarning(table, tableId, tableField, currentValue, newValue);
    }

    public static Log4obj addLogInfo(String table, String tableId, String tableField, String currentValue, String newValue) {
        return instanceService().addLogInfo(table, tableId, tableField, currentValue, newValue);
    }

    public static Log4obj addLogFatal(String table, String tableId, String tableField, String currentValue, String newValue) {
        return instanceService().addLogFatal(table, tableId, tableField, currentValue, newValue);
    }

    public static Log4obj addLog(String table, String tableId, String tableField, String currentValue, String newValue, int logLevel, String msgTmpl, String... args) {
        return instanceService().addLog(table, tableId, tableField, currentValue, newValue, logLevel, msgTmpl, args);
    }

    public static Log4obj addLogErr(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args) {
        return instanceService().addLogErr(table, tableId, tableField, currentValue, newValue, msgTmpl, args);
    }

    public static Log4obj addLogWarning(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args) {
        return instanceService().addLogWarning(table, tableId, tableField, currentValue, newValue, msgTmpl, args);
    }

    public static Log4obj addLogInfo(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args) {
        return instanceService().addLogInfo(table, tableId, tableField, currentValue, newValue, msgTmpl, args);
    }

    public static Log4obj addLogFatal(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args) {
        return instanceService().addLogFatal(table, tableId, tableField, currentValue, newValue, msgTmpl, args);
    }

    public static Log4obj addLog(Log4obj obj) {
        return instanceService().addLog(obj);
        
    }
    public static void main(String[] args) {
        Log4objUtils.addLog("Staff", "10", "Trans_Total", "0", "100.000", Log4obj.LOG_INFO, "Tang so luong giao dich tu {0} len {10}", new String[]{"0", "100.000"});
    }
}
