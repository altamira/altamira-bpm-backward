package br.com.altamira.erp.entity.services;

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

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

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
        return ShrinkWrap
                .create(WebArchive.class)
                .addAsLibraries(
                        ShrinkWrap
                        .create(JavaArchive.class,
                                "materialendpointtest.jar")
                        .addClasses(
                                MaterialEndpoint.class,
                                Material.class,
                                Quotation.class,
                                QuotationItem.class,
                                Request.class,
                                RequestItem.class,
                                Supplier.class,
                                MaterialStandard.class,
                                PurchaseOrder.class,
                                PurchaseOrderItem.class,
                                PurchasePlanning.class,
                                PurchasePlanningItem.class,
                                Quotation.class,
                                QuotationItem.class,
                                QuotationItemQuote.class,
                                QuotationRequest.class,
                                Standard.class,
                                SupplierContact.class,
                                SupplierInStock.class,
                                SupplierPriceList.class,
                                SupplierStandard.class,
                                UserPreference.class,
                                SupplierStandardPK.class,
                                MaterialStandardPK.class,
                                br.com.altamira.bpm.AltamiraCustomDialect.class))
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
                        MaterialStandardPK.class,
                        br.com.altamira.bpm.AltamiraCustomDialect.class)
                .addAsManifestResource("META-INF/persistence.xml",
                        "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void should_be_deployed() {
        Assert.assertNotNull(materialendpoint);
    }

    @Test
    public void testCreate() throws Exception {

        Material material = new Material();

        material.setId(null);
        material.setLamination("XX");
        material.setLength(new BigDecimal("1.5"));
        material.setTax(new BigDecimal(3.4));
        material.setThickness(new BigDecimal("9.8"));
        material.setTreatment("XX");
        material.setWidth(new BigDecimal("9.9"));

        // Check if exists
        List<Material> materials = em
                .createNamedQuery("Material.findUnique", Material.class)
                .setParameter("lamination", material.getLamination())
                .setParameter("treatment", material.getTreatment())
                .setParameter("thickness", material.getThickness())
                .setParameter("width", material.getWidth())
                .setParameter("length", material.getLength()).getResultList();

        // If exists, drop
        if (!materials.isEmpty()) {
            utx.begin();
            em.remove(em.merge(materials.get(0)));
            em.flush();
            utx.commit();
        }

        // Do the test
        Response r = materialendpoint.create(material);

        // Check the results
        Assert.assertEquals(Status.CREATED.getStatusCode(), r.getStatus());
        Assert.assertNotNull(material.getId());
        Assert.assertNotEquals((Long) 0l, material.getId());
        assertNotNull(em.find(Material.class, material.getId()));

        List<Material> checkExist = em
                .createNamedQuery("Material.findUnique", Material.class)
                .setParameter("lamination", material.getLamination())
                .setParameter("treatment", material.getTreatment())
                .setParameter("thickness", material.getThickness())
                .setParameter("width", material.getWidth())
                .setParameter("length", material.getLength()).getResultList();

        assertFalse(checkExist.isEmpty());
        assertEquals(material, checkExist.get(0));
        assertEquals(material.getId(), checkExist.get(0).getId());

        // Clean up test data
        utx.begin();
        em.remove(em.merge(material));
        em.flush();
        utx.commit();

    }

    @Test
    public void testFindById() throws Exception {

        Material material = new Material();

        material.setId(null);
        material.setLamination("XX");
        material.setLength(new BigDecimal("1.5"));
        material.setTax(new BigDecimal(3.4));
        material.setThickness(new BigDecimal("9.8"));
        material.setTreatment("XX");
        material.setWidth(new BigDecimal("9.9"));

        // Check if exists
        List<Material> materials = em
                .createNamedQuery("Material.findUnique", Material.class)
                .setParameter("lamination", material.getLamination())
                .setParameter("treatment", material.getTreatment())
                .setParameter("thickness", material.getThickness())
                .setParameter("width", material.getWidth())
                .setParameter("length", material.getLength()).getResultList();

        // If not exists, create
        if (materials.isEmpty()) {
            utx.begin();
            em.persist(material);
            em.flush();
            utx.commit();
        } else {
            material = materials.get(0);
        }

        // Do the test
        Response r = materialendpoint.findById(material.getId());

        // Check the results
        Assert.assertEquals(Status.OK.getStatusCode(), r.getStatus());

        // TODO Compare entity from HTTP response with this one get from database
        //Material materialFound = r.readEntity(Material.class);
        //assertEquals(material, materialFound);
        // Clean up test data
        utx.begin();
        em.remove(em.merge(material));
        em.flush();
        utx.commit();
    }

    @Test
    public void testListAll() throws Exception {

        Material material = null;

        TypedQuery<Material> findAllQuery = em.createNamedQuery("Material.findAll", Material.class);
        findAllQuery.setFirstResult(1);
        findAllQuery.setMaxResults(1);

        if (findAllQuery.getResultList().isEmpty()) {

            material = new Material();

            material.setId(null);
            material.setLamination("XX");
            material.setLength(new BigDecimal("1.5"));
            material.setTax(new BigDecimal(3.4));
            material.setThickness(new BigDecimal("9.8"));
            material.setTreatment("XX");
            material.setWidth(new BigDecimal("9.9"));

            em.persist(material);

        }

        // Do the test
        Assert.assertFalse(materialendpoint.listAll(1, 1).isEmpty());

        // Clean up test data
        if (material != null) {
            utx.begin();
            em.remove(em.merge(material));
            em.flush();
            utx.commit();
        }
    }

    @Test
    public void testUpdate() throws Exception {

        Material materialToChange = new Material();

        materialToChange.setId(null);
        materialToChange.setLamination("XX");
        materialToChange.setLength(new BigDecimal("1.5"));
        materialToChange.setTax(new BigDecimal(3.4));
        materialToChange.setThickness(new BigDecimal("9.8"));
        materialToChange.setTreatment("XX");
        materialToChange.setWidth(new BigDecimal("9.9"));

        // Check if exists
        List<Material> materials = em
                .createNamedQuery("Material.findUnique", Material.class)
                .setParameter("lamination", materialToChange.getLamination())
                .setParameter("treatment", materialToChange.getTreatment())
                .setParameter("thickness", materialToChange.getThickness())
                .setParameter("width", materialToChange.getWidth())
                .setParameter("length", materialToChange.getLength()).getResultList();

        // If not, create
        if (materials.isEmpty()) {
            utx.begin();
            em.persist(materialToChange);
            em.flush();
            utx.commit();
        } else {
            materialToChange = materials.get(0);
        }

        Material materialChanged = new Material();

        materialChanged.setId(null);
        materialChanged.setLamination("UX");
        materialChanged.setLength(new BigDecimal("2.2"));
        materialChanged.setTax(new BigDecimal(4.5));
        materialChanged.setThickness(new BigDecimal("6.7"));
        materialChanged.setTreatment("UX");
        materialChanged.setWidth(new BigDecimal("1.5"));

        // Check if the changes already exists
        materials = em.createNamedQuery("Material.findUnique", Material.class)
                .setParameter("lamination", materialChanged.getLamination())
                .setParameter("treatment", materialChanged.getTreatment())
                .setParameter("thickness", materialChanged.getThickness())
                .setParameter("width", materialChanged.getWidth())
                .setParameter("length", materialChanged.getLength()).getResultList();

        // If exists, drop
        if (!materials.isEmpty()) {
            utx.begin();
            em.remove(em.merge(materials.get(0)));
            em.flush();
            utx.commit();
        }

        // Prepare to update
        Material materialUpdate = em.find(Material.class, materialToChange.getId());

        assertNotNull(materialUpdate);

        materialUpdate.setLamination(materialChanged.getLamination());
        materialUpdate.setLength(materialChanged.getLength());
        materialUpdate.setTax(materialChanged.getTax());
        materialUpdate.setThickness(materialChanged.getThickness());
        materialUpdate.setTreatment(materialChanged.getTreatment());
        materialUpdate.setWidth(materialChanged.getWidth());

        // Do the update test
        Response r = materialendpoint.update(materialUpdate);

        // Check results
        Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), r.getStatus());

        assertEquals(materialUpdate, em.find(Material.class, materialToChange.getId()));

        if (materialUpdate.getId() != null) {
            utx.begin();
            em.remove(em.merge(materialToChange));
            em.flush();
            utx.commit();
        }
    }

    @Test
    public void testDeleteById() throws Exception {

        Material material = new Material();

        material.setId(null);
        material.setLamination("XX");
        material.setLength(new BigDecimal("1.5"));
        material.setTax(new BigDecimal(3.4));
        material.setThickness(new BigDecimal("9.8"));
        material.setTreatment("XX");
        material.setWidth(new BigDecimal("9.9"));

        // Check if exists
        List<Material> materials = em
                .createNamedQuery("Material.findUnique", Material.class)
                .setParameter("lamination", material.getLamination())
                .setParameter("treatment", material.getTreatment())
                .setParameter("thickness", material.getThickness())
                .setParameter("width", material.getWidth())
                .setParameter("length", material.getLength()).getResultList();

        // If not, create
        if (materials.isEmpty()) {
            utx.begin();
            em.persist(material);
            utx.commit();
        } else {
            material = materials.get(0);
        }

        // Do the delete test
        Response r = materialendpoint.deleteById(material.getId());

        // Check the results
        Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), r.getStatus());

        Assert.assertNull(em.find(Material.class, material.getId()));

        // Clean code (in case of failure)
        utx.begin();
        em.remove(em.merge(material));
        em.flush();
        utx.commit();

    }

    @Test
    public void testcreateService() {
        Material material = new Material();

        material.setId(null);
        material.setLamination("XX");
        material.setLength(new BigDecimal("1.5"));
        material.setTax(new BigDecimal(3.4));
        material.setThickness(new BigDecimal("9.8"));
        material.setTreatment("XX");
        material.setWidth(new BigDecimal("9.9"));

//        ClientFactory.newClient();
//        Client client = Client.create(config);
//        WebResource service = client.resource(getBaseURI());
//        
//        Response response = target.request().post(Entity.entity(material, "application/json"));
//        //Read output in string format
//        Assert.assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
//        response.close();
    }

}
