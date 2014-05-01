package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.dao.PurchasePlanningDao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import br.com.altamira.erp.entity.model.PurchaseOrderItem;
import br.com.altamira.erp.entity.model.PurchasePlanning;
import br.com.altamira.erp.entity.model.Quotation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.joda.time.DateTime;

/**
 *
 */
@Stateless
@Path("/purchaseplannings")
public class PurchasePlanningEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @Inject
    private PurchasePlanningDao purchasePlanningDao;

    /*@POST
    @Path("/current")
    @Consumes("application/json")
    public Response create(PurchasePlanning entity) {
        entity.setId(null);
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(PurchasePlanning.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }*/

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        PurchasePlanning entity = em.find(PurchasePlanning.class, id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        em.remove(entity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("id") long id) {
        TypedQuery<PurchasePlanning> findByIdQuery = em.createNamedQuery("PurchasePlanning.findById", PurchasePlanning.class);
        findByIdQuery.setParameter("id", id);
        PurchasePlanning entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            entity = null;
        }
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @GET
    @Produces("application/json")
    public List<PurchasePlanning> listAll(
            @QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<PurchasePlanning> findAllQuery = em.createNamedQuery("PurchasePlanning.findAll", PurchasePlanning.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<PurchasePlanning> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") long id/*, PurchasePlanning entity*/) {
    	
    	PurchasePlanning purchasePlanning = purchasePlanningDao.getCurrent();
    	
    	purchasePlanning.setApproveDate(DateTime.now().toDate());
        
    	PurchasePlanning entity = em.merge(purchasePlanning);
        em.flush();

        /*
        Map<String, Object> variables = new HashMap<String, Object>();

        variables.put("requestId", request.getId());

        runtimeService.startProcessInstanceByKey("SteelRawMaterialPurchasingRequest", variables);
        */

        return Response.ok(UriBuilder.fromResource(PurchasePlanningEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }

    @GET
    @Path("/current")
    @Produces("application/json")
    public Response getCurrent() {
        PurchasePlanning entity = purchasePlanningDao.getCurrent();
        return Response.ok(UriBuilder.fromResource(PurchasePlanningEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }

    @GET
    @Path("{id:[0-9][0-9]*}/report")
    @Produces("application/pdf")
    public Response getPlanningReportInPdf(@PathParam("id") long planningId) {

        // generate report
        JasperPrint jasperPrint = null;

        try {
            byte[] planningReportJasper = purchasePlanningDao.getPlanningReportJasperFile();
            byte[] planningReportAltamiraimage = purchasePlanningDao.getPlanningReportAltamiraImage();
            byte[] pdf = null;

            final ByteArrayInputStream reportStream = new ByteArrayInputStream(planningReportJasper);
            final Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("PLANNING_ID", new BigDecimal(planningId));

            Locale locale = new Locale.Builder().setLanguage("pt").setRegion("BR").build();
            parameters.put("REPORT_LOCALE", locale);

            BufferedImage imfg = null;
            try {
                InputStream in = new ByteArrayInputStream(planningReportAltamiraimage);
                imfg = ImageIO.read(in);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            parameters.put("altamira_logo", imfg);
            parameters.put("USERNAME", "Parth");

            Session session = em.unwrap(Session.class);

            jasperPrint = session.doReturningWork(new ReturningWork<JasperPrint>() {
                @Override
                public JasperPrint execute(Connection connection) {
                    JasperPrint jasperPrint = null;

                    try {
                        jasperPrint = JasperFillManager.fillReport(reportStream, parameters, connection);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return jasperPrint;
                }
            });

            pdf = JasperExportManager.exportReportToPdf(jasperPrint);

            ByteArrayInputStream pdfStream = new ByteArrayInputStream(pdf);

            Response.ResponseBuilder response = Response.ok(pdfStream);
            response.header("Content-Disposition", "inline; filename=Planning Report.pdf");

            return response.build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (jasperPrint != null) {
                    // store generated report in database
                	purchasePlanningDao.insertGeneratedPlanningReport(jasperPrint);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not insert generated report in database.");
            }
        }

    }
}
