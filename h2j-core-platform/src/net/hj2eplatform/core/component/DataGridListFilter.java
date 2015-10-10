package net.hj2eplatform.core.component;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * make sure we have a session
 */
public class DataGridListFilter implements Filter,  java.io.Serializable{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
//        ((HttpServletRequest) request).getSession();
//        System.out.println("request: " + request);
//        System.out.println("request: " + response);
//        System.out.println("request: " + chain);
        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }

    }

    @Override
    public void destroy() {
    }
}
