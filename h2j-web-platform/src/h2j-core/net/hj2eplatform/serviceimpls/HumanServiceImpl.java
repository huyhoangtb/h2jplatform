package net.hj2eplatform.serviceimpls;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import net.hj2eplatform.dto.UserLazyDataModel;
import net.hj2eplatform.iservices.IHumanService;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.models.Human;
import net.hj2eplatform.core.utils.DateTimeUtils;
import net.hj2eplatform.utils.HumanType;
import net.hj2eplatform.utils.WorkingTitleDefine;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HoangNH
 */
@Repository
@Transactional
public class HumanServiceImpl extends AbstractService<Human> implements IHumanService, java.io.Serializable {

    @PersistenceContext
    protected EntityManager em;
    static Logger logger = Logger.getLogger(HumanServiceImpl.class);

    public List<Human> getHumanListByIdArr(String idArr) {
        String sql = "select * from human where human_id in (" + idArr + ")";
        Query query = em.createNativeQuery(sql, Human.class);
        return query.getResultList();
    }

    public List<Human> getHumanByWorkingTitle(Long rootOrgId, HumanType humanType, WorkingTitleDefine titleDefine) {
        StringBuffer sql = new StringBuffer("select human.* from human human where human.status = 1 "
                + " and (human.user_type = 1 or human.user_type = 2)");
        sql.append(" and human.org_root_id = ").append(rootOrgId);
        sql.append(" and human.possition = '").append(titleDefine.getName()).append("'");
        Query query = em.createNativeQuery(sql.toString(), Human.class);
        return query.getResultList();
    }

    public List<Human> suggestHuman(String input, Long rootOrgId, HumanType humanType, WorkingTitleDefine titleDefine, Integer maxRecored, Long notIncludeHumanId) {
        input = input.trim().toUpperCase();
        StringBuffer sql = new StringBuffer("select human.* from human human where human.status = 1 ");
        sql.append(" and (upper(human.full_name) like '%").append(input).append("%'");
        sql.append(" or upper(human.email_address) like '%").append(input).append("%'");
        sql.append(" or upper(human.tel) like '%").append(input).append("%')");
        sql.append(" and human.org_root_id = ").append(rootOrgId);
        if (humanType == HumanType.RETAIL_CUSTOMER) {
            sql.append(" and human.user_type = ").append(HumanType.RETAIL_CUSTOMER.toInteger());
        } else {
            sql.append(" and (human.user_type = 1 or human.user_type = 2)");
        }
        sql.append(" and human.org_root_id = ").append(rootOrgId);
        sql.append(" and human.possition = '").append(titleDefine.getName()).append("'");
        if (notIncludeHumanId != null && notIncludeHumanId > 0) {
            sql.append(" and human.human_id != ").append(notIncludeHumanId);
        }
        Query query = em.createNativeQuery(sql.toString(), Human.class);
        query.setFirstResult(0);
        query.setMaxResults(maxRecored);
        return query.getResultList();
    }

    public List<Human> suggestHuman(String input, Organization org, HumanType humanType, Integer maxRecored) {
        input = input.trim().toUpperCase();
        StringBuffer sql = new StringBuffer("select human.* from human human where human.status = 1 ");
        sql.append(" and (upper(human.full_name) like '%").append(input).append("%'");
        sql.append(" or upper(human.email_address) like '%").append(input).append("%'");
        sql.append(" or upper(human.tel) like '%").append(input).append("%')");
        sql.append(" and human.org_root_id = ").append(org.getOrganizationId());
        if (humanType == HumanType.RETAIL_CUSTOMER) {
            sql.append(" and human.user_type = ").append(HumanType.RETAIL_CUSTOMER.toInteger());
        } else {
            sql.append(" and (human.user_type = 1 or human.user_type = 2)");
        }
        sql.append(" and human.org_root_id = ").append(org.getOrganizationId());

        Query query = em.createNativeQuery(sql.toString(), Human.class);
        query.setFirstResult(0);
        query.setMaxResults(maxRecored);
        return query.getResultList();
    }

    public Boolean validateCustomerByEmail(String email, Organization rootOrg, Long currentCustomerId) {
        email = email.trim().toUpperCase();
        StringBuffer sql = new StringBuffer("SELECT DISTINCT human.* from human , organization org WHERE human.org_root_id = org.root_id and "
                + " (( org.org_type = 2 and (human.user_type = 1 or human.user_type =2)) "
                + " OR (org.org_type != 2 and human.user_type = 3)) ");
        if (currentCustomerId != null) {
            sql.append(" and upper(human.human_id) != ").append(currentCustomerId);
        }
        sql.append(" and upper(human.email_address) = '").append(email).append("'");
        sql.append(" AND org.path LIKE '").append(rootOrg.getPath()).append("%' ");
        Query query = em.createNativeQuery(sql.toString(), Human.class);
        query.setFirstResult(0);
        query.setMaxResults(2);
        if (query.getResultList().size() > 0) {
            return true;
        }
        return false;
    }

    public Boolean validateHumanByEmail(String email, Organization rootOrg, Long currentHumanId) {
        email = email.trim().toUpperCase();
        StringBuffer sql = new StringBuffer("SELECT DISTINCT human.* from human , organization org "
                + " WHERE human.org_root_id = org.root_id  ");
        if (currentHumanId != null) {
            sql.append(" and human.human_id != ").append(currentHumanId);
        }
        sql.append(" and upper(human.email_address) = '").append(email).append("'");
        sql.append(" and org.root_id = ").append(rootOrg.getRootId());
        Query query = em.createNativeQuery(sql.toString(), Human.class);
        query.setFirstResult(0);
        query.setMaxResults(2);
        if (query.getResultList().size() > 0) {
            return true;
        }
        return false;
    }

    public List<Human> suggestCustomer(String input, Organization rootSaleOrg, Integer maxRecored) {
        input = input.trim().toUpperCase();
        StringBuffer sql = new StringBuffer("SELECT DISTINCT human.* from human , organization org WHERE human.org_root_id = org.root_id and "
                + " (( org.org_type = 2 and (human.user_type = 1 or human.user_type =2)) "
                + " OR (org.org_type != 2 and human.user_type = 3)) ");
        sql.append(" and (upper(human.full_name) like '%").append(input).append("%'");
        sql.append(" or upper(human.email_address) like '%").append(input).append("%'");
        sql.append(" or upper(human.tel) like '%").append(input).append("%')");
        sql.append(" AND org.path LIKE '").append(rootSaleOrg.getPath()).append("%' ");
        Query query = em.createNativeQuery(sql.toString(), Human.class);
        query.setFirstResult(0);
        query.setMaxResults(maxRecored);
        return query.getResultList();
    }

    public List<Human> checkExistsHuman(Long rootOrg, String emailAddress, Integer userType, Long currentHumanId) {
        try {
            StringBuffer sql = new StringBuffer("select human.* from human  where  human.user_type = ?1 and human.email_address = ?2 and human.status = 1 and human.org_root_id = ?3 and human.human_id != ?4");
            Query query = em.createNativeQuery(sql.toString(), Human.class);
            query.setParameter(1, userType);
            query.setParameter(2, emailAddress);
            query.setParameter(3, rootOrg);
            query.setParameter(4, currentHumanId);
            return (List<Human>) query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
//            e.printStackTrace();
        }
        return null;
    }

    public List<Human> getHumanByEmailAddressInRootOrg(Long rootOrg, String emailAddress, Integer userType) {
        try {
            StringBuffer sql = new StringBuffer("select human.* from human  where  human.user_type = ?1 and human.email_address = ?2 and human.status = 1 and human.org_root_id = ?3");
            sql.append(" order by human.full_name  ");
            Query query = em.createNativeQuery(sql.toString(), Human.class);
            query.setParameter(1, userType);
            query.setParameter(2, emailAddress);
            query.setParameter(3, rootOrg);
            return (List<Human>) query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
//            e.printStackTrace();
        }
        return null;
    }

    public Human getHumanByEmailAddress(Long orgId, String emailAddress, Integer userType) {
        try {
            StringBuffer sql = new StringBuffer("select human.* from human human where  human.user_type = ?1 and human.email_address = ?2 and human.status = 1 and human.organization_id in ");
            sql.append(sqlSearchInOrg(orgId, orgId));
            sql.append(" order by human.full_name  ");
            Query query = em.createNativeQuery(sql.toString(), Human.class);
            query.setParameter(1, userType);
            query.setParameter(2, emailAddress);
            return (Human) query.getSingleResult();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;

    }

    public List<Human> getHumanByEmail(Long orgId, String emailAddress, Integer userType) {
        StringBuffer sql = new StringBuffer("select human from Human human where human.organizationId = :organizationId and human.userType = :userType and human.emailAddress = :emailAddress and human.status = 1 order by human.fullName");
        Query query = em.createQuery(sql.toString());
        query.setParameter("organizationId", orgId);
        query.setParameter("userType", userType);
        query.setParameter("emailAddress", emailAddress);
        return query.getResultList();
    }

    public List<Human> getHumanInOrg(Long orgId) {
        StringBuffer sql = new StringBuffer("select human from Human human where human.organizationId = :organizationId and (human.userType = 1 or human.userType = 2 ) and human.status = 1 order by human.fullName");
        Query query = em.createQuery(sql.toString());
        query.setParameter("organizationId", orgId);
//        query.setParameter("userType", HumanType.STAFF.toInteger());
        return query.getResultList();
    }

    public List<Human> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        StringBuffer sql = new StringBuffer("select human from Human human, Organization org   where human.organizationId = org.organizationId ");
        this.buildQuery(sql, filters);

        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            sql.append(" order by  human.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        }

        Query query = em.createQuery(sql.toString());
        String birthday = (String)filters.get(UserLazyDataModel.USER_BITHDAY);
        if (birthday != null) {
            query.setParameter("birthday", DateTimeUtils.convertStringToDate(birthday, DateTimeUtils.ddMMyyyy), TemporalType.DATE);
        }
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public int counter(Map<String, Object> filters) {
        StringBuffer counter = new StringBuffer("select count(human) from Human human, Organization org   where human.organizationId = org.organizationId ");
        this.buildQuery(counter, filters);

        Query counterQuery = em.createQuery(counter.toString());
        String birthday = (String)filters.get(UserLazyDataModel.USER_BITHDAY);
        if (birthday != null) {
            counterQuery.setParameter("birthday", DateTimeUtils.convertStringToDate(birthday, DateTimeUtils.ddMMyyyy), TemporalType.DATE);
        }

        return ((Long) counterQuery.getSingleResult()).intValue();
    }

    private void buildQuery(StringBuffer sql, Map<String, Object> filters) {
        String path = (String)filters.get(UserLazyDataModel.USER_PARENT_PATH);
        String orgId = (String)filters.get(UserLazyDataModel.USER_ORGNAZATION_ID);
        String fullName = (String)filters.get(UserLazyDataModel.USER_FULLNAME);
        String email = (String)filters.get(UserLazyDataModel.USER_EMAIL);
        String birthday = (String)filters.get(UserLazyDataModel.USER_BITHDAY);
        String username = (String)filters.get(UserLazyDataModel.USER_USERNAME);
        if (orgId != null) {
            sql.append(" and org.organizationId =").append(orgId);
            sql.append(" and org.path like '").append(path).append("%' ");
        }
        if (fullName != null && fullName.trim().compareTo("") != 0) {
            sql.append(" and human.fullName like '%").append(fullName).append("%'");
        }
        if (email != null && email.trim().compareTo("") != 0) {
            sql.append(" and human.emailAddress = '").append(email).append("'");
        }
        if (username != null && username.trim().compareTo("") != 0) {
            sql.append(" and human.username = '").append(username).append("'");
        }
        if (birthday != null) {
            sql.append(" and human.birthday = :birthday");
        }
    }

    @Override
    public Human getRowKey(String rowKey) {
        if (rowKey == null || rowKey.compareTo("null") == 0) {
            return null;
        }
        return this.loadEntity(Human.class, Long.valueOf(rowKey));
    }
}
