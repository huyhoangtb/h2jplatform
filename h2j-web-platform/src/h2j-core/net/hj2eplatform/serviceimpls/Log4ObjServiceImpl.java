/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.serviceimpls;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import net.hj2eplatform.controller.AuthenticationController;
import net.hj2eplatform.iservices.ILog4ObjServices;
import static net.hj2eplatform.iservices.ILog4ObjServices.CREATE_DATE_FROM;
import static net.hj2eplatform.iservices.ILog4ObjServices.CREATE_DATE_TO;
import net.hj2eplatform.core.models.Log4obj;
import net.hj2eplatform.core.serviceimpls.AbstractService;
import net.hj2eplatform.core.utils.DateTimeUtils;
import net.hj2eplatform.core.utils.OrgType;
import net.hj2eplatform.core.utils.StatusDefine;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HuyHoang
 */
@Repository
@Transactional
public class Log4ObjServiceImpl extends AbstractService<Log4obj> implements ILog4ObjServices, java.io.Serializable {

    @Override
    public List<Log4obj> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        String createDateFrom = (String)filters.get(CREATE_DATE_FROM);
        String createDateTo = (String)filters.get(CREATE_DATE_TO);
        StringBuilder sql = new StringBuilder("select log from Log4obj log where log.log4objId =   log.log4objId ");
        this.buildQuery(sql, filters);
        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            sql.append(" order by  log.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        }

        Query query = em.createQuery(sql.toString());
        if (createDateFrom != null) {
            query.setParameter("fromDate", DateTimeUtils.convertStringToDate(createDateFrom, DateTimeUtils.ddMMyyyy));
        }
        if (createDateTo != null) {
            query.setParameter("toDate", DateTimeUtils.convertStringToDate(createDateTo, DateTimeUtils.ddMMyyyy));
        }
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public int counter(Map<String, Object> filters) {
        String createDateFrom = (String)filters.get(CREATE_DATE_FROM);
        String createDateTo = (String)filters.get(CREATE_DATE_TO);
        StringBuilder counter = new StringBuilder("select count(log) from Log4obj log where log.log4objId =   log.log4objId ");

        this.buildQuery(counter, filters);

        Query query = em.createQuery(counter.toString());

        if (createDateFrom != null) {
            query.setParameter("fromDate", DateTimeUtils.convertStringToDate(createDateFrom, DateTimeUtils.ddMMyyyy));
        }
        if (createDateTo != null) {
            query.setParameter("toDate", DateTimeUtils.convertStringToDate(createDateTo, DateTimeUtils.ddMMyyyy));
        }


        return ((Long) query.getSingleResult()).intValue();
    }

    private void buildQuery(StringBuilder sql, Map<String, Object> filters) {
        String table = (String)filters.get(TABLE);
        String tableId = (String)filters.get(TABLE_ID);
        String tableField = (String)filters.get(TABLE_FIELD);
        String currentValue = (String)filters.get(CURRENT_VALUE);
        String newValue = (String)filters.get(NEW_VALUE);
        String logLevel = (String)filters.get(LOG_LEVEL);
        String createDateFrom = (String)filters.get(CREATE_DATE_FROM);
        String createDateTo = (String)filters.get(CREATE_DATE_TO);
        if (table != null) {
            sql.append(" and log.log4objTable = ").append(table);
        }
        if (tableId != null) {
            sql.append(" and log.log4obj_table_id = ").append(tableId);
        }
        if (tableField != null) {
            sql.append(" and log.log4objTableField = ").append(tableField);
        }
        if (currentValue != null) {
            sql.append(" and log.currentValue = ").append(currentValue);
        }
        if (newValue != null) {
            sql.append(" and log.newValue = ").append(newValue);
        }
        if (logLevel != null) {
            sql.append(" and log.log4objLevel = ").append(logLevel);
        }
        if (createDateFrom != null) {
            sql.append(" and log.createDate >= :fromDate");
        }
        if (createDateTo != null) {
            sql.append(" and log.createDate <= :toDate");
        }
         if (AuthenticationController.getCurrentOrg() != null && AuthenticationController.getCurrentOrg().getOrgType() != null || AuthenticationController.getCurrentOrg().getOrgType() != OrgType.ORG_H2J_CENTER.toInteger()) {
            sql.append(" and log.orgRootId = ").append(AuthenticationController.getCurrentOrg().getRootId());
        }

    }

    @Override
    public Log4obj getRowKey(String id) {
        if (id == null || id.compareTo("null") == 0) {
            return null;
        }
        try {
            StringBuffer sql = new StringBuffer("select log4obj.* from  log4obj log4obj  where log4obj.log4obj_id = ").append(id);

            return (Log4obj) em.createNativeQuery(sql.toString(), Log4obj.class).getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Log4obj addLog(String table, String tableId, String tableField, String currentValue, String newValue, int logLevel) {
         return this.addLog(table, tableId, tableField, currentValue, newValue, logLevel, null, null);
    }

    @Override
    public Log4obj addLogErr(String table, String tableId, String tableField, String currentValue, String newValue) {
       return this.addLog(table, tableId, tableField, currentValue, newValue, Log4obj.LOG_ERROR, null, null);
    }

    @Override
    public Log4obj addLogWarning(String table, String tableId, String tableField, String currentValue, String newValue) {
        return this.addLog(table, tableId, tableField, currentValue, newValue, Log4obj.LOG_WARNING, null, null);
    }

    @Override
    public Log4obj addLogInfo(String table, String tableId, String tableField, String currentValue, String newValue) {
        return this.addLog(table, tableId, tableField, currentValue, newValue, Log4obj.LOG_INFO, null, null);
    }

    @Override
    public Log4obj addLogFatal(String table, String tableId, String tableField, String currentValue, String newValue) {
        return this.addLog(table, tableId, tableField, currentValue, newValue, Log4obj.LOG_FATAL, null, null);
    }

    @Override
    public Log4obj addLog(String table, String tableId, String tableField, String currentValue, String newValue, int logLevel, String msgTmpl, String... args) {
        Log4obj log4obj = new Log4obj();
        log4obj.setCreateDate(new Date()); 
        log4obj.setCreateUser(AuthenticationController.getCurrentUser() == null ? null : AuthenticationController.getCurrentUser().getUsername());
        log4obj.setCurrentValue(currentValue);
        log4obj.setLog4objLevel(logLevel);
        log4obj.setLog4objTable(table);
        log4obj.setLog4objTableField(tableField);
        log4obj.setLog4objTableId(tableId);
        log4obj.setNewValue(newValue);
        log4obj.setStatus(StatusDefine.activate);
        if(msgTmpl != null) {
            if(args == null) {
                log4obj.setLogMsg(msgTmpl); 
            } else {
                log4obj.setLogMsg(MessageFormat.format(msgTmpl, args));
            }
        }
        this.addLog(log4obj);
       return log4obj;
    }

    @Override
    public Log4obj addLogErr(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args) {
        return this.addLog(table, tableId, tableField, currentValue, newValue, Log4obj.LOG_ERROR, msgTmpl, args);
    }

    @Override
    public Log4obj addLogWarning(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args) {
       return  this.addLog(table, tableId, tableField, currentValue, newValue, Log4obj.LOG_WARNING, msgTmpl, args);
    }

    @Override
    public Log4obj addLogInfo(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args) {
          return this.addLog(table, tableId, tableField, currentValue, newValue, Log4obj.LOG_INFO, msgTmpl, args);
    }

    @Override
    public Log4obj addLogFatal(String table, String tableId, String tableField, String currentValue, String newValue, String msgTmpl, String... args) {
       return  this.addLog(table, tableId, tableField, currentValue, newValue, Log4obj.LOG_FATAL, msgTmpl, args);
    }

    @Override
    public Log4obj addLog(Log4obj obj) {
        obj.setLog4objId(this.getSequence((LOG4OBJSEQ)).longValue());
        if(AuthenticationController.getCurrentOrg() != null) {
            obj.setOrgId(AuthenticationController.getCurrentOrg().getOrganizationId());
            obj.setOrgRootId(AuthenticationController.getCurrentOrg().getRootId().longValue());
        }
        this.persistEntity(obj);
        return obj;
    }
    private static String LOG4OBJSEQ = "LOG4OBJ";
}
