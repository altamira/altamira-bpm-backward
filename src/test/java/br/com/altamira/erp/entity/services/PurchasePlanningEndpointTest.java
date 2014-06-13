package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.PurchasePlanning;
import java.util.Date;
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

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PurchasePlanningEndpointTest {

    static Long purchasePlanningId;
    
	@Test
	public void _1getCurrent() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings/current");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<PurchasePlanning> response = request.get(PurchasePlanning.class);
            PurchasePlanning purchasePlanning = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(purchasePlanning.getId());
            
            purchasePlanningId = purchasePlanning.getId();
        }

	@Test
	public void _2testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings/"+purchasePlanningId);
            request.accept(MediaType.APPLICATION_JSON);
             
            ClientResponse<PurchasePlanning> response = request.get(PurchasePlanning.class);
            PurchasePlanning purchasePlanning = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(purchasePlanning.getId());
            Assert.assertEquals(purchasePlanning.getId(), purchasePlanningId);
        }

	@Test
	public void _3testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings?start=1&max=5");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<PurchasePlanning> purchasePlanningList = (List<PurchasePlanning>) response.getEntity(new GenericType<List<PurchasePlanning>>() {
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertFalse(purchasePlanningList.isEmpty());
        }

	@Test
	public void _4testUpdate() throws Exception {
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/"+purchasePlanningId);
            test_request.accept(MediaType.APPLICATION_JSON);
            test_request.header("Content-Type", MediaType.APPLICATION_JSON);
            
            ClientResponse<PurchasePlanning> test_response = test_request.put(PurchasePlanning.class);
            PurchasePlanning purchasePlanning = test_response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
            Assert.assertNotNull(purchasePlanning.getClosedDate());
	}
        
        @Test
        @Ignore
	public void _5testDeleteById() throws Exception {
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings/"+purchasePlanningId);
            ClientResponse test_response = test_request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings/"+purchasePlanningId);
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
	}
}
