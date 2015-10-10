package net.hj2eplatform.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import net.hj2eplatform.core.exception.ValidateInputException;
import net.hj2eplatform.iservices.IGenerateService;
import net.hj2eplatform.iservices.IOrganizationService;
import net.hj2eplatform.iservices.IHumanDtoService;
import net.hj2eplatform.core.utils.ControllerUtils;
import net.hj2eplatform.iservices.IUserService;
import net.hj2eplatform.models.Organization;
import net.hj2eplatform.models.Human;
import net.hj2eplatform.models.Users;
import net.hj2eplatform.utils.ControllerName;
import static net.hj2eplatform.core.utils.DataValidator.PASSWORD_PATTERN;
import net.hj2eplatform.utils.DefinePermission;
import net.hj2eplatform.utils.DomainType;
import net.hj2eplatform.core.utils.HashData;
import net.hj2eplatform.core.utils.ResourceMessages;
import net.hj2eplatform.utils.HumanType;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Nguyen Huy Hoang
 */
@Controller("authenticationController")
@Scope("request")
public class AuthenticationController implements PhaseListener, Serializable {

    final static Logger logger = Logger.getLogger(AuthenticationController.class);
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IGenerateService service;
//    private Users user;
//    private Human human;
//    private Organization organization;
//    private Organization orgRoot;
    @Autowired
    private AuthenticationManager authenticationManager; // specific for Spring Security
    @Autowired
    SecurityContextRepository repository;
    @Autowired
    private IHumanDtoService humanDtoService;
    @Autowired
    RememberMeServices rememberMeServices;
    private String username = null;
    private String password = null;
    private String passwordChange = null;
    private String retypePasswordChange = null;
    private static Map<String, String> mappingByPermistion;
    private static String PASSWORD_DEFAULT = "";
    private static String SESSION_USER_STORE = "SESSION_USER_STORE";
    private static String SESSION_HUMAN_STORE = "SESSION_HUMAN_STORE";
    private static String SESSION_ORG_STORE = "SESSION_ORG_STORE";
    private static String SESSION_ROOT_ORG_STORE = "SESSION_ROOT_ORG_STORE";

    @PostConstruct
    public void initAuthenticationController() {
    }

    public String gotoProfilePage() {
        if (this.getUser() == null || this.getUser().getUserId() == null) {
            return null;
        }
        return ControllerUtils.forwardToPage("/app/administrator/core/human/edit_profile").toString();
    }

    public String login() {
//        String defaultURL = "http://" + ThemesContext.getInstance().getCurentDomain().getDomainName();
        String defaultURL = null;
        String url = null;

        if (!validateInput()) {
            return null;
        }
        try {
            this.userService.setAuthenticationManager(authenticationManager);

            if (!this.userService.login(username, password)) {
                return null;
            }
            password = null;

            Users user = (Users) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Human human = this.getHuman();
            if (this.getHuman() == null) {
                ControllerUtils.setSessionParamester(SESSION_HUMAN_STORE, (Human) service.loadEntity(Human.class, user.getHumanId()));
            }

            ControllerUtils.setSessionParamester(SESSION_ORG_STORE, organizationService.loadEntity(Organization.class, human.getOrganizationId()));

            if (ThemesContext.getInstance().getDomainType() == DomainType.ADMINISTRATOR.toInteger()) {

                if (isGranted("ROLE_SUPER_ADMIN,ROLE_AS_ADMIN")) {
                    String urlStore = ControllerUtils.getStoreUrl();
                    if (urlStore != null) {
                        FacesContext.getCurrentInstance().getExternalContext().redirect(urlStore);
                        return urlStore;
                    }
                }
                defaultURL = getDefaultLoginPage();
                return defaultURL;
            } else {
                defaultURL = "http://" + ControllerUtils.getRequest().getServerName();
            }
            // redirect ve url truoc do
            RequestCache requestCache = new HttpSessionRequestCache();
            SavedRequest savedRequest = requestCache.getRequest(ControllerUtils.getRequest(), ControllerUtils.getResponse());
            if (savedRequest == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(defaultURL);
                return defaultURL;
            }
            url = savedRequest.getRedirectUrl();
            if (ThemesContext.getInstance().getDomainType() == DomainType.CUSTOMER_SITE.toInteger()
                    && url.compareTo("/app/administrator/login/index.h2j") == 0) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(defaultURL);
                return defaultURL;
            }

            if (url != null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
                return url;
            }

        } catch (UsernameNotFoundException unfx) {
            logger.debug(unfx);
            ControllerUtils.addErrorMessage(unfx.getMessage());
            return null;
        } catch (Exception ex) {
            logger.debug(ex);
            ex.printStackTrace();
            ControllerUtils.addErrorMessage("Tài khoản không tồn tại, vui lòng kiểm tra lại!");
            return null;
        }
        return defaultURL;
    }

    public String login(String u, String p) {
//        String defaultURL = "http://" + ThemesContext.getInstance().getCurentDomain().getDomainName();
        String defaultURL = null;
        String url = null;

        if (!validateInput()) {
            return null;
        }
        try {
            this.userService.setAuthenticationManager(authenticationManager);
            username = u;
            password = p;
            if (!this.userService.loginAuto(username, password)) {
                return null;
            }
            password = null;

            Users user = (Users) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Human human = this.getHuman();
            if (this.getHuman() == null) {
                ControllerUtils.setSessionParamester(SESSION_HUMAN_STORE, (Human) service.loadEntity(Human.class, user.getHumanId()));
            }

            ControllerUtils.setSessionParamester(SESSION_ORG_STORE, organizationService.loadEntity(Organization.class, human.getOrganizationId()));
            if (ThemesContext.getInstance().getDomainType() == DomainType.ADMINISTRATOR.toInteger()) {

                if (isGranted("ROLE_SUPER_ADMIN,ROLE_AS_ADMIN")) {
                    String urlStore = ControllerUtils.getStoreUrl();
                    if (urlStore != null) {
                        FacesContext.getCurrentInstance().getExternalContext().redirect(urlStore);
                        return urlStore;
                    }
                }
                defaultURL = getDefaultLoginPage();
                return defaultURL;
            } else {
                defaultURL = "http://" + ControllerUtils.getRequest().getServerName();
            }
            // redirect ve url truoc do
            RequestCache requestCache = new HttpSessionRequestCache();
            SavedRequest savedRequest = requestCache.getRequest(ControllerUtils.getRequest(), ControllerUtils.getResponse());
            if (savedRequest == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(defaultURL);
                return defaultURL;
            }
            url = savedRequest.getRedirectUrl();
            if (ThemesContext.getInstance().getDomainType() == DomainType.CUSTOMER_SITE.toInteger()
                    && url.compareTo("/app/administrator/login/index.h2j") == 0) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(defaultURL);
                return defaultURL;
            }

            if (url != null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
                return url;
            }

        } catch (UsernameNotFoundException unfx) {
            logger.debug(unfx);
            ControllerUtils.addErrorMessage(unfx.getMessage());
            return null;
        } catch (Exception ex) {
            logger.debug(ex);
            ex.printStackTrace();
            ControllerUtils.addErrorMessage("Tài khoản không tồn tại, vui lòng kiểm tra lại!");
            return null;
        }
        return defaultURL;
    }
//@RequestMapping()

    public void loginOnSite(ActionEvent event) {
        String defaultURL = null;

        String url = null;
        RequestContext context = RequestContext.getCurrentInstance();
        if (!validateInput()) {
            context.addCallbackParam("loggedIn", false);
            return;
        }
        try {
            this.userService.setAuthenticationManager(authenticationManager);

            if (!this.userService.login(username, password)) {
                context.addCallbackParam("loggedIn", false);
                return;
            }
            password = null;
            Users user = (Users) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Human human = this.getHuman();
            if (this.getHuman() == null) {
                ControllerUtils.setSessionParamester(SESSION_HUMAN_STORE, (Human) service.loadEntity(Human.class, user.getHumanId()));
            }
            ControllerUtils.setSessionParamester(SESSION_ORG_STORE, organizationService.loadEntity(Organization.class, human.getOrganizationId()));
            if (ThemesContext.getInstance().getDomainType() == DomainType.ADMINISTRATOR.toInteger()) {
                if (isGranted("ROLE_SUPER_ADMIN,ROLE_AS_ADMIN")) {
                    String urlStore = ControllerUtils.getStoreUrl();
                    if (urlStore != null) {
                        context.addCallbackParam("loggedIn", true);
                        FacesContext.getCurrentInstance().getExternalContext().redirect(urlStore);
                        context.addCallbackParam("loggedIn", true);
                        return;
                    }
                }
                context.addCallbackParam("loggedIn", true);
                return;
            } else {
                context.addCallbackParam("loggedIn", true);
                defaultURL = "http://" + ControllerUtils.getRequest().getServerName();
            }
            // redirect ve url truoc do
            RequestCache requestCache = new HttpSessionRequestCache();
            SavedRequest savedRequest = requestCache.getRequest(ControllerUtils.getRequest(), ControllerUtils.getResponse());
            if (savedRequest == null) {
                context.addCallbackParam("loggedIn", true);
                FacesContext.getCurrentInstance().getExternalContext().redirect(defaultURL);
                return;
            }
            url = savedRequest.getRedirectUrl();
            if (ThemesContext.getInstance().getDomainType() == DomainType.CUSTOMER_SITE.toInteger()
                    && url.compareTo("/app/administrator/login/index.h2j") == 0) {
                context.addCallbackParam("loggedIn", true);
                FacesContext.getCurrentInstance().getExternalContext().redirect(defaultURL);
                return;
            }

            if (url != null) {
                context.addCallbackParam("loggedIn", true);
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            }

        } catch (UsernameNotFoundException unfx) {
            logger.debug(unfx);
            context.addCallbackParam("loggedIn", false);
            ControllerUtils.addErrorMessage(unfx.getMessage());
            return;
        } catch (Exception ex) {
            logger.debug(ex);
            ex.printStackTrace();
            context.addCallbackParam("loggedIn", false);
            ControllerUtils.addErrorMessage("Tài khoản không tồn tại, vui lòng kiểm tra lại!");
            return;
        }
    }

    public String doLogin() throws IOException, ServletException {
        this.userService.setAuthenticationManager(authenticationManager);
        Authentication auth = this.userService.loginAjax(username, password);
        if (auth == null) {
            return null;
        }
        password = null;
        SecurityContextHolder.getContext().setAuthentication(auth);
//        rememberMeServices.loginSuccess(ControllerUtils.getRequest(), ControllerUtils.getResponse(), auth);
        Users user = (Users) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Human human = this.getHuman();
        if (this.getHuman() == null) {
            ControllerUtils.setSessionParamester(SESSION_HUMAN_STORE, (Human) service.loadEntity(Human.class, user.getHumanId()));
        }

        ControllerUtils.setSessionParamester(SESSION_ORG_STORE, organizationService.loadEntity(Organization.class, human.getOrganizationId()));
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//                .getRequestAttributes()).getRequest();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check?j_username=" + username + "&j_password=" + HashData.hashDocument(password));

        dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());

        FacesContext.getCurrentInstance().responseComplete();

        // It's OK to return null here because Faces is just going to exit.
        return null;
    }

    public void changePassword() {
        if (!PASSWORD_DEFAULT.equals(AuthenticationController.getCurrentUser().getPassword())) {
            if ((password == null || password.compareTo("") == 0)) {
                ControllerUtils.addErrorMessage("Bạn chưa nhập mật khẩu cũ!");
                return;
            }
            if (this.getUser().getPassword().compareTo(HashData.hashDocument(password)) != 0) {
                ControllerUtils.addErrorMessage("Mật khẩu cũ bạn nhập không đúng. Vui lòng kiểm tra lại!");
                return;
            }
        }

        if (passwordChange == null || passwordChange.compareTo("") == 0) {
            ControllerUtils.addErrorMessage("Bạn chưa nhập mật khẩu mới!");
            return;
        }
        Pattern emailParttern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = emailParttern.matcher(passwordChange);
        if (!matcher.matches()) {
            ControllerUtils.addErrorMessage("Mật khẩu mới bạn nhập không hợp lệ, Vui lòng kiểm tra lại!");
            return;
        }
        if (retypePasswordChange == null || retypePasswordChange.compareTo("") == 0) {
            ControllerUtils.addErrorMessage("Vui lòng xác nhận mật khẩu mới!");
            return;
        }
        if (passwordChange.compareTo(retypePasswordChange) != 0) {
            ControllerUtils.addErrorMessage("Mật khẩu mới không khớp giữ 2 lần nhập, Vui lòng kiểm tra lại!");
            return;
        }
        this.getUser().setPassword(HashData.hashDocument(passwordChange));
        this.userService.saveEntity(this.getUser());
        ControllerUtils.addSuccessMessage("Bạn đã đổi mật khẩu đăng nhập thành công!");
    }

    public String logout() {
        try {
            System.exit(0);
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_logout");
            dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
            FacesContext.getCurrentInstance().responseComplete();

            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false); //true
            session.invalidate();

        } catch (ServletException se) {
            logger.debug(se);
            ControllerUtils.addErrorMessage(se.getMessage());
            return null;
        } catch (IOException ex) {
            logger.debug(ex);
            ControllerUtils.addErrorMessage(ex.getMessage());
            return null;
        } catch (Exception ex) {
            logger.debug(ex);
            return null;
        }

        return "/";
    }

    public static AuthenticationController getCurrentInstance() {
        AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        return authenticationController;
    }

    public static Human getCurrentHuman() {
        AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        return authenticationController.getHuman();
    }

    public static Users getCurrentUser() {
        AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        return authenticationController.getUser();
    }

    public static Organization getCurrentOrg() {
        AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);
        return authenticationController.getOrganization();
    }

    public static Organization getCurrentRootOrg() {
        AuthenticationController authenticationController = ControllerUtils.getBean(ControllerName.AUTHENTICATION);

        return authenticationController.getRootOrg();
    }

    public Organization getRootOrg() {
        Organization rootOrg = ControllerUtils.getSessionParamester(SESSION_ROOT_ORG_STORE);
        if (rootOrg != null) {
            return rootOrg;
        }
        Organization org = this.getOrganization();
        if (org != null && org.getOrganizationId() != null) {
            rootOrg = organizationService.loadEntity(Organization.class, org.getRootId());
            ControllerUtils.setSessionParamester(SESSION_ROOT_ORG_STORE, rootOrg);
        }
        return null;
    }

    //phục vụ composite: resources/ficomer/security.xhtml (check quyền của human để ẩn hiện theo đúng ý đồ)
    public Boolean isGranted(String moduleCodeArr) {
        try {

            if (moduleCodeArr == null || "".equals(moduleCodeArr)) {
                return false;
            }
            String[] agrs = moduleCodeArr.split(",");

            Collection<GrantedAuthority> grantedAuthoritys = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

            for (GrantedAuthority authority : grantedAuthoritys) {
                for (String st : agrs) {
                    if (st.trim().toUpperCase().equals(authority.getAuthority().toUpperCase())) {
                        return true;
                    }
                }

            }

        } catch (Exception e) {

        }
        return false;
    }
    //phục vụ composite: resources/ficomer/security.xhtml (check quyền của human để ẩn hiện theo đúng ý đồ)

    public static Boolean isGrantedSingle(String per) {
        if (per == null || "".equals(per)) {
            return false;
        }

        Collection<GrantedAuthority> grantedAuthoritys = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (GrantedAuthority authority : grantedAuthoritys) {
            if (per.trim().toUpperCase().equals(authority.getAuthority().toUpperCase())) {
                return true;
            }

        }

        return false;
    }

    private boolean validateInput() {

        if (this.username == null || this.username.trim().compareTo("") == 0) {
            ControllerUtils.addErrorMessage("Vui lòng nhập tên đăng nhập.");
            return false;
        }
        if ((this.password == null || this.password.trim().compareTo("") == 0) && !this.getUser().getPassword().equals(PASSWORD_DEFAULT)) {

            ControllerUtils.addErrorMessage("Vui lòng nhập mật khẩu.");
            return false;
        }
        return true;

    }

    public Human getHuman() {
        Human human = ControllerUtils.getSessionParamester(SESSION_HUMAN_STORE);
        if (human != null) {
            return human;
        }
        try {
            Users user = this.getUser();
            human = (Human) service.loadEntity(Human.class, user.getHumanId());
            ControllerUtils.setSessionParamester(SESSION_HUMAN_STORE, human);
            return human;
        } catch (Exception e) {
            return null;
        }

    }

    public void setHuman(Human human) {
        ControllerUtils.setSessionParamester(SESSION_HUMAN_STORE, human);
    }
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public IUserService getUserService() {
        return userService;
    }

    public Organization getOrganization() {
        Organization organization = ControllerUtils.getSessionParamester(SESSION_ORG_STORE);
        if (organization != null) {
            return organization;
        }
        Human human = this.getHuman();
        organization = organizationService.loadEntity(Organization.class, human.getOrganizationId());
        return organization;
    }

    public String getPasswordChange() {
        return passwordChange;
    }

    public void setPasswordChange(String passwordChange) {
        this.passwordChange = passwordChange;
    }

    public String getRetypePasswordChange() {
        return retypePasswordChange;
    }

    public void setRetypePasswordChange(String retypePasswordChange) {
        this.retypePasswordChange = retypePasswordChange;
    }

//    public void setOrganization(Organization organization) {
//        this.organization = organization;
//    }
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public Users getUser() {
        Users user = (Users) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return user;
    }

//    public void setUser(Users user) {
//        this.user = user;
//    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void saveLoginUserHuman() {
        try {
            HumanType humanType = null;
            Human human = this.getHuman();
            if (human.getUserType() == null || human.getUserType() == HumanType.RETAIL_CUSTOMER.toInteger()) {
                humanType = HumanType.RETAIL_CUSTOMER;
                human.setUserType(HumanType.RETAIL_CUSTOMER.toInteger());
            } else if (human.getUserType() == HumanType.DEPUTY_STAFF.toInteger()) {
                human.setUserType(HumanType.DEPUTY_STAFF.toInteger());
            } else {//(human.getUserType() == HumanType.STAFF.toInteger()) {
                human.setUserType(HumanType.STAFF.toInteger());
            }
            this.service.saveEntity(human);

        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }

    }

    public void updatePassword() {
        try {
            this.humanDtoService.updateUserPassword(this.getUser());
        } catch (ValidateInputException ex) {
            ControllerUtils.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            ControllerUtils.addErrorMessage(ResourceMessages.getResource("system_error"));
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static String getDefaultLoginPage() {
        if (mappingByPermistion == null) {
            mappingByPermistion = new HashMap<String, String>();
//            mappingByPermistion.put(DefinePermission.PER_PRODUCT_INTRODUCTION.toString(), "admin_tour_list");
//            mappingByPermistion.put(DefinePermission.PER_SUB_PARTNER.toString(), "admin_sale_tour_group_as_agent");
//            mappingByPermistion.put(DefinePermission.ROLE_AS_ADMIN.toString(), "admin_tour_list");
//            mappingByPermistion.put(DefinePermission.ROLE_SUPER_ADMIN.toString(), "site-manager-menu");
//            mappingByPermistion.put(DefinePermission.ROLE_MARKETING_ONLINE.toString(), "site-manager-menu");
//            mappingByPermistion.put(DefinePermission.PER_SALE_ALL.toString(), "admin_sale_my_tour_group");
//            mappingByPermistion.put(DefinePermission.PER_PRODUCTION_ALL.toString(), "admin_tour_list");
//            mappingByPermistion.put(DefinePermission.PER_OPERATOR_ALL.toString(), "admin_sale_my_tour_group");
//            mappingByPermistion.put(DefinePermission.PER_ACCOUNT_ALL.toString(), "invoice_retail");
        }
        Collection<GrantedAuthority> grantedAuthoritys = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (GrantedAuthority authority : grantedAuthoritys) {
            if (DefinePermission.ROLE_SUPER_ADMIN.toString().equals(authority.getAuthority().toUpperCase())) {
                return mappingByPermistion.get(DefinePermission.ROLE_SUPER_ADMIN.toString());
            }
            if (DefinePermission.PER_OPERATOR_ALL.toString().equals(authority.getAuthority().toUpperCase())) {
                return mappingByPermistion.get(DefinePermission.PER_OPERATOR_ALL.toString());
            }
            if (DefinePermission.PER_SALE_ALL.toString().equals(authority.getAuthority().toUpperCase())) {
                return mappingByPermistion.get(DefinePermission.PER_SALE_ALL.toString());
            }
            if (DefinePermission.PER_ACCOUNT_ALL.toString().equals(authority.getAuthority().toUpperCase())) {
                return mappingByPermistion.get(DefinePermission.PER_ACCOUNT_ALL.toString());
            }
            if (DefinePermission.PER_PRODUCTION_ALL.toString().equals(authority.getAuthority().toUpperCase())) {
                return mappingByPermistion.get(DefinePermission.PER_PRODUCTION_ALL.toString());
            }
            if (DefinePermission.ROLE_AS_ADMIN.toString().equals(authority.getAuthority().toUpperCase())) {
                return mappingByPermistion.get(DefinePermission.ROLE_AS_ADMIN.toString());
            }
            if (DefinePermission.PER_PRODUCT_INTRODUCTION.toString().equals(authority.getAuthority().toUpperCase())) {
                return mappingByPermistion.get(DefinePermission.PER_PRODUCT_INTRODUCTION.toString());
            }
            if (DefinePermission.PER_SUB_PARTNER.toString().equals(authority.getAuthority().toUpperCase())) {
                return mappingByPermistion.get(DefinePermission.PER_SUB_PARTNER.toString());
            }
            if (DefinePermission.ROLE_MARKETING_ONLINE.toString().equals(authority.getAuthority().toUpperCase())) {
                return mappingByPermistion.get(DefinePermission.ROLE_MARKETING_ONLINE.toString());
            }
        }
//        return "/";
        return "/app/administrator/welcome.xhtml";
    }

    public void afterPhase(PhaseEvent event) {
    }

    /* (non-Javadoc)
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     *
     * Do something before rendering phase.
     */
    public void beforePhase(PhaseEvent event) {
        Exception e = (Exception) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get(WebAttributes.AUTHENTICATION_EXCEPTION);

        if (e instanceof BadCredentialsException) {
            logger.debug("Found exception in session map: " + e);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
                    WebAttributes.AUTHENTICATION_EXCEPTION, null);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Username or password not valid.", "Username or password not valid"));
        }
    }

    /* (non-Javadoc)
     * @see javax.faces.event.PhaseListener#getPhaseId()
     *
     * In which phase you want to interfere?
     */
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
