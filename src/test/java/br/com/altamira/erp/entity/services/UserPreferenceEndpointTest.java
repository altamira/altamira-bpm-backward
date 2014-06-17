package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.UserPreference;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.junit.Assert.fail;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserPreferenceEndpointTest {

        static String userName;
    
	@Test
	public void _1testCreate() throws Exception {
            
            // Prepare test data
            UserPreference userPreference = new UserPreference();
            userPreference.setName("Test");
            userPreference.setPreferences("Test preferences");
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/userpreferences");
            request.accept(MediaType.APPLICATION_JSON);
            request.body(MediaType.APPLICATION_JSON, userPreference);
            
            ClientResponse<UserPreference> response = request.post(UserPreference.class);
            UserPreference userPreferenceCrtd = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            Assert.assertNotNull(userPreferenceCrtd.getName());
            
            // store user preference
            userName = userPreferenceCrtd.getName();
	}

	@Test
	public void _2testFindByName() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/userpreferences/"+userName);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<UserPreference> response = request.get(UserPreference.class);
            UserPreference userPreference = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertEquals(userPreference.getName(), userName);
	}

	@Test
	public void _3testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/userpreferences?start=1&max=10");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<UserPreference> userPreferences = (List<UserPreference>) response.getEntity(new GenericType<List<UserPreference>>() {
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(userPreferences);
	}

	@Test
	public void _4testUpdate() throws Exception {
            
            // Get User Preference
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/userpreferences/"+userName);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<UserPreference> response = request.get(UserPreference.class);
            UserPreference userPreference = response.getEntity();
            
            // Prepare test data
            userPreference.setPreferences("Test preferences updated");
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/userpreferences/"+userName);
            test_request.accept(MediaType.APPLICATION_JSON);
            test_request.header("Content-Type", MediaType.APPLICATION_JSON);
            test_request.body(MediaType.APPLICATION_JSON, userPreference);
            
            ClientResponse<UserPreference> test_response = test_request.put(UserPreference.class);
            UserPreference userPreferenceUpdt = test_response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
            Assert.assertEquals(userPreferenceUpdt.getPreferences(), "Test preferences updated");
	}
        
        @Test
	public void _5testDeleteById() throws Exception {
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/userpreferences/"+userName);
            ClientResponse test_response = test_request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/userpreferences/"+userName);
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
	}
}
