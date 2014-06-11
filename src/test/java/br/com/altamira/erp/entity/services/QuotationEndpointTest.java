package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.Quotation;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuotationEndpointTest {
    
    static Long quotationId = new Long(82);

	@Test
        //@Ignore
	public void _1testGetCurrent() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Quotation> response = request.get(Quotation.class);
            Quotation quotation = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(quotation.getId());
            
            quotationId = quotation.getId();
	}
        
        @Test
        @Ignore
	public void _2testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/"+quotationId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Quotation> response = request.get(Quotation.class);
            Quotation quotation = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(quotation.getId());
            Assert.assertEquals(quotation.getId(), quotationId);
	}
        
        @Test
        @Ignore
	public void _3testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<Quotation> quotationList = (List<Quotation>) response.getEntity(new GenericType<List<Quotation>>() {
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertFalse(quotationList.isEmpty());
	}
        
        @Test
        @Ignore
	public void _4testUpdate() throws Exception {
            
            // Get the current quotation
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Quotation> response = request.get(Quotation.class);
            Quotation quotation = response.getEntity();
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/"+quotation.getId());
            test_request.accept(MediaType.APPLICATION_JSON);
            test_request.header("Content-Type", MediaType.APPLICATION_JSON);
            
            ClientResponse<Quotation> test_response = test_request.put(Quotation.class);
            Quotation quotation_res = test_response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
            Assert.assertNotNull(quotation_res.getClosedDate());
	}

	@Test
        @Ignore
	public void _5testDeleteById() throws Exception {
            
            // Get the current quotation
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/"+quotationId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Quotation> response = request.get(Quotation.class);
            Quotation quotation = response.getEntity();
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/"+quotation.getId());
            ClientResponse test_response = test_request.delete();
            
            Long quotationDeleted = quotation.getId();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/"+quotationId);
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
            
	}
}
