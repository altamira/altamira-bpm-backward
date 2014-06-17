package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.Standard;
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
import org.junit.runners.MethodSorters;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StandardEndpointTest {

    static Long standardId;
    
	@Test
	public void _1testCreate() throws Exception {
            
            // prepare test data
            Standard standard = new Standard();
            standard.setName("Test");
            standard.setDescription("Test Standard");
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/standards");
            request.accept(MediaType.APPLICATION_JSON);
            request.body(MediaType.APPLICATION_JSON, standard);
            
            ClientResponse<Standard> response = request.post(Standard.class);
            Standard standardCrtd = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            Assert.assertNotNull(standardCrtd.getId());
            
            // store standard id
            standardId = standardCrtd.getId();
	}

	@Test
	public void _2testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/standards/"+standardId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Standard> response = request.get(Standard.class);
            Standard standard = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertEquals(standard.getId(), standardId);
	}

	@Test
	public void _3testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/standards?start=1&max=10");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<Standard> standards = (List<Standard>) response.getEntity(new GenericType<List<Standard>>() {
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(standards);
	}

	@Test
	public void _4testUpdate() throws Exception {
            
            // Get the Standard
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/standards/"+standardId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<Standard> response = request.get(Standard.class);
            Standard standard = response.getEntity();
            
            // prepare test data
            standard.setName("Test UPDT");
            standard.setDescription("Test Standard Updated");
            
            // Do the tests
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/standards/"+standardId);
            test_request.accept(MediaType.APPLICATION_JSON);
            test_request.header("Content-Type", MediaType.APPLICATION_JSON);
            test_request.body(MediaType.APPLICATION_JSON, standard);
            
            ClientResponse<Standard> test_response = test_request.put(Standard.class);
            Standard standardUpdt = test_response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
            Assert.assertEquals(standardUpdt.getName(), "Test UPDT");
            Assert.assertEquals(standardUpdt.getDescription(), "Test Standard Updated");
	}
        
        @Test
	public void _5testDeleteById() throws Exception {
            
            // Do the tests
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/standards/"+standardId);
            ClientResponse test_response = test_request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/standards/"+standardId);
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
	}
}
