package br.com.altamira.erp.entity.services;

import static org.junit.Assert.fail;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.altamira.erp.entity.model.Quotation;
import br.com.altamira.erp.entity.model.QuotationItem;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuotationItemEndpointTest {

    static QuotationItem test_quotationItem;
    static Long newQuotationItemId;
    
        @Test
	public void _1testDeleteById() throws Exception {
            
            // get the current quotation
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Quotation> response = request.get(Quotation.class);
            Quotation quotation = response.getEntity();
            
            Set<QuotationItem> quotationitems = quotation.getQuotationItem();
            QuotationItem quotationItem = quotationitems.iterator().next();
            
            // store the quotationItem
            test_quotationItem = quotationItem;
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/items"+test_quotationItem.getId());
            ClientResponse test_response = test_request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/items"+test_quotationItem.getId());
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
	}
            
	@Test
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
	public void _3testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/quotations/current/items"+test_quotationItem.getId());
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<QuotationItem> response = request.get(QuotationItem.class);
            QuotationItem quotationItem = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}

	@Test
	public void _4testListAll() {
		//Assert.assertFalse(quotationitemendpoint.listAll(1, 1).isEmpty());
	}

	@Test
	public void _5testUpdate() {
		fail("Not yet implemented"); // TODO
	}
}
