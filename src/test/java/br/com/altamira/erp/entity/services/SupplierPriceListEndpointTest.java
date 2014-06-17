package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.Material;
import br.com.altamira.erp.entity.model.Supplier;
import br.com.altamira.erp.entity.model.SupplierPriceList;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SupplierPriceListEndpointTest {
    
        static Material test_material;
        static Supplier test_supplier;
        static Long supplierPriceListId;

        @BeforeClass
        public static void setup() throws Exception {
            
            // create material
            Material material = new Material();

            material.setId(null);
            material.setLamination("XX");
            material.setLength(new BigDecimal("1.5"));
            material.setTax(new BigDecimal(3.4));
            material.setThickness(new BigDecimal("9.8"));
            material.setTreatment("XX");
            material.setWidth(new BigDecimal("9.9"));
            
            ClientRequest material_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/materials");
            material_request.accept(MediaType.APPLICATION_JSON);
            material_request.body(MediaType.APPLICATION_JSON, material);
            ClientResponse<Material> material_response = material_request.post(Material.class);
            Material materialCrtd = material_response.getEntity();
            
            // create supplier
            Supplier supplier = new Supplier();
            supplier.setName("TEST");
            
            ClientRequest supplier_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/suppliers");
            supplier_request.accept(MediaType.APPLICATION_JSON);
            supplier_request.body(MediaType.APPLICATION_JSON, supplier);
            ClientResponse<Supplier> supplier_response = supplier_request.post(Supplier.class);
            Supplier supplierCrtd = supplier_response.getEntity();
            
            // store material and supplier
            test_material = materialCrtd;
            test_supplier = supplierCrtd;
        }
    
	@Test
	public void _1testCreate() throws Exception {
            
            // Prepare test data
            SupplierPriceList supplierPriceList = new SupplierPriceList();
            supplierPriceList.setChangeDate(DateTime.now().toDate());
            supplierPriceList.setMaterial(test_material);
            supplierPriceList.setSupplier(test_supplier);
            supplierPriceList.setPrice(BigDecimal.ZERO);
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierpricelists");
            request.accept(MediaType.APPLICATION_JSON);
            request.body(MediaType.APPLICATION_JSON, supplierPriceList);
            
            ClientResponse<SupplierPriceList> response = request.post(SupplierPriceList.class);
            SupplierPriceList supplierPriceListCrtd = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            Assert.assertNotNull(supplierPriceListCrtd.getId());
            
            // store supplier pricelist id
            supplierPriceListId = supplierPriceListCrtd.getId();
	}

	@Test
	public void _2testFindById() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierpricelists/"+supplierPriceListId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<SupplierPriceList> response = request.get(SupplierPriceList.class);
            SupplierPriceList supplierPriceList = response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertEquals(supplierPriceList.getId(), supplierPriceListId);
	}

	@Test
	public void _3testListAll() throws Exception {
            
            // Do the test
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierpricelists?start=1&max=10");
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse response = request.get(ClientResponse.class);
            List<SupplierPriceList> supplierPriceLists = (List<SupplierPriceList>) response.getEntity(new GenericType<List<SupplierPriceList>>() {
                    });
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            Assert.assertNotNull(supplierPriceLists);
	}

	@Test
	public void _4testUpdate() throws Exception {
            
            // Get the supplier pricelist
            ClientRequest request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierpricelists/"+supplierPriceListId);
            request.accept(MediaType.APPLICATION_JSON);
            
            ClientResponse<SupplierPriceList> response = request.get(SupplierPriceList.class);
            SupplierPriceList supplierPriceList = response.getEntity();
            
            // Prepare test data
            supplierPriceList.setPrice(new BigDecimal("200"));
            supplierPriceList.setChangeDate(new SimpleDateFormat("dd/MM/yyyy").parse("21/12/2013"));
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierpricelists/"+supplierPriceListId);
            test_request.accept(MediaType.APPLICATION_JSON);
            test_request.header("Content-Type", MediaType.APPLICATION_JSON);
            test_request.body(MediaType.APPLICATION_JSON, supplierPriceList);
            
            ClientResponse<SupplierPriceList> test_response = test_request.put(SupplierPriceList.class);
            SupplierPriceList supplierPriceListUpdt = test_response.getEntity();
            
            // Check the results
            Assert.assertEquals(Response.Status.OK.getStatusCode(), test_response.getStatus());
            Assert.assertEquals(supplierPriceListUpdt.getChangeDate(), new SimpleDateFormat("dd/MM/yyyy").parse("21/12/2013"));
            Assert.assertEquals(supplierPriceListUpdt.getPrice(), new BigDecimal("200"));
	}
        
        @Test
	public void _5testDeleteById() throws Exception {
            
            // Do the test
            ClientRequest test_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierpricelists/"+supplierPriceListId);
            ClientResponse test_response = test_request.delete();
            
            // Check the results
            Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), test_response.getStatus());
            
            ClientRequest check_request = new ClientRequest("http://localhost:8080/altamira-bpm/rest/supplierpricelists/"+supplierPriceListId);
            check_request.accept(MediaType.APPLICATION_JSON);
            ClientResponse check_response = check_request.get();
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), check_response.getStatus());
	}
}
