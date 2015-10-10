/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.serviceimpls;

import net.hj2eplatform.core.serviceimpls.AbstractService;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.persistence.Query;
import net.hj2eplatform.iservices.IContactUsService;
import net.hj2eplatform.models.ContactUs;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HuyHoang
 */
@Repository
@Transactional
public class ContactUsServiceImpl extends AbstractService<ContactUs> implements IContactUsService, java.io.Serializable {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ContactUs> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filter, Object object) {
        StringBuilder sql = new StringBuilder(" select * from contact_us ctu where 1=1 ");
        buildSql(filter, object, sql);
        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            sql.append(" order by  ctu.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        } else {
            sql.append(" order by  ctu.created_date desc");
        }

        Query query = em.createNativeQuery(sql.toString(), ContactUs.class);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int counter(Map<String, Object> filter, Object object) {
       StringBuilder sql = new StringBuilder(" select count(*) from contact_us ctu where 1=1 ");
       
        buildSql(filter, object, sql);
        Query counterQuery = em.createNativeQuery(sql.toString());

        return ((Long) counterQuery.getSingleResult()).intValue();
    }

    public void buildSql(Map<String, Object> filter, Object object, StringBuilder sql) {
        ContactUs ContactUsSearch = (ContactUs) object;

//        if (invoiceSearch.getTranDateFrom() != null) {
//            sql.append(" and inv.tran_date >= STR_TO_DATE('").append(DateTimeUtils.convertDateToString(invoiceSearch.getTranDateFrom())).append("', '%d/%m/%Y')");
//        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ContactUs getRowKey(String rowKey) {
        if (rowKey == null) {
            return null;
        }

         String sql = "select * from contact_us ctu where ctu.contact_id = ?1 ";
            Query query = em.createNativeQuery(sql.toString(), ContactUs.class);
            query.setParameter(1, Long.valueOf(rowKey));
            return (ContactUs) query.getSingleResult();
    }

    @Override
    public ContactUs getContactUs(Long contactId, Long randomNumber) {
        try {
            String sql = "select * from contact_us ctu where ctu.contact_id = ?1 and ctu.random_number = ?2";
            Query query = em.createNativeQuery(sql.toString(), ContactUs.class);
            query.setParameter(1, contactId);
            query.setParameter(2, randomNumber);
            return (ContactUs) query.getSingleResult();
        } catch (Exception exx) {
            return null;
        }

    }
}
