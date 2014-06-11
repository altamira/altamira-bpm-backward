package br.com.altamira.erp.entity.services;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.altamira.erp.entity.model.Quotation;
import br.com.altamira.erp.entity.model.QuotationItem;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.util.GenericType;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuotationItemEndpointTest {

    static QuotationItem test_quotationItem;
    static Long newQuotationItemId;
    
        @Test
        //@Ignore
	public void _1testDeleteById() throws Exception {
            
            // get the current quotation
            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target("http://localhost:8080/altamira-bpm/rest/quotations/current");
            Response response = target.request().get();
            
            Object responseObj = response.readEntity(Object.class);
            Quotation quotation = new ObjectMapper().convertValue(responseObj, Quotation.class);
            
            Set<QuotationItem> quotationitems = quotation.getQuotationItem();
            QuotationItem quotationItem = quotationitems.iterator().next();
            
            // store the quotationItem
            test_quotationItem = quotationItem;
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/items/"+test_quotationItem.getId());
            ClientResponse test_response = test_request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/items/"+test_quotationItem.getId());
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
	}
            
	@Test
        //@Ignore
	public void _2testCreate() throws Exception {
            
            // prepare test data
            test_quotationItem.setId(null);
            
            // Do the tests
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/items");
            request.accept(MediaType.APPLICATION_JSON);
            request.body(MediaType.APPLICATION_JSON, test_quotationItem);
            
            ClientResponse<QuotationItem> response = request.post(QuotationItem.class);
            QuotationItem quotationItem = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            Assert.assertNotNull(quotationItem.getId());
            
            // store new quotation item id
            newQuotationItemId = quotationItem.getId();
	}

	@Test
        //@Ignore
	public void _3testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/items/"+newQuotationItemId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<QuotationItem> response = request.get(QuotationItem.class);
            QuotationItem quotationItem = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertEquals(quotationItem.getId(), newQuotationItemId);
	}

	@Test
        //@Ignore
	public void _4testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/items?start=1&max=10");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<QuotationItem> quotationItems = (List<QuotationItem>) response.getEntity(new GenericType<List<QuotationItem>>() {
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(quotationItems);
	}

	@Test
        //@Ignore
	public void _5testUpdate() throws Exception {
            
            // Get the Quotation Item
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/items/"+newQuotationItemId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<QuotationItem> response = request.get(QuotationItem.class);
            QuotationItem quotationItem = response.getEntity();
            
            // prepate test data
            quotationItem.setWeight(new BigDecimal("900.50"));
            quotationItem.setStandard(new BigInteger("2"));
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/items/"+newQuotationItemId);
            test_request.accept(MediaType.APPLICATION_JSON);
            test_request.header("Content-Type", MediaType.APPLICATION_JSON);
            test_request.body(MediaType.APPLICATION_JSON, quotationItem);
            
            ClientResponse<QuotationItem> test_response = test_request.put(QuotationItem.class);
            QuotationItem quotationItemUpdt = test_response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
            Assert.assertEquals(quotationItemUpdt.getWeight(), new BigDecimal("900.50"));
            Assert.assertEquals(quotationItemUpdt.getStandard(), new BigInteger("2"));
	}
}
