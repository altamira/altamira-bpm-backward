package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.Standard;
import br.com.altamira.erp.entity.model.Supplier;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SupplierEndpointTest {
	
        static Long supplierId;
    
	@Test
	public void _1testCreate() throws Exception {
            
            // Prepare test data
            Supplier supplier = new Supplier();
            supplier.setName("TEST");
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/suppliers");
            request.accept(MediaType.APPLICATION_JSON);
            request.body(MediaType.APPLICATION_JSON, supplier);
            
            ClientResponse<Supplier> response = request.post(Supplier.class);
            Supplier supplierCrtd = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            Assert.assertNotNull(supplierCrtd.getId());
            
            // store supplier id
            supplierId = supplierCrtd.getId();
	}

	@Test
	public void _2testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/suppliers/"+supplierId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Supplier> response = request.get(Supplier.class);
            Supplier supplier = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertEquals(supplier.getId(), supplierId);
	}

	@Test
	public void _3testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/suppliers?start=1&max=10");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<Supplier> suppliers = (List<Supplier>) response.getEntity(new GenericType<List<Supplier>>() {
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(suppliers);
	}

	@Test
	public void _4testUpdate() throws Exception {
            
            // Get the supplier
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/suppliers/"+supplierId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Supplier> response = request.get(Supplier.class);
            Supplier supplier = response.getEntity();
            
            // Prepare test data
            supplier.setAlias("TEST");
            supplier.setCityTaxId("1");
            supplier.setFederalTaxId("1");
            supplier.setStateTaxId("1");
            
            // Do the tests
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/suppliers/"+supplierId);
            test_request.accept(MediaType.APPLICATION_JSON);
            test_request.header("Content-Type", MediaType.APPLICATION_JSON);
            test_request.body(MediaType.APPLICATION_JSON, supplier);
            
            ClientResponse<Supplier> test_response = test_request.put(Supplier.class);
            Supplier supplierUpdt = test_response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
            Assert.assertEquals(supplierUpdt.getAlias(), "TEST");
            Assert.assertEquals(supplierUpdt.getCityTaxId(), "1");
            Assert.assertEquals(supplierUpdt.getFederalTaxId(), "1");
            Assert.assertEquals(supplierUpdt.getStateTaxId(), "1");
	}
        
        @Test
	public void _5testDeleteById() throws Exception {
            
            // Do the tests
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/suppliers/"+supplierId);
            ClientResponse test_response = test_request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/suppliers/"+supplierId);
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
	}
}
