/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.component;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.ELRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

/**
 *
 * @author hoang_000
 */
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements  java.io.Serializable {

    public CustomAuthenticationSuccessHandler() {
    }

    public CustomAuthenticationSuccessHandler(String defaultUrl) {
        setDefaultTargetUrl(defaultUrl);
        
    }

    /* (non-Javadoc)
     * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        if (RequestUtil.isAjaxRequest(request)) {
            RequestUtil.sendJsonResponse(response, "success", "true");
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }

        
        
        
    }

}

class RequestUtil {

    private static final Logger LOGGER = Logger.getLogger(RequestUtil.class.getName());
    private static final RequestMatcher REQUEST_MATCHER = new ELRequestMatcher("hasHeader('X-Requested-With','XMLHttpRequest')");
    public static final String JSON_VALUE = "{\"%s\": %s}";

    public static Boolean isAjaxRequest(HttpServletRequest request) {
        return REQUEST_MATCHER.matches(request);
    }

    public static void sendJsonResponse(HttpServletResponse response, String key, String message) {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        try {
            response.getWriter().write(String.format(JSON_VALUE, key, message));
        } catch (IOException e) {
            LOGGER.error("error writing json to response", e);
        }
    }

}
