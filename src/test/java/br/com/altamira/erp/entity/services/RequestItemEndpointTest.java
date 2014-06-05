package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.Material;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.altamira.erp.entity.model.Request;
import br.com.altamira.erp.entity.model.RequestItem;
import java.math.BigDecimal;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RequestItemEndpointTest {
    
    static Long test_requestId;
    static Long test_requestItemId;
    
	@Test
        //@Ignore
	public void _1testCreate() throws Exception {
            
            // Get current Request
            ClientRequest rest_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/current");
            rest_request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Request> response = rest_request.get(Request.class);
            Request requestRES = response.getEntity();
            
            // Find the material
            Material material = new Material();
            material.setId(null);
            material.setLamination("XX");
            material.setLength(new BigDecimal("1.5"));
            material.setTax(new BigDecimal(3.4));
            material.setThickness(new BigDecimal("9.8"));
            material.setTreatment("XX");
            material.setWidth(new BigDecimal("9.9"));
            
            ClientRequest rest_material = new ClientRequest("http://localhost:8080/altamira-bpm/rest/materials");
            rest_material.accept(MediaType.APPLICATION_JSON);
            rest_material.body(MediaType.APPLICATION_JSON, material);
            
            ClientResponse<Material> response_material = rest_material.post(Material.class);
            Material materialResponse = response_material.getEntity();
            
            // Set up test data
            RequestItem requestItem = new RequestItem();
            requestItem.setArrival(DateTime.now().toDate());
            requestItem.setId(null);
            requestItem.setMaterial(materialResponse);
            requestItem.setRequest(requestRES);
            requestItem.setWeight(new BigDecimal("950.6"));
            
            // Do the test
            ClientRequest test_requestItem = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+requestRES.getId()+"/items/");
            test_requestItem.accept(MediaType.APPLICATION_JSON);
            test_requestItem.body(MediaType.APPLICATION_JSON, requestItem);
            
            ClientResponse<RequestItem> response_requestItem = test_requestItem.post(RequestItem.class);
            RequestItem requestItemRES = response_requestItem.getEntity();
            
            // Check the result
            Assert.assertEquals(Response.Status.OK.getStatusCode() ,response_requestItem.getStatus());
            Assert.assertNotNull(requestItemRES.getId());
            
            // store request Item for further use
            test_requestId = requestRES.getId();
            test_requestItemId = requestItemRES.getId();
	}
        
        @Test
        //@Ignore
	public void _2testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+test_requestId+"/items/"+test_requestItemId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<RequestItem> response = request.get(RequestItem.class);
            RequestItem requestItemRES = response.getEntity();
            
            // Check the result
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(requestItemRES.getId());
            Assert.assertEquals(test_requestItemId, requestItemRES.getId());
	}
        
        @Test
        //@Ignore
	public void _3testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+test_requestId+"/items?start=1&max=50");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<RequestItem> requestItems = (List<RequestItem>) response.getEntity(new GenericType<List<RequestItem>>() {
                    });
            
            // Check the result
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertFalse(requestItems.isEmpty());
	}
        
        @Test
        //@Ignore
	public void _4testUpdate() throws Exception {
            
            // Get the request
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+test_requestId+"/items/"+test_requestItemId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<RequestItem> response = request.get(RequestItem.class);
            RequestItem requestItemRES = response.getEntity();
            
            // prepate test data
            Assert.assertNotNull(requestItemRES);
            Assert.assertEquals(requestItemRES.getId(), test_requestItemId);
            
            requestItemRES.setWeight(new BigDecimal("950.6"));
            
            // Do the test
            ClientRequest request_update = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+test_requestId+"/items/"+test_requestItemId);
            request_update.accept(MediaType.APPLICATION_JSON);
            request_update.body(MediaType.APPLICATION_JSON, requestItemRES);
            
            ClientResponse<RequestItem> response_update = request_update.put(RequestItem.class);
            RequestItem requestItemUpdt = response_update.getEntity();
            
            // Check the result
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response_update.getStatus());
            Assert.assertEquals(requestItemUpdt.getWeight(), new BigDecimal("950.6"));
	}

	@Test
        //@Ignore
	public void _5testDeleteById() throws Exception {
            
            // Get the request
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+test_requestId+"/items/"+test_requestItemId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<RequestItem> response = request.get(RequestItem.class);
            RequestItem requestItemRES = response.getEntity();
            
             // Do the test
            ClientRequest request_delete = new ClientRequest("http://localhost:8080/altamira-bpm/rest/requests/"+test_requestId+"/items/"+test_requestItemId);
            request_delete.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response_delete = request_delete.delete();
            
            // Check the result
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response_delete.getStatus());
	}
}
