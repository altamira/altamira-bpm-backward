/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.Material;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.DriverManager;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author PARTH
 */

@RunWith(Arquillian.class)
public class MaterialEndpointTest 
{
    
    private static URI getBaseURI() 
    {
        return UriBuilder.fromUri("http://localhost/").port(8080).build();
    }
    
    @Test
    public void testCreate() throws Exception
    {   
        Material material = new Material();

        material.setId(null);
        material.setLamination("XX");
        material.setLength(new BigDecimal("1.5"));
        material.setTax(new BigDecimal(3.4));
        material.setThickness(new BigDecimal("9.8"));
        material.setTreatment("XX");
        material.setWidth(new BigDecimal("9.9"));
        
        // TO-DO Check if exists
        
        // TO-DO If exists, drop
        
        // Do the test
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/materials");
        request.accept(MediaType.APPLICATION_JSON);
        request.body(MediaType.APPLICATION_JSON, material);
        
        ClientResponse<Material> response = request.post(Material.class);
        Material materialResponse = response.getEntity();
        
        // Check the results
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Assert.assertNotNull(materialResponse.getId());
        Assert.assertNotEquals((Long) 0l, materialResponse.getId());
        
        // Clean up test data
        
    }
    
    @Test
    public void testFindById() throws Exception
    {
        Material material = new Material();

        material.setId(null);
        material.setLamination("XX");
        material.setLength(new BigDecimal("1.5"));
        material.setTax(new BigDecimal(3.4));
        material.setThickness(new BigDecimal("9.8"));
        material.setTreatment("XX");
        material.setWidth(new BigDecimal("9.9"));
        
        ClientRequest req = new ClientRequest("http://localhost:8080/altamira-bpm/rest/materials");
        req.accept(MediaType.APPLICATION_JSON);
        req.body(MediaType.APPLICATION_JSON, material);
        
        ClientResponse<Material> res = req.post(Material.class);
        Material mat = res.getEntity();
        
        // Do the test
        Long materialId = mat.getId();
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/materials");
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse<Material> response = request.get(Material.class);
        Material materialResponse = response.getEntity();
        
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(mat.getId(), materialResponse.getId());
        
    }
    
    @Test
    public void testListAll() throws Exception
    {
        ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/materials");
        request.accept(MediaType.APPLICATION_JSON);
        
        ClientResponse response = request.get(ClientResponse.class);
        List<Material> materials = (List<Material>) response.getEntity(new GenericType<List<Material>>() {
                    });
        
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertFalse(materials.isEmpty());
        
    }
    
    @Test
    public void testUpdate() throws Exception
    {
        fail(); // yet to develop
    }
    
    @Test
    public void testDelete() throws Exception
    {
        fail(); // yet to develop
    }
}
