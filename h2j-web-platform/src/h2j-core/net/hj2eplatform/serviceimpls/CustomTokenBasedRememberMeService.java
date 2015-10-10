package net.hj2eplatform.serviceimpls;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

public class CustomTokenBasedRememberMeService extends TokenBasedRememberMeServices {

    @Override
    protected int calculateLoginLifetime(HttpServletRequest request, Authentication authentication) {
        System.out.println("COOKIE: Process1!");
        return super.calculateLoginLifetime(request, authentication);
    }

    @Override
    protected boolean isTokenExpired(long tokenExpiryTime) {
        System.out.println("COOKIE: Process2!");
        return super.isTokenExpired(tokenExpiryTime);
    }

    @Override
    protected String makeTokenSignature(long tokenExpiryTime, String username, String password) {
        System.out.println("COOKIE: Process3!");
        return super.makeTokenSignature(tokenExpiryTime, username, password);
    }

    @Override
    protected String retrievePassword(Authentication authentication) {
        System.out.println("COOKIE: Process4!");
        return super.retrievePassword(authentication);
    }

    @Override
    protected String retrieveUserName(Authentication authentication) {
        System.out.println("COOKIE: Process5!");
        return super.retrieveUserName(authentication);
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("COOKIE: Process6!");
        return super.processAutoLoginCookie(cookieTokens, request, response);
    }    

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        System.out.println("COOKIE: Process7!");
        super.onLoginSuccess(request, response, successfulAuthentication);
    }
}