/**
 * A servlet filter that includes the HTTP headers to allow <a
 * href="http://www.html5rocks.com/en/tutorials/cors/">cross-origin resource
 * sharing</a> from browsers that support CORS.
 * <p>
 * This code was adapted from the sample code found at <a href=
 * "http://padcom13.blogspot.com/2011/09/cors-filter-for-java-applications.html"
 * >Matthias Hryniszak's blog</a>. Thanks, Matthias!
 * <p>
 * To use this filter to make a "remote" ErraiBus accessible from a webapp, add
 * this to your web.xml:
 * <p>
 * 
 *  <filter>
 *    <filter-name>CorsFilter</filter-name>
 *    <filter-class>org.jboss.errai.bus.server.servlet.CorsFilter</filter-class>
 *  </filter>
 *  <filter-mapping>
 *    <filter-name>CorsFilter</filter-name>
 *    <url-pattern>*.erraiBus</url-pattern>
 *  </filter-mapping>
 * 
 * @author Matthias Hryniszak - original version
 * @author Jonathan Fuerth - updates for Errai
 */

package br.com.altamira.erp.entity.services;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JBossCorsFilter implements Filter {

  public JBossCorsFilter() {
  }

  public void init(FilterConfig fc) throws ServletException {
  }

  public void destroy() {
  }

  public void doFilter(ServletRequest  request, ServletResponse  response,
          FilterChain chain) throws IOException, ServletException {

      HttpServletRequest req = (HttpServletRequest)request;                                   
      HttpServletResponse res = (HttpServletResponse)response;  

      r.addHeader("Access-Control-Allow-Credentials", "true");
      res.addHeader("Access-Control-Allow-Headers",
              "Accept, Accept-Encoding, Accept-Language, Cache-Control, Connection, Content-Length, Content-Type," +
              "Cookie, Host, Pragma, Referer, RemoteQueueID, User-Agent, X-Requested-With");

      if(req.getHeader("Origin") != null){
          res.addHeader("Access-Control-Allow-Origin", "*");
          res.addHeader("Access-Control-Expose-Headers", "X-Cache-Date");
      }

      if("OPTIONS".equals(req.getMethod())){
          res.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
          res.addHeader("Access-Control-Allow-Headers", "Origin, Content-Type, X-Cache-Date");
          res.addHeader("Access-Control-Max-Age", "-1");
      } else {
      
    	  chain.doFilter(req, res);
    	  
      }

  }
}
