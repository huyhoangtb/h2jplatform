package net.hj2eplatform.serviceimpls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import net.hj2eplatform.controller.AuthenticationController;
import net.hj2eplatform.core.iservices.IAbstractService;
import net.hj2eplatform.models.AppParam;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HoangNH
 */
@Repository
@Transactional
public abstract class AbstractService<T> extends net.hj2eplatform.core.serviceimpls.AbstractService<T> implements IAbstractService<T>, Serializable {

    public List<SelectItem> getParramItems(String groupCode, String code) {
        List<AppParam> items = this.getAppParamList(groupCode, code);
        if (items == null) {
            return null;
        }

        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (AppParam param : items) {
            selectItems.add(new SelectItem(param.getParamValue(), param.getParamName()));
        }
        return selectItems;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<AppParam> getAppParamList(String groupCode, String code) {
        try {
            StringBuffer sql = new StringBuffer("select param from AppParam param where param.groupCode = :groupCode and param.status=1");
            if (code != null) {
                sql.append(" and param.paramCode = :paramCode");
            }
            sql.append(" order by param.showOrder");

            Query query = em.createQuery(sql.toString()).setParameter("groupCode", groupCode);
            if (code != null) {
                query.setParameter("paramCode", code);
            }
            return query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public String sqlSearchInLoginOrg() {
        String str = "(select o1.organization_id from organization o1 where "
                + " (o1.group_partner_id = 0 or o1.root_id = o1.organization_id) and o1.root_id = " + AuthenticationController.getCurrentOrg().getRootId()
                + " and EXISTS "
                + " (select 1 from organization o2 where o1.path like CONCAT(o2.path, '%') and o2.organization_id = " + AuthenticationController.getCurrentOrg().getOrganizationId()
                + "  ) )";
        return str;
    }

    public String sqlSearchInOrg(Long rootOrgId, Long curentOrgId) {
        String str = "(select o1.organization_id from organization o1 where "
                + " ((o1.group_partner_id = 0 or o1.root_id = o1.organization_id)) and o1.root_id = " + rootOrgId
                + " and EXISTS "
                + " (select 1 from organization o2 where o1.path like CONCAT(o2.path, '%') and o2.organization_id = " + curentOrgId
                + "  ) )";
        return str;
    }

}
