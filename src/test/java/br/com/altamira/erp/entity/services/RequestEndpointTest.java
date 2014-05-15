package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.Request;
import java.math.BigDecimal;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RequestEndpointTest {

	@Test
	public void testDeleteById() throws Exception{
		
            // Do the test
            BigDecimal requestId = new BigDecimal("1");
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+requestId);
            
            ClientResponse response = request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
            
            ClientRequest checkRequest = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+requestId);
            request.accept(MediaType.APPLICATION_JSON);
            ClientResponse checkResponse = request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), checkResponse.getStatus());
            
	}

	@Test
	public void testFindById() throws Exception {
            
            // Do the test
            BigDecimal requestId = new BigDecimal("18");
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+requestId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Request> response = request.get(Request.class);
            Request requestRES = response.getEntity();
	
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}

	@Test
	public void testListAll() throws Exception {
		
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests?start=1&max=10");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<Request> requestsRES = (List<Request>) response.getEntity(new GenericType<List<Request>>() {
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertFalse(requestsRES.isEmpty());
            
	}
        
        @Test
        public void testGetCurrent() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/current");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Request> response = request.get(Request.class);
            Request requestRES = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(requestRES.getId());

        }

	@Test
	public void testUpdate() throws Exception {
		
            // Do the test
            BigDecimal requestId = new BigDecimal("18");
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+requestId);
            request.accept(MediaType.APPLICATION_JSON);
            request.header("Content-Type", MediaType.APPLICATION_JSON);
            
            ClientResponse<Request> response = request.put(Request.class);
            Request requestRES = (Request) response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(requestRES.getSendDate());
            
	}
}
