package br.com.altamira.erp.entity.services;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.altamira.erp.entity.model.Quotation;
import br.com.altamira.erp.entity.model.QuotationItem;
import br.com.altamira.erp.entity.model.QuotationItemQuote;
import java.math.BigDecimal;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.map.ObjectMapper;
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
public class QuotationItemQuoteEndpointTest {

    static QuotationItemQuote test_quotationItemQuote;
    static Long test_quotationId;
    static Long test_quotationItemId;
    static Long newQuotationItemQuote;
    
        @Test
        //@Ignore
	public void _1testDeleteById() throws Exception {
            
            // get the current quotation
            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target("http://localhost:8080/altamira-bpm/rest/quotations/current");
            Response response = target.request().get();
            
            Object responseObj = response.readEntity(Object.class);
            Quotation quotation = new ObjectMapper().convertValue(responseObj, Quotation.class);
            
            // Get the quotation item quote
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
                    test_quotationId = quotation.getId();
                    test_quotationItemId = quotationItem.getId();
                    
                    break;
                }
            }
            
            // store the quotation item quote
            test_quotationItemQuote = quotationItemQuote;
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/quotations/"+test_quotationId
                                                                                                    +"/items/"+test_quotationItemId
                                                                                                    +"/quotes/"+test_quotationItemQuote.getId());
            ClientResponse test_response = test_request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/quotations/"+test_quotationId
                                                                                                     +"/items/"+test_quotationItemId
                                                                                                     +"/quotes/"+test_quotationItemQuote.getId());
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
        }
    
        @Test
        //@Ignore
	public void _2testCreate() throws Exception {
            
            // prepare test data
            test_quotationItemQuote.setId(null);
            
            // Do the tests
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/quotations/"+test_quotationId
                                                                                               +"/items/"+test_quotationItemId
                                                                                               +"/quotes");
            request.accept(MediaType.APPLICATION_JSON);
            request.body(MediaType.APPLICATION_JSON, test_quotationItemQuote);
            
            ClientResponse<QuotationItemQuote> response = request.post(QuotationItemQuote.class);
            QuotationItemQuote quotationItemQuote = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(quotationItemQuote.getId());
            
            // store new quotation item quote id
            newQuotationItemQuote = quotationItemQuote.getId();
	}

	@Test
        //@Ignore
	public void _3testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/quotations/"+test_quotationId
                                                                                               +"/items/"+test_quotationItemId
                                                                                               +"/quotes/"+newQuotationItemQuote);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<QuotationItemQuote> response = request.get(QuotationItemQuote.class);
            QuotationItemQuote quotationItemQuote = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertEquals(quotationItemQuote.getId(), newQuotationItemQuote);
	}

	@Test
        //@Ignore
	public void _4testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/quotations/"+test_quotationId
                                                                                               +"/items/"+test_quotationItemId
                                                                                               +"/quotes");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<QuotationItemQuote> quotationItemQuotes = (List<QuotationItemQuote>) response.getEntity(new GenericType<List<QuotationItemQuote>>(){
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(quotationItemQuotes);
        }

	@Test
        //@Ignore
	public void _5testUpdate() throws Exception {
            
            // Get the Quotation Item Quote
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/quotations/"+test_quotationId
                                                                                               +"/items/"+test_quotationItemId
                                                                                               +"/quotes/"+newQuotationItemQuote);
            request.accept(MediaType.APPLICATION_JSON);
            ClientResponse<QuotationItemQuote> response = request.get(QuotationItemQuote.class);
            QuotationItemQuote quotationItemQuote = response.getEntity();
            
            // prepare test data
            quotationItemQuote.setWeight(new BigDecimal("20"));
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest"+"/quotations/"+test_quotationId
                                                                                                    +"/items/"+test_quotationItemId
                                                                                                    +"/quotes/"+newQuotationItemQuote);
            test_request.accept(MediaType.APPLICATION_JSON);
            test_request.header("Content-Type", MediaType.APPLICATION_JSON);
            test_request.body(MediaType.APPLICATION_JSON, quotationItemQuote);
            
            ClientResponse<QuotationItemQuote> test_response = test_request.put(QuotationItemQuote.class);
            QuotationItemQuote quotationItemQuoteUpdt = test_response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
            Assert.assertEquals(quotationItemQuoteUpdt.getWeight(), new BigDecimal("20"));
        }
}
