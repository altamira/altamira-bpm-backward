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
import java.util.Collection;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

public class JBossCorsFilter implements Filter {

	public JBossCorsFilter() {
	}

	public void init(FilterConfig fc) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		/*
		System.out.print("************************ START FILTER: " + ((HttpServletRequest)request).getRequestURI() + " ************************\n");
		
		System.out.print("----------------- ServletRequest BEFORE apply cors headers -----------------------------\n");

		Enumeration<String> requestHeaderNames = ((HttpServletRequest)request).getHeaderNames();

		while (requestHeaderNames.hasMoreElements()) {

			String headerName = requestHeaderNames.nextElement();

			Enumeration<String> headers = ((HttpServletRequest)request).getHeaders(headerName);
			while (headers.hasMoreElements()) {
				String headerValue = headers.nextElement();
				System.out.print(headerName + " = " + headerValue + "\t\n");
			}

		}

		System.out.print("----------------- ServletResponse BEFORE apply cors headers -----------------------------\n");

		Collection<String> responseHeaderNames = ((HttpServletResponse)response).getHeaderNames();

		for (String headerName : responseHeaderNames) {

			Collection<String> headers = ((HttpServletResponse)response).getHeaders(headerName);
			for (String headerValue : headers) {
				System.out.print(headerName + " = " + headerValue + "\t\n");
			}

		}

        System.out.print("------------------ APPLY CORS HEADER -----------------------------\n");
        */
        
		HttpServletResponse r = (HttpServletResponse) response;

		//r.addHeader("Access-Control-Allow-Origin", "*");
		/*r.addHeader("Access-Control-Allow-Headers",
				"Accept,Accept-Encoding,Accept-Language,Cache-Control,Connection,Content-Length,Content-Type,"
						+ "Cookie,Host,Pragma,Referer,RemoteQueueID,User-Agent");*/
		
		r.addHeader("Access-Control-Allow-Credentials", "true");
		r.setHeader("Access-Control-Allow-Origin", "*");
		r.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		r.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With");
		r.setHeader("Access-Control-Max-Age", "86400");

		r.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");

		if (!"OPTIONS".equals(((HttpServletRequest)request).getMethod())) {
			chain.doFilter(request, response);
		}

		/*System.out.print("----------------- ServletRequest AFTER apply cors headers -----------------------------\n");
		
		requestHeaderNames = ((HttpServletRequest)request).getHeaderNames();

		while (requestHeaderNames.hasMoreElements()) {

			String headerName = requestHeaderNames.nextElement();

			Enumeration<String> headers = ((HttpServletRequest)request).getHeaders(headerName);
			while (headers.hasMoreElements()) {
				String headerValue = headers.nextElement();
				System.out.print(headerName + " = " + headerValue + "\t\n");

			}

		}
        
		System.out.print("------------------- ServletResponse AFTER apply cors headers -----------------------------\n");
		
		responseHeaderNames = ((HttpServletResponse)response).getHeaderNames();

		for (String headerName : responseHeaderNames) {

			Collection<String> headers = ((HttpServletResponse)response).getHeaders(headerName);
			for (String headerValue : headers) {
				System.out.print(headerName + " = " + headerValue + "\t\n");
			}

		}

		System.out.print("************************ END FILTER: " + ((HttpServletRequest)request).getRequestURI() + " ************************\n");
		*/
	}
	
	public void doOptions(HttpServletRequest request, HttpServletResponse response)
	        throws IOException {
		//System.out.print("***** START OPTIONS: " + ((HttpServletRequest)request).getRequestURI() + "-------------------------------\n");

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With");
		response.setHeader("Access-Control-Max-Age", "86400");

		response.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
		
		//System.out.print("***** END OPTIONS: " + ((HttpServletRequest)request).getRequestURI() + "-------------------------------\n");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException {
		//System.out.print("***** START GET: " + ((HttpServletRequest)request).getRequestURI() + "-------------------------------\n");

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With");
		response.setHeader("Access-Control-Max-Age", "86400");

		response.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");

		//System.out.print("***** END GET: " + ((HttpServletRequest)request).getRequestURI() + "-------------------------------\n");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException {
		//System.out.print("***** START POST: " + ((HttpServletRequest)request).getRequestURI() + "-------------------------------\n");

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With");
		response.setHeader("Access-Control-Max-Age", "86400");

		response.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
		
		//System.out.print("***** END POST: " + ((HttpServletRequest)request).getRequestURI() + "-------------------------------\n");
	}
}
