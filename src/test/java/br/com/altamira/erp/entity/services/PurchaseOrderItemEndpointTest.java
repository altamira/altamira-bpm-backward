package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.PurchaseOrder;
import br.com.altamira.erp.entity.model.PurchaseOrderItem;
import br.com.altamira.erp.entity.model.PurchasePlanning;
import br.com.altamira.erp.entity.model.PurchasePlanningItem;
import br.com.altamira.erp.entity.model.Supplier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
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
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PurchaseOrderItemEndpointTest {

    static PurchasePlanningItem test_purchasePlanningItem;
    static PurchaseOrder test_purchaseOrder;
    static Long new_purchaseOrderItemId;
    
    @Before
    public void setup() throws Exception {
        
        // Get current Purchase planning
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://localhost:8080/altamira-bpm/rest/purchaseplannings/current");
        Response response = target.request().get();
        
        Object responseObj = response.readEntity(Object.class);
        PurchasePlanning purchasePlanning = new ObjectMapper().convertValue(responseObj, PurchasePlanning.class);
        
        // Create new purchase order
        ClientRequest request_supplier = new ClientRequest("http://localhost:8080/altamira-bpm/rest/suppliers/"+new Long(1));
        request_supplier.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse<Supplier> response_supplier = request_supplier.get(Supplier.class);
        Supplier supplier = response_supplier.getEntity();
        
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchasePlanning(purchasePlanning);
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setCreatedDate(DateTime.now().toDate());
        purchaseOrder.setComments("Comments");
        purchaseOrder.setCompanyInvoice(BigInteger.ONE);
        purchaseOrder.setCompanyBilling(BigInteger.ONE);
        purchaseOrder.setCompanyShipping(BigInteger.ONE);
        purchaseOrder.setShippingTax(BigInteger.ZERO);
        purchaseOrder.setDiscount(BigDecimal.ZERO);
        purchaseOrder.setShippingTerms("CIF");
        
        ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders");
        test_request.accept(MediaType.APPLICATION_JSON);
        test_request.body(MediaType.APPLICATION_JSON, purchaseOrder);
        
        ClientResponse<PurchaseOrder> test_response = test_request.post(PurchaseOrder.class);
        PurchaseOrder purchaseOrderCrtd = test_response.getEntity();
        
        // store purchase order and purchase planning item
        test_purchaseOrder = purchaseOrderCrtd;
        test_purchasePlanningItem = purchasePlanning.getPurchasePlanningItem().iterator().next();
        
    }
    
    @Test
    public void _1testCreate() throws Exception {
        
        // Create new Purchase Order Item entity
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        purchaseOrderItem.setPurchaseOrder(test_purchaseOrder);
        purchaseOrderItem.setPlanningItem(test_purchasePlanningItem);
        purchaseOrderItem.setDate(DateTime.now().toDate());
        purchaseOrderItem.setWeight(BigDecimal.ZERO);
        purchaseOrderItem.setTax(new BigDecimal("18.5"));
        purchaseOrderItem.setStandard(BigDecimal.ONE);
        
        // Do the test
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/purchaseorders/"+test_purchaseOrder.getId()+"/items");
        request.accept(MediaType.APPLICATION_JSON);
        request.body(MediaType.APPLICATION_JSON, purchaseOrderItem);
        
        ClientResponse<PurchaseOrderItem> response = request.post(PurchaseOrderItem.class);
        PurchaseOrderItem purchaseOrderItemCrtd = response.getEntity();
        
        // Check the results
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Assert.assertNotNull(purchaseOrderItemCrtd.getId());
        
        // Store new Purchase Order Item
        new_purchaseOrderItemId = purchaseOrderItemCrtd.getId();
    }

    @Test
    public void _2testFindById() throws Exception {
        
        // Do the test
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/purchaseorders/"+test_purchaseOrder.getId()+"/items/"+new_purchaseOrderItemId);
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse<PurchaseOrderItem> response = request.get(PurchaseOrderItem.class);
        PurchaseOrderItem purchaseOrderItem = response.getEntity();
        
        // Check the results
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(purchaseOrderItem.getId(), new_purchaseOrderItemId);
    }

    @Test
    public void _3testListAll() throws Exception {
        
        // Do the test
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders/"+test_purchaseOrder.getId());
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse response = request.get(ClientResponse.class);
        List<PurchaseOrderItem> purchaseOrderItems = (List<PurchaseOrderItem>) response.getEntity(new GenericType<List<PurchaseOrderItem>>() {
                });
        
        // Check the results
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(purchaseOrderItems);
    }

    @Test
    public void _4testUpdate() throws Exception {
        
        // Get the new Purchase Order
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/purchaseorders/"+test_purchaseOrder.getId()+"/items/"+new_purchaseOrderItemId);
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse<PurchaseOrderItem> response = request.get(PurchaseOrderItem.class);
        PurchaseOrderItem purchaseOrderItem = response.getEntity();
        
        // prepare test data
        purchaseOrderItem.setPrice(new BigDecimal("200"));
        purchaseOrderItem.setWeight(new BigDecimal("20"));
        
        // Do the test
        ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders"+test_purchaseOrder.getId()+"/items/"+new_purchaseOrderItemId);
        test_request.accept(MediaType.APPLICATION_JSON);
        test_request.header("Content-Type", MediaType.APPLICATION_JSON);
        test_request.body(MediaType.APPLICATION_JSON, purchaseOrderItem);
        
        ClientResponse<PurchaseOrderItem> test_response = test_request.put(PurchaseOrderItem.class);
        PurchaseOrderItem purchaseOrderItemUpdt = test_response.getEntity();
        
        // Check the results
        Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
        Assert.assertEquals(purchaseOrderItemUpdt.getPrice(),new BigDecimal("200"));
        Assert.assertEquals(purchaseOrderItemUpdt.getWeight(),new BigDecimal("20"));
    }
        
    @Test
    public void _5testDeleteById() throws Exception {
        
        // Do the test
        ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders/"+test_purchaseOrder.getId()+"/items/"+new_purchaseOrderItemId);
        ClientResponse test_response = test_request.delete();
        
        // Check the results
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
        
        ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/purchaseorders/"+test_purchaseOrder.getId()+"/items/"+new_purchaseOrderItemId);
        check_request.accept(MediaType.APPLICATION_JSON);
        ClientResponse check_response = check_request.get();
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
    }
}
