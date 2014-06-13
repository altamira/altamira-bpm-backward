package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.PurchasePlanning;
import br.com.altamira.erp.entity.model.PurchasePlanningItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.util.GenericType;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PurchasePlanningItemEndpointTest {

    static PurchasePlanningItem test_planningItem;
    static Long test_planningId;
    static Long new_planningId;
    
    @Test
    public void _1testDeleteById() throws Exception {
        
        // get the current purchase planning
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/altamira-bpm/rest/purchaseplannings/current");
        Response response = target.request().get();
        
        Object responseObj = response.readEntity(Object.class);
        PurchasePlanning purchasePlanning = new ObjectMapper().convertValue(responseObj, PurchasePlanning.class);
        
        Set<PurchasePlanningItem> purchasePlanningItems = purchasePlanning.getPurchasePlanningItem();
        PurchasePlanningItem purchasePlanningItem = purchasePlanningItems.iterator().next();
        
        // store the purchase planning item
        test_planningItem = purchasePlanningItem;
        test_planningId = purchasePlanning.getId();
        
        // Do the test
        ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/purchaseplannings/"+test_planningId
                                                                                                +"/items/"+test_planningItem.getId());
        ClientResponse test_response = test_request.delete();
        
        // Check the results
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
        
        ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/purchaseplannings"+test_planningId
                                                                                                 +"/items/"+test_planningItem.getId());
        check_request.accept(MediaType.APPLICATION_JSON);
        ClientResponse check_response = check_request.get();
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
    }

    @Test
    public void _2testCreate() throws Exception {
    
        // prepare test data
        test_planningItem.setId(null);
        
        // Do the tests
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings/"+test_planningId+"/items");
        request.accept(MediaType.APPLICATION_JSON);
        request.body(MediaType.APPLICATION_JSON, test_planningItem);
        
        ClientResponse<PurchasePlanningItem> response = request.post(PurchasePlanningItem.class);
        PurchasePlanningItem purchasePlanningItem = response.getEntity();
        
        // Check the results
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Assert.assertNotNull(purchasePlanningItem.getId());
        
        // store new purchase planning item id
        new_planningId = purchasePlanningItem.getId();
    }

    @Test
    public void _3testFindById() throws Exception {
        
        // Do the test
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings/"+test_planningId+"/items/"+new_planningId);
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse<PurchasePlanningItem> response = request.get(PurchasePlanningItem.class);
        PurchasePlanningItem purchasePlanningItem = response.getEntity();
        
        // Check the results
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(purchasePlanningItem.getId(), new_planningId);
    }

    @Test
    public void _4testListAll() throws Exception {
        
        // Do the tests
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings/"+test_planningId+"/items?start=1&max=10");
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse response = request.get(ClientResponse.class);
        List<PurchasePlanningItem> purchasePlanningItems = (List<PurchasePlanningItem>) response.getEntity(new GenericType<List<PurchasePlanningItem>>() {
                });
        
        // Check the results
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(purchasePlanningItems);
    }

    @Test
    public void _5testUpdate() throws Exception {
        
        // Get the Purchase Planning Item
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings/"+test_planningId+"/items/"+new_planningId);
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse<PurchasePlanningItem> response = request.get(PurchasePlanningItem.class);
        PurchasePlanningItem purchasePlanningItem = response.getEntity();
        
        // prepate test data
        purchasePlanningItem.setWeight(new BigDecimal("52.50"));
        
        // Do the test
        ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings"+test_planningId+"/items/"+new_planningId);
        test_request.accept(MediaType.APPLICATION_JSON);
        test_request.header("Content-Type", MediaType.APPLICATION_JSON);
        test_request.body(MediaType.APPLICATION_JSON, purchasePlanningItem);
        
        ClientResponse<PurchasePlanningItem> test_response = test_request.put(PurchasePlanningItem.class);
        PurchasePlanningItem purchasePlanningItemUpdt = test_response.getEntity();
        
        // Check the results
        Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
        Assert.assertEquals(purchasePlanningItemUpdt.getWeight(), new BigDecimal("52.50"));
    }

}
