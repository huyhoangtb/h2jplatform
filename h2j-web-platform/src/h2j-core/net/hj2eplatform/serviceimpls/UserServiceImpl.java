package net.hj2eplatform.serviceimpls;

import net.hj2eplatform.core.serviceimpls.AbstractService;
import java.util.ArrayList;
import java.util.Date;
import net.hj2eplatform.iservices.IUserService;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import net.hj2eplatform.dto.UserLazyDataModel;
import net.hj2eplatform.context.H2jContextDefine;
import net.hj2eplatform.controller.AuthenticationController;
import net.hj2eplatform.core.models.SecurityPermission;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.models.Human;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.HashData;
import net.hj2eplatform.models.Users;
import net.hj2eplatform.core.utils.DateTimeUtils;
import net.hj2eplatform.utils.DefinePermission;
import net.hj2eplatform.utils.PartnerContractType;
import net.hj2eplatform.utils.HumanType;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HoangNH
 */
@Repository
@Transactional
public class UserServiceImpl extends AbstractService<Users> implements IUserService, java.io.Serializable {

    @PersistenceContext
    protected EntityManager em;
    @Autowired
    private IOrganizationService organizationService;
    static Logger logger = Logger.getLogger(UserServiceImpl.class);
//    @Autowired
    private AuthenticationManager authenticationManager; // specific for Spring Security

    @Override
    public Authentication loginAjax(String username, String password) {
        try {
            String passHash = null;
            if (password != null) {
                passHash = HashData.hashDocument(password);
            }
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username, passHash));
            if (authenticate.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(
                        authenticate);
                return authenticate;
            }
        } catch (NoResultException ex) {
            System.out.println("ngoai+le+1" + ex);
            ControllerUtils.addErrorMessage("Tên đăng nhập hoặc mật khẩu bạn nhập chưa đúng.");
            return null;

        } catch (AuthenticationException e) {
            System.out.println("ngoai+le+2" + e);
            ControllerUtils.addErrorMessage("Tên đăng nhập hoặc mật khẩu bạn nhập chưa đúng.");
            return null;
        }
        return null;
    }
    @Override
    public boolean login(String username, String password) {
        try {
            String passHash = null;
            if (password != null) {
                passHash = HashData.hashDocument(password);
            }
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username, passHash));
            if (authenticate.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(
                        authenticate);
                return true;
            }
        } catch (NoResultException ex) {
            System.out.println("ngoai+le+1" + ex);
            ControllerUtils.addErrorMessage("Tên đăng nhập hoặc mật khẩu bạn nhập chưa đúng.");
            return false;

        } catch (AuthenticationException e) {
            System.out.println("ngoai+le+2" + e);
            ControllerUtils.addErrorMessage("Tên đăng nhập hoặc mật khẩu bạn nhập chưa đúng.");
            return false;
        }
        return false;
    }

    @Override
    public boolean loginAuto(String username, String password) {
        try {
            String passHash = null;
            passHash = password;
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username, passHash));
            if (authenticate.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(
                        authenticate);
                return true;
            }
        } catch (NoResultException ex) {
            System.out.println("ngoai+le+1" + ex);
            ControllerUtils.addErrorMessage("Tên đăng nhập hoặc mật khẩu bạn nhập chưa đúng.");
            return false;

        } catch (AuthenticationException e) {
            System.out.println("ngoai+le+2" + e);
            ControllerUtils.addErrorMessage("Tên đăng nhập hoặc mật khẩu bạn nhập chưa đúng.");
            return false;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Users loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException { //inteface của Spring security
        Users user = getAccount(username);

        if (!checkByUserName(user)) {
            return null;
        }

        List<SecurityPermission> permissionList = new ArrayList<SecurityPermission>();

        SecurityPermission per = getDefaultPermission(user);
        if (per != null) {
            permissionList.add(per);
        } else {
            Human human = (Human) loadObject(Human.class, user.getHumanId());
            if (human != null && human.getUserType() == HumanType.RETAIL_CUSTOMER.toInteger()) {
                SecurityPermission securityPermission = new SecurityPermission();
                securityPermission.setPermissionCode(DefinePermission.CUSTOMER.toString());
                AuthenticationController.getCurrentInstance().setHuman(human);
                permissionList.add(securityPermission);
            } else {
                permissionList = this.getPermission(user.getUserId(), user.getOrganizationId()); //lấy danh sách quyền luôn
            }

        }
//        }
        if (permissionList == null || permissionList.size() <= 0) {
            ControllerUtils.addErrorMessage("Bạn không có quyền truy cập hệ thống.");
            return null;
        }

        user.setPermissionList(permissionList);

        return user;
    }

    private SecurityPermission getDefaultPermission(Users user) {
        SecurityPermission securityPermission = new SecurityPermission();
        Organization root = organizationService.getRootOrgById(user.getOrganizationId());

        if (root.getOrganizationId().longValue() == H2jContextDefine.getH2jRootOrg().getOrganizationId().longValue()) {
            if (user.getIsAdmin() != null && user.getIsAdmin().intValue() == 1) {
                securityPermission.setPermissionCode(DefinePermission.ROLE_SUPER_ADMIN.toString());
            } else {
                securityPermission = null;
            }

            return securityPermission;
        }

        if (root.getContractType() == null || root.getContractType().intValue() == PartnerContractType.SUB_PARTNER.toInteger()) {
            securityPermission.setPermissionCode(DefinePermission.PER_SUB_PARTNER.toString());
            return securityPermission;
        }
        if (root.getContractType().intValue() == PartnerContractType.PRODUCT_INTRODUCTION_PARTNER.toInteger()) {
            securityPermission.setPermissionCode(DefinePermission.PER_PRODUCT_INTRODUCTION.toString());
            return securityPermission;
        }
        if (root.getContractType().intValue() == PartnerContractType.PRODUCT_INTRODUCTION_PARTNER_TEST_SOFTWARE.toInteger()) {
            Date today = new Date();
            if ((root.getContractDateFrom() == null || !today.before(root.getContractDateFrom()))
                    && (root.getContractDateTo() == null || !today.after(root.getContractDateTo()))
                    && (user.getIsAdmin() != null && user.getIsAdmin().intValue() == 1)) {
                securityPermission.setPermissionCode(DefinePermission.ROLE_AS_ADMIN.toString());
            } else if ((user.getIsAdmin() != null && user.getIsAdmin().intValue() == 1)) {
                securityPermission.setPermissionCode(DefinePermission.PER_PRODUCT_INTRODUCTION.toString());
            } else {
                securityPermission = null;
            }

            return securityPermission;
        }
        if (root.getContractType().intValue() == PartnerContractType.CONTRACT_PARTNER_NO1.toInteger()
                || root.getContractType().intValue() == PartnerContractType.CONTRACT_PARTNER_NO2.toInteger()
                || root.getContractType().intValue() == PartnerContractType.CONTRACT_PARTNER_NO3.toInteger()
                || root.getContractType().intValue() == PartnerContractType.CONTRACT_PARTNER_NO4.toInteger()) {
            Date today = new Date();
            if ((root.getContractDateFrom() == null || !today.before(root.getContractDateFrom()))
                    && (root.getContractDateTo() == null || !today.after(root.getContractDateTo()))
                    && (user.getIsAdmin() != null && user.getIsAdmin().intValue() == 1)) {
                securityPermission.setPermissionCode(DefinePermission.ROLE_AS_ADMIN.toString());
            } else if ((user.getIsAdmin() != null && user.getIsAdmin().intValue() == 1)) {
                securityPermission.setPermissionCode(DefinePermission.PER_PRODUCT_INTRODUCTION.toString());
            } else {
                securityPermission = null;
            }

            return securityPermission;
        }

        return null;

    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Users getAccount(String username) {
        try {
            String sql = "select user from Users user where upper(user.username) = :username or upper(user.socialId) = :username";
            Query query = em.createQuery(sql).setParameter("username", username.toUpperCase());
            return (Users) query.getSingleResult();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;

    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<SecurityPermission> getPermission(Long userId, Long orgId) {
        String sql = " (select p1.permission_id, p1.organization_id, p1.permission_code, p1.permission_name, p1.type, p1.status, p1.comments, p1.full_control, up.allow_delete as allow_delete, up.allow_edit as allow_edit, up.allow_view as allow_view from user_permission up INNER JOIN permission p1 ON up.permission_id = p1.permission_id where     p1.status = 1 and up.user_id=  " + userId + ")"
                + " UNION  "
                + " (select  p2.permission_id, p2.organization_id, p2.permission_code, p2.permission_name, p2.type, p2.status, p2.comments, p2.full_control,rp.allow_delete as allow_delete, rp.allow_edit as allow_edit, rp.allow_view as allow_view  from role_permission rp inner join user_role ur on rp.role_id = ur.role_id  "
                + " inner JOIN permission p2 on rp.permission_id = p2.permission_id where   p2.status = 1 and ur.status=1 and ur.user_id= " + userId + ")";
        if (orgId != null) {
            sql = sql + " UNION "
                    + " (SELECT p3.permission_id, p3.organization_id, p3.permission_code, p3.permission_name, p3.type, p3.status, p3.comments, p3.full_control,rp.allow_delete as allow_delete, rp.allow_edit as allow_edit, rp.allow_view as allow_view  from role_permission rp inner join org_role orr on rp.role_id = orr.role_id  "
                    + " inner join permission p3 on  rp.permission_id = p3.permission_id where p3.status=1 and rp.status=1 and  orr.organization_id = " + orgId + ")"
                    + " UNION "
                    + " (select p4.permission_id, p4.organization_id, p4.permission_code, p4.permission_name, p4.type, p4.status, p4.comments, p4.full_control,op.allow_delete as allow_delete, op.allow_edit as allow_edit, op.allow_view as allow_view  from org_permission op inner JOIN permission p4 on op.permission_id = p4.permission_id where  op.status=1 and p4.status=1  and  op.organization_id = " + orgId + ")";
        }

        Query query = em.createNativeQuery(sql, SecurityPermission.class);
        return query.getResultList();
    }

    public static boolean checkByUserName(Users user) {
        if (user == null) {
            ControllerUtils.addErrorMessage("Tài khoản của bạn ko tồn tại.");
            return false;
        }

        if (user.getStatus() != 1) {
            ControllerUtils.addErrorMessage("Tên đăng nhập của bạn không có hiệu lực.");
            return false;
        }
        Date now = new Date();
        if (user.getStartDate() != null && now.before(user.getStartDate())) {
            ControllerUtils.addErrorMessage("Tài khoản chưa hiệu lực. Vui lòng đăng nhập trở lại sau ngày " + DateTimeUtils.convertDateToString(now, DateTimeUtils.ddMMyyyy));
            return false;
        }

        if (user.getEndDate() != null && now.after(user.getEndDate())) {
            ControllerUtils.addErrorMessage("Tài khoản của bạn đã hết hiệu lực từ ngày " + DateTimeUtils.convertDateToString(user.getEndDate(), DateTimeUtils.ddMMyyyy));
            return false;
        }
        return true;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public List<Users> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        StringBuffer sql = new StringBuffer("select user from Users user, Organization org   where user.organizationId = org.organizationId ");
        this.buildQuery(sql, filters);

        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            sql.append(" order by  user.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        }

        Query query = em.createQuery(sql.toString());
        String birthday = (String) filters.get(UserLazyDataModel.USER_BITHDAY);
        if (birthday != null) {
            query.setParameter("birthday", DateTimeUtils.convertStringToDate(birthday, DateTimeUtils.ddMMyyyy), TemporalType.DATE);
        }
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public Users getUserById(Long userId) {
        String sql = "select user from Users user where user.userId = :userId";
        Query query = em.createQuery(sql).setParameter("userId", userId);
        return (Users) query.getSingleResult();
    }

    public Users getUserBySocialId(String socialId) {
        String sql = "select user from Users user where user.socialId = :socialId";
        Query query = em.createQuery(sql).setParameter("socialId", socialId);
        return (Users) query.getSingleResult();
    }

    @Override
    public Object checkIdFb(String fbId) {
        String sql = "SELECT count(*) FROM users WHERE social_id = ?1";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, fbId);
        return ((Long) query.getSingleResult()).intValue();
    }

    public int counter(Map<String, Object> filters) {
        StringBuffer counter = new StringBuffer("select count(user) from Users user, Organization org   where user.organizationId = org.organizationId ");
        this.buildQuery(counter, filters);

        Query counterQuery = em.createQuery(counter.toString());
        String birthday = (String) filters.get(UserLazyDataModel.USER_BITHDAY);
        if (birthday != null) {
            counterQuery.setParameter("birthday", DateTimeUtils.convertStringToDate(birthday, DateTimeUtils.ddMMyyyy), TemporalType.DATE);
        }

        return ((Long) counterQuery.getSingleResult()).intValue();
    }

    private void buildQuery(StringBuffer sql, Map<String, Object> filters) {
        String path = (String) filters.get(UserLazyDataModel.USER_PARENT_PATH);
        String orgId = (String) filters.get(UserLazyDataModel.USER_ORGNAZATION_ID);
        String fullName = (String) filters.get(UserLazyDataModel.USER_FULLNAME);
        String email = (String) filters.get(UserLazyDataModel.USER_EMAIL);
        String birthday = (String) filters.get(UserLazyDataModel.USER_BITHDAY);
        String username = (String) filters.get(UserLazyDataModel.USER_USERNAME);
        if (orgId != null) {
            sql.append(" and org.organizationId =").append(orgId);
            sql.append(" and org.path like '").append(path).append("%' ");
        }
        if (fullName != null && fullName.trim().compareTo("") != 0) {
            sql.append(" and user.fullName like '%").append(fullName).append("%'");
        }
        if (email != null && email.trim().compareTo("") != 0) {
            sql.append(" and user.emailAddress = '").append(email).append("'");
        }
        if (username != null && username.trim().compareTo("") != 0) {
            sql.append(" and user.username = '").append(username).append("'");
        }
        if (birthday != null) {
            sql.append(" and user.birthday = :birthday");
        }

    }

    public Users getUserByUsername(String username) {
        try {
            String sql = "select user from Users user where user.username = :username ";
            Query query = em.createQuery(sql);
            query.setParameter("username", username);
            return (Users) query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void updateUser(Users user) {
        StringBuilder sql = new StringBuilder("update Users user set "
                + "user.organizationId = :organizationId"
                + ", user.belongId = :belongId"
                + ", user.mandatoryResetPass = :mandatoryResetPass"
                + ", user.startDate = :startDate"
                + ", user.endDate = :endDate"
                + ", user.status = :status"
                + ", user.note = :note"
                + " where user.userId = :userId");

        Query query = em.createQuery(sql.toString());
        query.setParameter("organizationId", user.getOrganizationId());
        query.setParameter("belongId", user.getHumanId());
        query.setParameter("mandatoryResetPass", user.getMandatoryResetPass());
        query.setParameter("startDate", user.getStartDate(), TemporalType.TIMESTAMP);
        query.setParameter("endDate", user.getEndDate(), TemporalType.TIMESTAMP);
        query.setParameter("status", user.getStatus());
        query.setParameter("note", user.getNote());
        query.setParameter("userId", user.getUserId());

        query.executeUpdate();
    }

    @Override
    public Users getRowKey(String rowKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
