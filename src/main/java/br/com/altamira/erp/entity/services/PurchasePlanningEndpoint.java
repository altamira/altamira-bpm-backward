package br.com.altamira.erp.entity.services;

import br.com.altamira.bpm.services.MailService;
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
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

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
    private PurchasePlanningDao planningDao;
    
    @Inject
    private MailService mailService;
    
    @Context
    private HttpServletRequest httpRequest;

    @POST
    @Path("/current")
    @Consumes("application/json")
    public Response create(PurchasePlanning entity) {
    	PurchasePlanning purchasePlanning;

    	List<PurchasePlanning> purchasePlannings = em
                .createNamedQuery("PurchasePlanning.getCurrent", PurchasePlanning.class)
                .getResultList();

        if (!purchasePlannings.isEmpty()) {

        	purchasePlanning = purchasePlannings.get(0);
        	purchasePlanning.setApproveDate(DateTime.now().toDate());
                purchasePlanning.setClosedDate(DateTime.now().toDate());
            
            em.merge(purchasePlanning);
            em.flush();
            
            // TO-DO send mail for notification for purchase planning approval
            String to = "alessandro.holanda@altamira.com.br";
            String cc = null;
            String bcc = null;
            String subject = "Approve Purchase Plan:"+purchasePlanning.getId();
            
            StringBuffer text = new StringBuffer();
            text.append("Please click on below link to approve Purchase Plan:\n")
                .append("http://localhost:8080/"+httpRequest.getContextPath()+"/forms/aprove-request.xhtml")
                .append("\n\n"+"Below is the link for Purchase Planning Report:\n")
                .append("http://localhost:8080/"+httpRequest.getContextPath()+"/rest/purchaseplannings/"+purchasePlanning.getId()+"/report");
            
            try {
                mailService.sendMail(to, cc, bcc, subject, text.toString(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            

            /*
            Map<String, Object> variables = new HashMap<String, Object>();

            variables.put("requestId", request.getId());

            runtimeService.startProcessInstanceByKey("SteelRawMaterialPurchasingRequest", variables);
            */
            
        }

        return getCurrent();
    }

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
    @Path("/current")
    @Consumes("application/json")
    public Response update(PurchasePlanning entity) {
        entity = em.merge(entity);
        return Response.ok(UriBuilder.fromResource(PurchasePlanningEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }

    @GET
    @Path("/current")
    @Produces("application/json")
    public Response getCurrent() {
        PurchasePlanning entity = planningDao.getCurrent();
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
            byte[] planningReportJasper = planningDao.getPlanningReportJasperFile();
            byte[] planningReportAltamiraimage = planningDao.getPlanningReportAltamiraImage();
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
                    planningDao.insertGeneratedPlanningReport(jasperPrint);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not insert generated report in database.");
            }
        }

    }
}
