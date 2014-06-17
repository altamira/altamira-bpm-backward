package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.Quotation;
import br.com.altamira.erp.entity.model.QuotationItem;
import br.com.altamira.erp.entity.model.QuotationItemQuote;
import br.com.altamira.erp.entity.model.Standard;
import br.com.altamira.erp.entity.model.SupplierInStock;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SupplierInStockEndpointTest {

        static QuotationItemQuote test_quotationItemQuote;
        static Standard test_standard;
        static Long supplierInStockId;
    
        @BeforeClass
        public static void setup() throws Exception {
            
            // Get the valid quotation item quote
            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target("http://localhost:8080/altamira-bpm/rest/quotations/current");
            Response response = target.request().get();
            
            Object responseObj = response.readEntity(Object.class);
            Quotation quotation = new ObjectMapper().convertValue(responseObj, Quotation.class);
            
            QuotationItemQuote quotationItemQuote = null;
            for(QuotationItem quotationItem : quotation.getQuotationItem())
            {
                if(quotationItem.getQuotationItemQuote().isEmpty())
                {
                    continue;
                }
                else
                {
                    quotationItemQuote = quotationItem.getQuotationItemQuote().iterator().next();
                    //test_quotationId = quotation.getId();
                    //test_quotationItemId = quotationItem.getId();
                    
                    break;
                }
            }
            
            // store the quotation item quote
            test_quotationItemQuote = quotationItemQuote;
            
            // Get the standard
            Standard standard = new Standard();
            standard.setName("Test");
            standard.setDescription("Test Standard");
            
            // Do the test
            ClientRequest standard_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/standards");
            standard_request.accept(MediaType.APPLICATION_JSON);
            standard_request.body(MediaType.APPLICATION_JSON, standard);
            
            ClientResponse<Standard> standard_response = standard_request.post(Standard.class);
            Standard standardCrtd = standard_response.getEntity();
            
            // store the standard
            test_standard = standardCrtd;
        }
    
	@Test
	public void _1testCreate() throws Exception {
            
            // Prepare test data
            SupplierInStock supplierInStock = new SupplierInStock();
            supplierInStock.setLength(new BigDecimal("1"));
            supplierInStock.setWeight(new BigDecimal("1"));
            supplierInStock.setWidth(new BigDecimal("1"));
            supplierInStock.setQuotationItemQuote(test_quotationItemQuote);
            supplierInStock.setStandard(test_standard);
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierinstocks");
            request.accept(MediaType.APPLICATION_JSON);
            request.body(MediaType.APPLICATION_JSON, supplierInStock);
            
            ClientResponse<SupplierInStock> response = request.post(SupplierInStock.class);
            SupplierInStock supplierInStockCrtd = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            Assert.assertNotNull(supplierInStockCrtd.getId());
            
            // Store supplier in stock id
            supplierInStockId = supplierInStockCrtd.getId();
	}

	@Test
	public void _2testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierinstocks/"+supplierInStockId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<SupplierInStock> response = request.get(SupplierInStock.class);
            SupplierInStock supplierInStock = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertEquals(supplierInStock.getId(), supplierInStockId);
	}

	@Test
	public void _3testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierinstocks?start=1&max=10");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<SupplierInStock> supplierInStocks = (List<SupplierInStock>) response.getEntity(new GenericType<List<SupplierInStock>>() {
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(supplierInStocks);
	}

	@Test
	public void _4testUpdate() throws Exception {
            
            // Get the supplier in stock
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierinstocks/"+supplierInStockId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<SupplierInStock> response = request.get(SupplierInStock.class);
            SupplierInStock supplierInStock = response.getEntity();
            
            // Prepare test data
            supplierInStock.setLength(new BigDecimal("1000"));
            supplierInStock.setWeight(new BigDecimal("200"));
            supplierInStock.setWidth(new BigDecimal("500"));
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierinstocks/"+supplierInStockId);
            test_request.accept(MediaType.APPLICATION_JSON);
            test_request.header("Content-Type", MediaType.APPLICATION_JSON);
            test_request.body(MediaType.APPLICATION_JSON, supplierInStock);
            
            ClientResponse<SupplierInStock> test_response = test_request.put(SupplierInStock.class);
            SupplierInStock supplierInStockUpdt = test_response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
            Assert.assertEquals(supplierInStockUpdt.getLength(), new BigDecimal("1000"));
            Assert.assertEquals(supplierInStockUpdt.getWeight(), new BigDecimal("200"));
            Assert.assertEquals(supplierInStockUpdt.getWidth(), new BigDecimal("500"));
	}
        
        @Test
	public void _5testDeleteById() throws Exception {
            
            // Do the tests
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierinstocks/"+supplierInStockId);
            ClientResponse test_response = test_request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierinstocks/"+supplierInStockId);
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
	}
}
