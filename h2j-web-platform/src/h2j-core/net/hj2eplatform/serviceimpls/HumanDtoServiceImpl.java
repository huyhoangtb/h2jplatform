package net.hj2eplatform.serviceimpls;

import net.hj2eplatform.core.serviceimpls.AbstractService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import net.hj2eplatform.core.component.LazyDataSupportMapFilter;
import net.hj2eplatform.controller.AuthenticationController;
import net.hj2eplatform.controller.UserController;
import net.hj2eplatform.dto.HumanDto;
import net.hj2eplatform.core.exception.ValidateInputException;
import net.hj2eplatform.iservices.IHumanDtoService;
import net.hj2eplatform.iservices.IHumanService;
import net.hj2eplatform.iservices.IUserService;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.models.Human;
import net.hj2eplatform.models.Users;
import net.hj2eplatform.utils.ControllerName;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.core.utils.DateTimeUtils;
import net.hj2eplatform.core.utils.DataValidator;
import net.hj2eplatform.core.utils.HashData;
import net.hj2eplatform.core.utils.MessagesExceptionUtils;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.utils.HumanType;
import net.hj2eplatform.core.utils.SystemDefine;
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
public class HumanDtoServiceImpl extends AbstractService<HumanDto> implements IHumanDtoService, java.io.Serializable {

    @PersistenceContext
    protected EntityManager em;
    static Logger logger = Logger.getLogger(HumanDtoServiceImpl.class);
    private IHumanService humanService;
    private IUserService userService;

    public void setParameter4Search(Organization organization, LazyDataSupportMapFilter<HumanDto> humanLazyDataModel, HumanDto searchData, Integer orgType) {
        Map filter = new HashMap<String, String>();
        filter.put(IHumanDtoService.USER_USERNAME, searchData.getUsername());
        filter.put(IHumanDtoService.USER_EMAIL, searchData.getEmailAddress());
        filter.put(IHumanDtoService.USER_FULLNAME, searchData.getFullName());
        filter.put(IHumanDtoService.USER_TEL, searchData.getTel());
        filter.put(IHumanDtoService.USER_GENDER, searchData.getGender() == null ? null : searchData.getGender().toString());
        filter.put(IHumanDtoService.ORG_TYPE, orgType.toString());
        if (organization != null) {
            filter.put(IHumanDtoService.USER_PARENT_PATH, organization.getPath());
            filter.put(IHumanDtoService.USER_ORGNAZATION_ROOT_ID, organization.getRootId() == null ? null : organization.getRootId().toString());
            filter.put(IHumanDtoService.USER_ORGNAZATION_ID, organization.getOrganizationId() == null ? null : organization.getOrganizationId().toString());
        }

        if (searchData.getBirthday() != null) {
            filter.put(IHumanDtoService.USER_BITHDAY, DateTimeUtils.convertDateToString(searchData.getBirthday(), DateTimeUtils.ddMMyyyy));
        }

        humanLazyDataModel.setFilters(filter);
    }

    public void saveHumanOnly(HumanDto human, Organization organization, HumanType humanType) {
        validateHumanOnly(human, organization, humanType);

        AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        boolean isupdate = true;
        if (human.getHumanId() == null) {
            isupdate = false;
            human.setHumanId(humanService.getSequence(SystemDefine.SEQUENCE_STAFF_ID).longValue());

        }

        if (humanType.toInteger() == HumanType.RETAIL_CUSTOMER.toInteger()) {
            human.setOrganizationId(authenticationController.getOrganization().getOrganizationId());
            human.setOrgRootId(authenticationController.getOrganization().getRootId());
        } else {
            human.setOrganizationId(organization.getOrganizationId());
            human.setOrgRootId(organization.getRootId());
        }
        Human saveHuman = human.cloneHuman();
        Date today = new Date();
        Human modifiedHuman = AuthenticationController.getCurrentHuman();
        Long modifiedHumanId = modifiedHuman == null ? null : modifiedHuman.getHumanId();
        if (!isupdate) {
            saveHuman.setCreateDate(today);
            if (modifiedHuman != null) {
                saveHuman.setCreateStaffId(modifiedHumanId);
            }
        }

        saveHuman.setModifiedDate(today);
        if (modifiedHuman != null) {
            saveHuman.setModifiedStaffId(modifiedHumanId);
        }

        if (!isupdate) {
            this.humanService.persistEntity(saveHuman);
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("create_user_success"));
        } else {
            this.humanService.saveEntity(saveHuman);
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("update_user_success"));
        }
        UserController userController = ControllerUtils.getBean(ControllerName.USER_CONTROLLER);
        userController.setHuman4InitPermission(human);
    }

    public void saveUserOnly(HumanDto human) {

        boolean isupdate = true;
        Users user = human.cloneUser();
        if (user.getUserId() == null) {
            validateUserAccountOnly(user);
            user.setUserId(getSequence(SystemDefine.SEQUENCE_USER_ID).longValue());
            user.setCreateDate(new Date());
            user.setLoginFailCounter(0);
            isupdate = false;
        }

        if (!isupdate) {
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("create_account_success"));
            this.userService.persistEntity(user);
        } else {
            this.userService.updateUser(user);
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("update_account_success"));
        }
        UserController userController = ControllerUtils.getBean(ControllerName.USER_CONTROLLER);
        userController.setHuman4InitPermission(human);

    }

    @Override
    public void updateUserPassword(Users user) {
        validateUserAccountOnly(user);
        StringBuilder sql = new StringBuilder("update Users user set "
                + "user.password = :password"
                + ", user.mandatoryResetPass = :mandatoryResetPass"
                + " where user.userId = :userId");

        Query query = em.createQuery(sql.toString());
        query.setParameter("password", user.getPassword());
        query.setParameter("mandatoryResetPass", user.getMandatoryResetPass());
        query.setParameter("userId", user.getUserId());

        query.executeUpdate();
        ControllerUtils.addSuccessMessage(ResourceMessages.getResource("update_account_password_success"));
    }

    @Override
    public void saveHuman(HumanDto human, Organization organization, HumanType humanType) {

        validateHuman(human, organization, humanType);

        AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        boolean isupdate = true;
        if (human.getHumanId() == null) {
            isupdate = false;
            human.setHumanId(humanService.getSequence(SystemDefine.SEQUENCE_STAFF_ID).longValue());

        }
        if (human.getUsername() != null && human.getUsername().trim().compareTo("") != 0 && human.getUserId() == null) {
            human.setUserId(getSequence(SystemDefine.SEQUENCE_USER_ID).longValue());
            human.setCreateDate(new Date());
            human.setLoginFailCounter(0);
        }
        if (humanType.toInteger() == HumanType.RETAIL_CUSTOMER.toInteger()) {
            human.setOrganizationId(authenticationController.getOrganization().getOrganizationId());
            human.setOrgRootId(authenticationController.getOrganization().getRootId());
        } else {
            human.setOrganizationId(organization.getOrganizationId());
            human.setOrgRootId(organization.getRootId());
        }
        Human saveHuman = human.cloneHuman();
        Date today = new Date();
        Human modifiedHuman = AuthenticationController.getCurrentHuman();
        Long modifiedHumanId = modifiedHuman == null ? null : modifiedHuman.getHumanId();
        if (!isupdate) {
            saveHuman.setCreateDate(today);
            if (modifiedHuman != null) {
                saveHuman.setCreateStaffId(modifiedHumanId);
            }
        }

        saveHuman.setModifiedDate(today);
        if (modifiedHuman != null) {
            saveHuman.setCreateStaffId(modifiedHumanId);
        }

        this.humanService.saveEntity(saveHuman);

        if (human.getUserId() != null) {
            this.userService.saveEntity(human.cloneUser());
        }
        if (!isupdate) {
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("create_user_success"));
        } else {
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("update_user_success"));
        }
        UserController userController = ControllerUtils.getBean(ControllerName.USER_CONTROLLER);
        userController.setHuman4InitPermission(human);

    }

    public void validateHumanOnly(HumanDto human, Organization organization, HumanType humanType) {
        if (((organization == null || organization.getOrganizationId() == null)
                && humanType.toInteger() == HumanType.STAFF.toInteger())// chi check truong hop khong quan ly mo hinh to chuc
                ) {
            throw new ValidateInputException(ResourceMessages.getResource("user_not_choice_organization"));
        }
        if (human.getFullName() == null || human.getFullName().trim().compareTo("") == 0) {
            throw new ValidateInputException(ResourceMessages.getResource("user_not_enter_fullname"));
        }
        if (human.getGender() == -1) {
            throw new ValidateInputException(ResourceMessages.getResource("user_choice_gender"));
        }
        if (human.getBirthday() == null) {
            throw new ValidateInputException(ResourceMessages.getResource("user_enter_brithday"));
        } else if (!human.getBirthday().before(new Date())) {
            throw new ValidateInputException(ResourceMessages.getResource("user_enter_brithday_invalide"));
        }

        if (human.getEmailAddress() != null || human.getEmailAddress().trim().compareTo("") != 0) {
            human.setEmailAddress(human.getEmailAddress().trim());
            DataValidator.validateEmailAdress(human.getEmailAddress());
            if (humanType.toInteger() == HumanType.RETAIL_CUSTOMER.toInteger()
                    && humanService.validateCustomerByEmail(human.getEmailAddress(), AuthenticationController.getCurrentRootOrg(), human.getHumanId())) {
                MessagesExceptionUtils.addErrorMessages("Địa chỉ email " + human.getEmailAddress() + " đã tồn tại. Vui lòng sử dụng địa chỉ email khác.");
            } else if (humanService.validateHumanByEmail(human.getEmailAddress(), AuthenticationController.getCurrentRootOrg(), human.getHumanId())) {
                MessagesExceptionUtils.addErrorMessages("Địa chỉ email " + human.getEmailAddress() + " đã tồn tại. Vui lòng sử dụng địa chỉ email khác.");
            }

        }

        human.setFullName(DataValidator.deleteSpace(DataValidator.standardName(human.getFullName())));
        human.setAddress(DataValidator.deleteSpace(DataValidator.standardName(human.getAddress())));
    }

    public void validateUserAccountOnly(Users human) {

        if (human.getPassword() == null || human.getPassword().trim().compareTo("") == 0) {
            throw new ValidateInputException(ResourceMessages.getResource("user_not_enter_password"));
        }
        if (human.getRetypePassword() == null || human.getRetypePassword().trim().compareTo("") == 0) {
            throw new ValidateInputException(ResourceMessages.getResource("user_not_enter_retype_password"));
        }
        if (human.getRetypePassword().toLowerCase().compareTo(human.getPassword().toLowerCase()) != 0) {
            throw new ValidateInputException(ResourceMessages.getResource("user_password_not_march"));
        }
        if (human.getUserId() == null) {
            DataValidator.validateUsernameAsEmail(human.getUsername());
            Users u = this.userService.getUserByUsername(human.getUsername());
            if (u != null) {
//                if (human.getUserId() == null) { // them moi user name da ton tai
                throw new ValidateInputException(ResourceMessages.getResource("user_username_exist"));
//                }

//                if (u.getUserId().longValue() != human.getUserId().longValue()) {
//                    throw new ValidateInputException(ResourceMessages.getResource("user_username_exist"));
//                }
            }
        }

        human.setPassword(HashData.hashDocument(human.getPassword()));

    }

    public void validateHuman(HumanDto human, Organization organization, HumanType humanType) {
        if (((organization == null || organization.getOrganizationId() == null)
                && humanType.toInteger() == HumanType.STAFF.toInteger())// chi check truong hop khong quan ly mo hinh to chuc
                ) {
            throw new ValidateInputException(ResourceMessages.getResource("user_not_choice_organization"));
        }
        if (human.getFullName() == null || human.getFullName().trim().compareTo("") == 0) {
            throw new ValidateInputException(ResourceMessages.getResource("user_not_enter_fullname"));
        }
        if (human.getGender() == -1) {
            throw new ValidateInputException(ResourceMessages.getResource("user_choice_gender"));
        }
        if (human.getBirthday() == null) {
            throw new ValidateInputException(ResourceMessages.getResource("user_enter_brithday"));
        } else if (!human.getBirthday().before(new Date())) {
            throw new ValidateInputException(ResourceMessages.getResource("user_enter_brithday_invalide"));
        }

        if (human.getUsername() != null && human.getUsername().trim().compareTo("") != 0) {
            if (human.getUserId() == null || (human.getUserId() != null && ((human.getPassword() != null && human.getPassword().compareTo("") != 0) || (human.getRetypePassword() != null) && human.getRetypePassword().compareTo("") != 0))) {
                if (human.getUserId() == null) {
                    DataValidator.validateUsername(human.getUsername());
                }

                if (human.getPassword() == null || human.getPassword().trim().compareTo("") == 0) {
                    throw new ValidateInputException(ResourceMessages.getResource("user_not_enter_password"));
                }
                if (human.getRetypePassword() == null || human.getRetypePassword().trim().compareTo("") == 0) {
                    throw new ValidateInputException(ResourceMessages.getResource("user_not_enter_retype_password"));
                }
                if (human.getRetypePassword().toLowerCase().compareTo(human.getPassword().toLowerCase()) != 0) {
                    throw new ValidateInputException(ResourceMessages.getResource("user_password_not_march"));
                }
            }

        }

//        if (human.getAddress() == null || human.getAddress().trim().compareTo("") == 0) {
//            throw new ValidateInputException(ResourceMessages.getResource("not_enter_address"));
//        }
        if (human.getEmailAddress() != null || human.getEmailAddress().trim().compareTo("") != 0) {
            human.setEmailAddress(human.getEmailAddress().trim());
            DataValidator.validateEmailAdress(human.getEmailAddress());
            if (humanType.toInteger() == HumanType.RETAIL_CUSTOMER.toInteger()
                    && humanService.validateCustomerByEmail(human.getEmailAddress(), AuthenticationController.getCurrentRootOrg(), human.getHumanId())) {
                MessagesExceptionUtils.addErrorMessages("Địa chỉ email " + human.getEmailAddress() + " đã tồn tại. Vui lòng sử dụng địa chỉ email khác.");
            } else if (humanService.validateHumanByEmail(human.getEmailAddress(), AuthenticationController.getCurrentRootOrg(), human.getHumanId())) {
                MessagesExceptionUtils.addErrorMessages("Địa chỉ email " + human.getEmailAddress() + " đã tồn tại. Vui lòng sử dụng địa chỉ email khác.");
            }

        }

        human.setFullName(DataValidator.deleteSpace(DataValidator.standardName(human.getFullName())));
        human.setAddress(DataValidator.deleteSpace(DataValidator.standardName(human.getAddress())));

        if (human.getUsername() != null && human.getUsername().trim().compareTo("") != 0) {
            Users u = this.userService.getUserByUsername(human.getUsername());
            if (u != null) {
                if (human.getUserId() == null) { // them moi user name da ton tai
                    throw new ValidateInputException(ResourceMessages.getResource("user_username_exist"));
                }

                if (u.getUserId().longValue() != human.getUserId().longValue()) {
                    throw new ValidateInputException(ResourceMessages.getResource("user_username_exist"));
                }
            }
            if (u == null) {// them moi thi ma hoa password luon
                human.setPassword(HashData.hashDocument(human.getPassword()));
            } else if (human.getPassword() != null && human.getPassword().compareTo("") != 0) { // ko phai them moi thi phai kiem tra xem password co khac nhau ko
                human.setPassword(HashData.hashDocument(human.getPassword()));
            } else {
                human.setPassword(u.getPassword());
            }

        }
    }

    public List<HumanDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        StringBuffer sql = new StringBuffer(" SELECT DISTINCT s.human_id,s.organization_id,s.org_root_id,s.first_name,s.last_name,s.full_name,s.avatar_url,s.birthday,s.national_id,s.province_id, "
                + "s.district_id,s.street_id,s.address,s.phone,s.tel,s.email_address,s.possition,s.gender,s.cmt,s.cmt_approve_date,s.cmt_issue_plance_id, "
                + "s.yahoo,s.skype, s.status human_status,s.comments,u.*, u.password as retype_password, s.modified_date  "
                + "from human s LEFT JOIN  users u on s.human_id = u.human_id ");

        this.buildQuery(sql, filters);

        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            sql.append(" order by  s.").append(sortField).append(" ").append(sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
        } else {
            sql.append(" order by  s.modified_date desc");
        }

        Query query = em.createNativeQuery(sql.toString(), HumanDto.class);
        String birthday = (String) filters.get(IHumanDtoService.USER_BITHDAY);
        if (birthday != null) {
            query.setParameter(1, DateTimeUtils.convertStringToDate(birthday, DateTimeUtils.ddMMyyyy), TemporalType.DATE);
        }
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public int counter(Map<String, Object> filters) {
        StringBuffer counter = new StringBuffer("select count(DISTINCT(s.human_id)) from human s LEFT JOIN  users u on s.human_id = u.human_id  ");

        this.buildQuery(counter, filters);

        Query counterQuery = em.createNativeQuery(counter.toString());
        String birthday = (String) filters.get(IHumanDtoService.USER_BITHDAY);
        if (birthday != null) {
            counterQuery.setParameter(1, DateTimeUtils.convertStringToDate(birthday, DateTimeUtils.ddMMyyyy), TemporalType.DATE);
        }

        return ((java.math.BigDecimal) counterQuery.getSingleResult()).intValue();
    }

    private void buildQuery(StringBuffer sql, Map<String, Object> filters) {
        String path = (String) filters.get(IHumanDtoService.USER_PARENT_PATH);
        String orgId = (String) filters.get(IHumanDtoService.USER_ORGNAZATION_ID);
        String humanType = (String) filters.get(IHumanDtoService.STAFF_TYPE);
        String orgRootId = (String) filters.get(IHumanDtoService.USER_ORGNAZATION_ROOT_ID);
        String fullName = (String) filters.get(IHumanDtoService.USER_FULLNAME);
        String email = (String) filters.get(IHumanDtoService.USER_EMAIL);
        String birthday = (String) filters.get(IHumanDtoService.USER_BITHDAY);
        String username = (String) filters.get(IHumanDtoService.USER_USERNAME);
        String tel = (String) filters.get(IHumanDtoService.USER_TEL);
        String gender = (String) filters.get(IHumanDtoService.USER_GENDER);
        String objectType = (String) filters.get(IHumanDtoService.ORG_TYPE);
        AuthenticationController controller = ControllerUtils.getBean(ControllerName.AUTHENTICATION);

        if (fullName != null && fullName.trim().compareTo("") != 0) {
            sql.append(" and s.full_name like '%").append(fullName.trim()).append("%'");
        }
        if (gender != null && gender.trim().compareTo("-1") != 0) {
            sql.append(" and s.gender = '").append(gender).append("'");
        }
        if (tel != null && tel.trim().compareTo("") != 0) {
            sql.append(" and s.tel = '").append(tel.trim()).append("'");
        }
        if (email != null && email.trim().compareTo("") != 0) {
            sql.append(" and s.email_address = '").append(email.trim()).append("'");
        }
        if (username != null && username.trim().compareTo("") != 0) {
            sql.append(" and u.username = '").append(username.trim()).append("'");
        }

        if (birthday != null) {
            sql.append(" and s.birthday = ?1");
        }

        if (orgId == null) {
//            orgId = controller.getOrganization().getOrganizationId().toString();
            path = controller.getOrganization().getPath();
        }
// lay customer ra
        if (humanType != null && Integer.valueOf(humanType) == HumanType.RETAIL_CUSTOMER.toInteger()) {
            sql.append(", organization org  where s.org_root_id = org.root_id and "
                    + " (( org.org_type = 2 and (s.user_type = 1 or s.user_type =2)) "
                    + " OR (org.org_type != 2 and s.user_type = 3)) ");
            sql.append(" AND org.path LIKE '").append(path).append("%' ");
        } else {

            sql.append(" , organization org  where org.organization_id = s.organization_id");
            if (path != null) {
                sql.append(" and org.path like '").append(path).append("%' ");
            }
            sql.append(" and s.org_root_id = ").append(orgRootId);

//            int objectTypeInt = Integer.valueOf(objectType).intValue();
//            switch (objectTypeInt) {
//                case 1://ObjectType.TYPE_STAFF
//                    sql.append(" and org.org_type = ").append(OrgType.ORG_H2J_CENTER.toInteger());
//                    break;
//                case 2://ObjectType.ORG_CUSTOMER neu la kieu khach hang, se gom ca khach lẻ, khach hang thuoc dai ly; nha cung cap vao
//                    sql.append(" and org.org_type = ").append(OrgType.ORG_CUSTOMER.toInteger());
//                    break;
//                case 3://ObjectType.SUPPLIER 
//                    sql.append(" and org.org_type = ").append(OrgType.SUPPLIER.toInteger());
//                    break;
//                case 4://ObjectType.AGENCY 
//                    sql.append(" and org.org_type = ").append(OrgType.AGENCY.toInteger());
//                    break;
//                default:
//                    sql.append(" and org.org_type = ").append(controller.getOrganization().getOrgType());
//                    break;
//            }
            if (humanType != null) {
                if (humanType.compareTo(HumanType.STAFF.toString()) == 0) {
                    sql.append(" and (s.user_type = ").append(HumanType.STAFF.toString())
                            .append(" or s.user_type = ").append(HumanType.DEPUTY_STAFF.toString()).append(")");
                } else {
                    sql.append(" and s.user_type = ").append(humanType);
                }

            }
//            // role admin thi tim trong to chuc cha
//            if (orgRootId != null) {
//                sql.append(" and s.org_root_id = ").append(orgRootId);
//            } else if (controller.getOrganization().getOrgType() != null || controller.getOrganization().getOrgType() != OrgType.ORG_H2J_CENTER.toInteger()) {
//                sql.append(" and s.org_root_id = ").append(controller.getOrganization().getRootId());
//            }
        }

    }

    @Override
    public HumanDto loadHumanDto(Long humanId) {
        try {
            StringBuffer sql = new StringBuffer("SELECT s.human_id,s.organization_id,s.org_root_id,s.first_name,s.last_name,s.full_name,s.avatar_url,s.birthday,s.national_id,s.province_id, "
                    + "s.district_id,s.street_id,s.address,s.phone,s.tel,s.email_address,s.possition,s.gender,s.cmt,s.cmt_approve_date,s.cmt_issue_plance_id, "
                    + "s.yahoo,s.skype, s.status human_status,s.comments,u.*, u.`password` as retype_password  "
                    + "from human s Left JOIN  users u on s.human_id = u.human_id  where s.human_id = ").append(humanId);

            return (HumanDto) em.createNativeQuery(sql.toString(), HumanDto.class).getSingleResult();
        } catch (Exception ex) {
            return null;
        }

    }

    @Override
    public HumanDto getRowKey(String rowKey) {
        if (rowKey == null || rowKey.compareTo("null") == 0) {
            return null;
        }
        try {
            StringBuffer sql = new StringBuffer("SELECT s.human_id,s.organization_id,s.org_root_id,s.first_name,s.last_name,s.full_name,s.avatar_url,s.birthday,s.national_id,s.province_id, "
                    + "s.district_id,s.street_id,s.address,s.phone,s.tel,s.email_address,s.possition,s.gender,s.cmt,s.cmt_approve_date,s.cmt_issue_plance_id, "
                    + "s.yahoo,s.skype, s.status human_status,s.comments,u.*, u.`password` as retype_password  "
                    + "from human s INNER JOIN  users u on s.human_id = u.human_id  where s.human_id = ").append(rowKey);

            return (HumanDto) em.createNativeQuery(sql.toString(), HumanDto.class).getSingleResult();
        } catch (Exception ex) {
            return null;
        }

    }

    public IHumanService getHumanService() {
        return humanService;
    }

    public void setHumanService(IHumanService humanService) {
        this.humanService = humanService;
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void saveHuman2(HumanDto human, Organization organization, HumanType humanType) {
        AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        boolean isupdate = true;
        if (human.getHumanId() == null) {
            isupdate = false;
            human.setHumanId(humanService.getSequence(SystemDefine.SEQUENCE_STAFF_ID).longValue());
        }
        if (human.getUsername() != null && human.getUsername().trim().compareTo("") != 0 && human.getUserId() == null) {
            human.setUserId(getSequence(SystemDefine.SEQUENCE_USER_ID).longValue());
            human.setCreateDate(new Date());
            human.setLoginFailCounter(0);
        }
        if (humanType.toInteger() == HumanType.RETAIL_CUSTOMER.toInteger()) {
            human.setOrganizationId(authenticationController.getOrganization().getOrganizationId());
            human.setOrgRootId(authenticationController.getOrganization().getRootId());
        } else {
            human.setOrganizationId(organization.getOrganizationId());
            human.setOrgRootId(organization.getRootId());
        }
        
        Human saveHuman = human.cloneHuman();
        Date today = new Date();
        Human modifiedHuman = AuthenticationController.getCurrentHuman();
        Long modifiedHumanId = modifiedHuman == null ? null : modifiedHuman.getHumanId();
        if (!isupdate) {
            saveHuman.setCreateDate(today);
            if (modifiedHuman != null) {
                saveHuman.setCreateStaffId(modifiedHumanId);
            }
        }

        saveHuman.setModifiedDate(today);
        if (modifiedHuman != null) {
            saveHuman.setModifiedStaffId(modifiedHumanId);
        }

        this.humanService.saveEntity(saveHuman);

        if (human.getUserId() != null) {
            this.userService.saveEntity(human.cloneUser());
        }
        if (!isupdate) {
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("create_user_success"));
        } else {
            ControllerUtils.addSuccessMessage(ResourceMessages.getResource("update_user_success"));
        }
        UserController userController = ControllerUtils.getBean(ControllerName.USER_CONTROLLER);
        userController.setHuman4InitPermission(human);
    }
}
