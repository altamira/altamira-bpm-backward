/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.bpm;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.camunda.bpm.engine.impl.util.json.JSONObject;

/**
 *
 * @author PARTH
 */
public class AltamiraSecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, 
                         ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpReq = (HttpServletRequest)request;
        HttpServletResponse httpRes = (HttpServletResponse)response;
        
        Principal principal = httpReq.getUserPrincipal();
        
        if(principal!=null || httpReq.getRequestURI().contains("/login"))
        {
            chain.doFilter(request, response);
        }
        else if(httpReq.getRequestURI().contains("/quotation/test"))
        {
            chain.doFilter(request, response);
        }
        else
        {
            // send json response
            httpRes.setContentType("application/json");
            PrintWriter printout = httpRes.getWriter();

            JSONObject jobj = new JSONObject("Access Denied: Authorization required");
            
            printout.write(jobj.toString());
        }
        
    }
    
    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void destroy() {
    }
    
}
