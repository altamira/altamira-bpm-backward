package br.com.altamira.erp.entity.services;

import static org.junit.Assert.fail;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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

@RunWith(Arquillian.class)
public class MaterialEndpointTest {

	@Inject
	private MaterialEndpoint materialendpoint;

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
						MaterialStandardPK.class)
				.addAsManifestResource("META-INF/persistence.xml",
						"persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void should_be_deployed() {
		Assert.assertNotNull(materialendpoint);
	}

	@PersistenceContext(unitName = "altamira-bpm-PU")
	private EntityManager em;

	@Inject
	private Material material;

	@Test
	public void testCreate() {

		material.setLamination("TT");
		material.setLength(new BigDecimal(1.5));
		material.setTax(new BigDecimal(3.4));
		material.setThickness(new BigDecimal(9.8));
		material.setTreatment("TT");
		material.setWidth(new BigDecimal(9.9));

		Assert.assertEquals(null, material.getId());
		materialendpoint.create(material);
		Assert.assertNotEquals((Long) 0l, material.getId());

		Material m = em.find(Material.class, material.getId());

		Assert.assertEquals(m.getLamination(), material.getLamination());

		materialendpoint.deleteById(material.getId());
	}

	@Test
	public void testDeleteById() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testFindById() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testListAll() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented"); // TODO
	}

}
