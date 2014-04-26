package br.com.altamira.erp.entity.services;

import static org.junit.Assert.fail;

import javax.inject.Inject;

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
public class PurchaseOrderItemEndpointTest {

	@Inject
	private PurchaseOrderItemEndpoint purchaseorderitemendpoint;
	
	@Inject
	private PurchaseOrderItem purchaseOrderItem;

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap
				.create(JavaArchive.class, "altamira-bpm.jar")
				.addClasses(PurchaseOrderItemEndpoint.class, Material.class,
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
	}

	@Test
	public void should_be_deployed() {
		Assert.assertNotNull(purchaseorderitemendpoint);
	}

	@Test
	public void testCreate() {
		fail("Not yet implemented"); // TODO
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
		Assert.assertFalse(purchaseorderitemendpoint.listAll(1, 1).isEmpty());
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented"); // TODO
	}
}
