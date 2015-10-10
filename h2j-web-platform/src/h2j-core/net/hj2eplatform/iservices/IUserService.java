package net.hj2eplatform.iservices;

import net.hj2eplatform.core.iservices.ILazyDataSupportMapFilter;
import net.hj2eplatform.models.Users;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author HoangNH
 */
public interface IUserService extends UserDetailsService, ILazyDataSupportMapFilter<Users> {

    public boolean login(String username, String password);

    public boolean loginAuto(String username, String password);

    public Users getUserByUsername(String username);

    public void updateUser(Users user);

    public void setAuthenticationManager(AuthenticationManager authenticationManager);

    public Users getUserById(Long userId);

    public Users getUserBySocialId(String socialId);

    public Object checkIdFb(String fbId);
    public Authentication loginAjax(String username, String password);
}
