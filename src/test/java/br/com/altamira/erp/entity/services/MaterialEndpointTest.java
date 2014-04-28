package br.com.altamira.erp.entity.services;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.altamira.erp.entity.model.Material;
import br.com.altamira.erp.entity.model.MaterialStandard;
import br.com.altamira.erp.entity.model.MaterialStandardPK;
import br.com.altamira.erp.entity.model.PurchaseOrder;
import br.com.altamira.erp.entity.model.PurchaseOrderItem;
import br.com.altamira.erp.entity.model.PurchasePlanning;
import br.com.altamira.erp.entity.model.PurchasePlanningItem;
import br.com.altamira.erp.entity.model.Quotation;
import br.com.altamira.erp.entity.model.QuotationItem;
import br.com.altamira.erp.entity.model.QuotationItemQuote;
import br.com.altamira.erp.entity.model.QuotationRequest;
import br.com.altamira.erp.entity.model.Request;
import br.com.altamira.erp.entity.model.RequestItem;
import br.com.altamira.erp.entity.model.Standard;
import br.com.altamira.erp.entity.model.Supplier;
import br.com.altamira.erp.entity.model.SupplierContact;
import br.com.altamira.erp.entity.model.SupplierInStock;
import br.com.altamira.erp.entity.model.SupplierPriceList;
import br.com.altamira.erp.entity.model.SupplierStandard;
import br.com.altamira.erp.entity.model.SupplierStandardPK;
import br.com.altamira.erp.entity.model.UserPreference;

@Stateless
@RunWith(Arquillian.class)
public class MaterialEndpointTest {

    @ArquillianResource  
    InitialContext ctx;
    
	@Inject
	private MaterialEndpoint materialendpoint;
	
    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;
    
    @Resource
    private UserTransaction utx;
	
	public static Archive<?> deploy() {  
        return ShrinkWrap.create(WebArchive.class)  
                .addAsLibraries(  
                        ShrinkWrap.create(JavaArchive.class, "materialendpointtest.jar")  
                            .addClasses(MaterialEndpoint.class, Material.class,
            						Quotation.class, QuotationItem.class, Request.class,
            						RequestItem.class, Supplier.class,
            						MaterialStandard.class, PurchaseOrder.class,
            						PurchaseOrderItem.class, PurchasePlanning.class,
            						PurchasePlanningItem.class, Quotation.class,
            						QuotationItem.class, QuotationItemQuote.class,
            						QuotationRequest.class, Standard.class,
            						SupplierContact.class, SupplierInStock.class,
            						SupplierPriceList.class, SupplierStandard.class,
            						UserPreference.class, SupplierStandardPK.class,
            						MaterialStandardPK.class, br.com.altamira.bpm.AltamiraCustomDialect.class))
    						.addAsManifestResource("META-INF/persistence.xml",
    								"persistence.xml")
    						.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml"); 
	}
	
	@OverProtocol("Servlet 3.0") 
	@Deployment  
	public static JavaArchive createDeployment() {
		return ShrinkWrap
				.create(JavaArchive.class, "altamira-bpm.jar")
				.addClasses(MaterialEndpoint.class, Material.class,
						Quotation.class, QuotationItem.class, Request.class,
						RequestItem.class, Supplier.class,
						MaterialStandard.class, PurchaseOrder.class,
						PurchaseOrderItem.class, PurchasePlanning.class,
						PurchasePlanningItem.class, Quotation.class,
						QuotationItem.class, QuotationItemQuote.class,
						QuotationRequest.class, Standard.class,
						SupplierContact.class, SupplierInStock.class,
						SupplierPriceList.class, SupplierStandard.class,
						UserPreference.class, SupplierStandardPK.class,
						MaterialStandardPK.class, br.com.altamira.bpm.AltamiraCustomDialect.class)
				.addAsManifestResource("META-INF/persistence.xml",
						"persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
				//.addAsResource("log4j.xml");
	}

	@Test
	@InSequence(1)
	public void should_be_deployed() {
		Assert.assertNotNull(materialendpoint);
	}

	@Test
	@InSequence(2)
	public void testCreate() throws Exception {
		
		Material material = new Material();
		
		material.setLamination("XX");
		material.setLength(new BigDecimal("1.5"));
		//material.setTax(new BigDecimal(3.4));
		material.setThickness(new BigDecimal("9.8"));
		material.setTreatment("XX");
		material.setWidth(new BigDecimal("9.9"));
		material.setTax(new BigDecimal("0.0"));
		
		List<Material> materials = em.createNamedQuery("Material.findUnique", Material.class)
				.setParameter("lamination", material.getLamination())
                .setParameter("treatment", material.getTreatment())
                .setParameter("thickness", material.getThickness())
                .setParameter("width", material.getWidth())
                .setParameter("length", material.getLength()).getResultList();
		
		//Assert.assertFalse(materials.isEmpty());
		
		//Assert.assertNotNull(em.find(Material.class, 246l));
		
		/*if (!materials.isEmpty()) {
			fail("Not is empty");
			return;
		}*/

		if (!materials.isEmpty()) {
                        utx.begin();
			em.remove(em.merge(materials.get(0)));
			em.flush();
                        utx.commit();
		}
		
		Response r = materialendpoint.create(material);
		
		List<Material> checkExist = em.createNamedQuery("Material.findUnique", Material.class)
				              .setParameter("lamination", material.getLamination())
                                              .setParameter("treatment", material.getTreatment())
                                              .setParameter("thickness", material.getThickness())
                                              .setParameter("width", material.getWidth())
                                              .setParameter("length", material.getLength())
                                              .getResultList();
		
		assertFalse(checkExist.isEmpty());
		assertNotNull(em.find(Material.class, material.getId()));
		
		Assert.assertNotEquals((Long) 0l, material.getId());
		Assert.assertNotNull(material.getId());
		Assert.assertEquals(Status.CREATED.getStatusCode(), r.getStatus());
		
		//em.remove(material);
		//em.flush();
		
	}

	@Test
	@InSequence(3)
	public void testFindById() {
		
		TypedQuery<Material> findAllQuery = em.createNamedQuery("Material.findAll", Material.class);
		findAllQuery.setFirstResult(1);
		findAllQuery.setMaxResults(1);
		final List<Material> results = findAllQuery.getResultList();
		
		if (results.isEmpty()) {
			fail("Table have no records to test find by id");
		} else {
			Response r = materialendpoint.findById(results.get(0).getId());
		
			Assert.assertEquals(Status.OK.getStatusCode(), r.getStatus());
		}
	}

	@Test
	@InSequence(4)
	public void testListAll() {
		Assert.assertFalse(materialendpoint.listAll(1, 1).isEmpty());
	}

	@Test
	@InSequence(5)
	public void testUpdate() {
		
		TypedQuery<Material> findAllQuery = em.createNamedQuery("Material.findAll", Material.class);
		findAllQuery.setFirstResult(1);
		findAllQuery.setMaxResults(1);
		final List<Material> results = findAllQuery.getResultList();
		
		if (results.isEmpty()) {
			fail("Table have no records to test find by id");
		} else {
			Response r = materialendpoint.update(results.get(0));
		
			Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), r.getStatus());
		}
	}

	@Test
	@InSequence(6)
	public void testDeleteById() throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		
		Material material = new Material();
		
		//material.setId(0l);
		material.setLamination("XX");
		material.setLength(new BigDecimal("1.5"));
		//material.setTax(new BigDecimal(3.4));
		material.setThickness(new BigDecimal("9.8"));
		material.setTreatment("XY");
		material.setWidth(new BigDecimal("9.9"));
		material.setTax(new BigDecimal("0.0"));
		
                utx.begin();
		em.persist(material);
		utx.commit();
                
		Response r = materialendpoint.deleteById(material.getId());
                
		Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), r.getStatus());

		Assert.assertNull(em.find(Material.class, material.getId()));

	}

}
