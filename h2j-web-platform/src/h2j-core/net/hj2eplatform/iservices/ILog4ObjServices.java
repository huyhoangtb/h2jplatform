package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import net.hj2eplatform.core.models.Log4obj;

/**
 *
 * @author HoangNH
 */
public interface ILog4ObjServices extends ILazyDataSupportMapFilter<Log4obj> {

    public final static String TABLE = "TABLE";
    public final static String TABLE_ID = "TABLE_ID";
    public final static String TABLE_FIELD = "TABLE_FIELD";
    public final static String CURRENT_VALUE = "CURRENT_VALUE";
    public final static String NEW_VALUE = "NEW_VALUE";
    public final static String LOG_LEVEL = "LOG_LEVEL";
    public final static String CREATE_DATE_FROM = "CREATE_DATE_FROM";
    public final static String CREATE_DATE_TO = "CREATE_DATE_TO";

    public Log4obj addLog(String table, String tableId, String tableField, String currentValue, String newValue, int logLevel);

    public Log4obj addLogErr(String table, String tableId, String tableField, String currentValue, String newValue);

    public Log4obj addLogWarning(String table, String tableId, String tableField, String currentValue, String newValue);

    public Log4obj addLogInfo(String table, String tableId, String tableField, String currentValue, String newValue);

    public Log4obj addLogFatal(String table, String tableId, String tableField, String currentValue, String newValue);

    public Log4obj addLog(String table, String tableId, String tableField, String currentValue, String newValue, int logLevel, String msgTmpl, String... args);

    public Log4obj addLogErr(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args);

    public Log4obj addLogWarning(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args);

    public Log4obj addLogInfo(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args);

    public Log4obj addLogFatal(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args);

    public Log4obj addLog(Log4obj obj);
}
