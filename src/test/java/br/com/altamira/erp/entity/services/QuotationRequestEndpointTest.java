package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.Quotation;
import br.com.altamira.erp.entity.model.QuotationRequest;
import br.com.altamira.erp.entity.model.Request;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuotationRequestEndpointTest {

    static Long quotationRequestId;
    
	@Test
        //@Ignore
	public void _1testCreate() throws Exception {
            
            // Get the current request
            ClientRequest pre_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/current");
            pre_request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Request> pre_response = pre_request.get(Request.class);
            Request request = pre_response.getEntity();
            
            // Get the current quotation
            ClientRequest pre_request1 = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current");
            pre_request1.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Quotation> pre_response1 = pre_request1.get(Quotation.class);
            Quotation quotation = pre_response1.getEntity();
            
            // add current request to current quotation
            QuotationRequest quotationRequest = new QuotationRequest();
            quotationRequest.setQuotation(quotation);
            quotationRequest.setRequest(request);
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/requests");
            test_request.accept(MediaType.APPLICATION_JSON);
            test_request.body(MediaType.APPLICATION_JSON, quotationRequest);
            
            ClientResponse<QuotationRequest> test_response = test_request.post(QuotationRequest.class);
            QuotationRequest quotationRequestRES = test_response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.CREATED.getStatusCode(), test_response.getStatus());
            Assert.assertNotNull(quotationRequestRES.getId());
            
            // store new quotation request id
            quotationRequestId = quotationRequestRES.getId();
	}

	@Test
        //@Ignore
	public void _2testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/requests/"+quotationRequestId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<QuotationRequest> response = request.get(QuotationRequest.class);
            QuotationRequest quotationRequest = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertEquals(quotationRequest.getId(), quotationRequestId);
	}

	@Test
        //@Ignore
	public void _3testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/requests?start=1&max=10");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<QuotationRequest> quotationRequests = (List<QuotationRequest>) response.getEntity(new GenericType<List<QuotationRequest>>() {
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(quotationRequests);
	}

	@Test
        @Ignore
	public void _4testUpdate() throws Exception {
            
            // Not constructive
	}

        @Test
        //@Ignore
	public void _5testDeleteById() throws Exception {
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/requests/"+quotationRequestId);
            ClientResponse test_response = test_request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/requests/"+quotationRequestId);
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
	}
}
