package br.com.altamira.erp.entity.services;

import java.math.BigDecimal;

import br.com.altamira.erp.entity.model.*;
import br.com.altamira.erp.entity.services.MaterialEndpoint;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

@RunWith(Arquillian.class)
public class MaterialEndpointTest
{

   @Inject
   private MaterialEndpoint materialendpoint;

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "altamira-bpm.jar")
            .addClasses(MaterialEndpoint.class, 
            		Material.class, Quotation.class, QuotationItem.class, 
            		Request.class, RequestItem.class, Supplier.class,
            		MaterialStandard.class, PurchaseOrder.class, PurchaseOrderItem.class,
            		PurchasePlanning.class, PurchasePlanningItem.class, Quotation.class, QuotationItem.class,
            		QuotationItemQuote.class, QuotationRequest.class, Standard.class,
            		SupplierContact.class, SupplierInStock.class, SupplierPriceList.class, SupplierStandard.class,
            		UserPreference.class, SupplierStandardPK.class, MaterialStandardPK.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Test
   public void should_be_deployed()
   {
      Assert.assertNotNull(materialendpoint);
   }
   
   @Test
   public void CreateMaterialTest() {
	   Material m = new Material();
	   
	   m.setLamination("TT");
	   m.setLength(new BigDecimal(1.5));
	   m.setTax(new BigDecimal(3.4));
	   m.setThickness(new BigDecimal(9.8));
	   m.setTreatment("TT");
	   m.setWidth(new BigDecimal(9.9));
	   
	   Assert.assertEquals(null, m.getId());
	   materialendpoint.create(m);
	   Assert.assertNotEquals((Long) 0l, m.getId());
   }
}
