/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.core.component;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * huyhoang for java platform. (h2j-platform)
 * @author HuyHoang
 * @Email: huyhoang85_tb@yahoo.com
 * @Tel: 0966298666
 */
public class H2jSiteFilter implements Filter, java.io.Serializable {

    private static String filterName = "FILTER_STARTED";
    private static String DOMAIN_IN_SESSION = "DOMAIN_IN_SESSION";
    private static String DOMAIN_IN_DEFAULT_LANGUAGLE = "DOMAIN_IN_DEFAULT_LANGUAGLE";

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding("UTF-8"); // fix loi unicode
        try {

            StringBuffer url = request.getRequestURL();
            if (url.toString().startsWith("http://www.")) {
                url.delete(0, 11);

                response.sendRedirect("http://" + url.toString());
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            } else if (url.toString().startsWith("https://www.")) {
                url.delete(0, 12);

                response.sendRedirect("https://" + url.toString());
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            if (request.getRequestURI().length() == 1) {
                System.out.println(request.getServerName());
                if (request.getServerName().toLowerCase().startsWith("admin")) {
                    response.sendRedirect("/app/administrator/index.h2j");
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                } else {
                    response.sendRedirect("/index.h2j");
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void destroy() {
    }
}
