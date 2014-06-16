package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.PurchaseOrder;
import br.com.altamira.erp.entity.model.PurchasePlanning;
import br.com.altamira.erp.entity.model.Supplier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PurchaseOrderEndpointTest {

    static Long new_purchaseOrderId;
    
    @Test
    public void _1testCreate() throws Exception {
        
        // Get current purchase planning
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseplannings/current");
        request.accept(MediaType.APPLICATION_JSON);
            
        ClientResponse<PurchasePlanning> response = request.get(PurchasePlanning.class);
        PurchasePlanning purchasePlanning = response.getEntity();
        
        // create new Purchase Order entity
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
        
        // Do the test
        ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders");
        test_request.accept(MediaType.APPLICATION_JSON);
        test_request.body(MediaType.APPLICATION_JSON, purchaseOrder);
        
        ClientResponse<PurchaseOrder> test_response = test_request.post(PurchaseOrder.class);
        PurchaseOrder purchaseOrderCrtd = test_response.getEntity();
        
        // Check the results
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), test_response.getStatus());
        Assert.assertNotNull(purchaseOrderCrtd.getId());
        
        // store new purchase order id
        new_purchaseOrderId = purchaseOrderCrtd.getId();
    }
    
    @Test
    public void _2testFindById() throws Exception {
        
        // Do the test
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders/"+new_purchaseOrderId);
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse<PurchaseOrder> response = request.get(PurchaseOrder.class);
        PurchaseOrder purchaseOrder = response.getEntity();
        
        // Check the results
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(purchaseOrder.getId(), new_purchaseOrderId);
    }
    
    @Test
    @Ignore
    public void _3testListAll() throws Exception {
        
        // Do the test
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders?start=1&max=10");
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse response = request.get(ClientResponse.class);
        List<PurchaseOrder> purchaseOrders = (List<PurchaseOrder>) response.getEntity(new GenericType<List<PurchaseOrder>>() {
                });
        
        // Check the results
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(purchaseOrders);
    }
    
    @Test
    public void _4testUpdate() throws Exception {
        
        // Get the purchase order
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders/"+new_purchaseOrderId);
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse<PurchaseOrder> response = request.get(PurchaseOrder.class);
        PurchaseOrder purchaseOrder = response.getEntity();
        
        // Prepate test data
        purchaseOrder.setComments("Comments updated");
        purchaseOrder.setCompanyInvoice(BigInteger.ZERO);
        purchaseOrder.setCompanyBilling(BigInteger.ZERO);
        purchaseOrder.setCompanyShipping(BigInteger.ZERO);
        purchaseOrder.setShippingTerms("FOB");
        
        // Do the test
        ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders/"+new_purchaseOrderId);
        test_request.accept(MediaType.APPLICATION_JSON);
        test_request.header("Content-Type", MediaType.APPLICATION_JSON);
        test_request.body(MediaType.APPLICATION_JSON, purchaseOrder);
        
        ClientResponse<PurchaseOrder> test_response = test_request.put(PurchaseOrder.class);
        PurchaseOrder purchaseOrderUpdt = test_response.getEntity();
        
        // Check the results
        Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
        Assert.assertEquals(purchaseOrderUpdt.getComments(), "Comments updated");
        Assert.assertEquals(purchaseOrderUpdt.getCompanyInvoice(), BigInteger.ZERO);
        Assert.assertEquals(purchaseOrderUpdt.getCompanyBilling(), BigInteger.ZERO);
        Assert.assertEquals(purchaseOrderUpdt.getCompanyShipping(), BigInteger.ZERO);
        Assert.assertEquals(purchaseOrderUpdt.getShippingTerms(), "FOB");
    }

    @Test
    public void _5testDeleteById() throws Exception {
        
        // Do the test
        ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders/"+new_purchaseOrderId);
        ClientResponse test_response = test_request.delete();
        
        // Check the results
        Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
        
        ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/purchaseorders/"+new_purchaseOrderId);
        check_request.accept(MediaType.APPLICATION_JSON);
        ClientResponse check_response = check_request.get();
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
    }
}
