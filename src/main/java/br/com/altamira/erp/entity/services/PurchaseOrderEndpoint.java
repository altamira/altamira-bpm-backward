package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.dao.OrderDao;

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

import br.com.altamira.erp.entity.model.Material;
import br.com.altamira.erp.entity.model.PurchaseOrder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
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
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;

import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

/**
 *
 */
@Stateless
@Path("/purchaseorders")
public class PurchaseOrderEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;
    
    @Inject
    OrderDao orderDao;
    
    @Inject
    private RuntimeService runtimeService;
    
    @Inject
    private TaskService taskService;
    
    @Context
    private HttpServletRequest httpRequest;

    @POST
    @Consumes("application/json")
    public Response create(PurchaseOrder entity) {
    	entity.setId(null);
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(PurchaseOrderEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        PurchaseOrder entity = em.find(PurchaseOrder.class, id);
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
        TypedQuery<PurchaseOrder> findByIdQuery = em.createNamedQuery("PurchaseOrder.findById", PurchaseOrder.class);
        findByIdQuery.setParameter("id", id);
        PurchaseOrder entity;
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
    public List<PurchaseOrder> listAll(
            @QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<PurchaseOrder> findAllQuery = em.createNamedQuery("PurchaseOrder.findAll", PurchaseOrder.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<PurchaseOrder> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") long id, PurchaseOrder entity) {
        entity = em.merge(entity);
        return Response.ok(UriBuilder.fromResource(PurchaseOrderEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }
    
    @PUT
    @Path("/checkOrders/{id:[0-9][0-9]*}")
    public Response checkOrders(@PathParam("id") long planningId)
    {
        // find relevant task
        List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals("planningId", planningId).list();
        
        if(!tasks.isEmpty())
        {
            // complete task
            Task task = tasks.get(0);
            String instanceId = task.getProcessInstanceId();
            taskService.complete(task.getId());
            
            return Response.ok().build();
        }
        else
        {
            return Response.status(Status.NOT_FOUND).build();
        }
        
    }
    
    @PUT
    @Path("/confirmOrders/{id:[0-9][0-9]*}")
    public Response confirmOrders(@PathParam("id") long planningId,
                                  @QueryParam("orderStatus") String orderStatus)
    {
        // find relevnt task
        List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals("planningId", planningId).list();
        
        if(!tasks.isEmpty())
        {
            // complete task
            Task task = tasks.get(0);
            String instanceId = task.getProcessInstanceId();
            runtimeService.setVariable(instanceId, "orderStatus", orderStatus);
            taskService.complete(task.getId());
            
            return Response.ok().build();
        }
        else
        {
            return Response.status(Status.NOT_FOUND).build();
        }
        
    }
    
    @GET
    @Path("{id}/report")
    @Produces("application/pdf")
    public Response getPurchaseOrderReportInPdf(@PathParam("id") long orderId) {
        // generate report
        JasperPrint jasperPrint = null;

        try {
            byte[] purchaseOrderReportJasper = orderDao.getPurchaseOrderReportJasperFile();
            byte[] purchaseOrderReportAltamiraimage = orderDao.getPurchaseOrderReportAltamiraImage();
            byte[] pdf = null;

            final ByteArrayInputStream reportStream = new ByteArrayInputStream(purchaseOrderReportJasper);
            final Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("PURCHASE_ORDER_ID", new BigDecimal(orderId));

            Date purchaseOrderDate = orderDao.getPurchaseOrderCreatedDateById(orderId);

            parameters.put("PURCHASE_ORDER_DATE", purchaseOrderDate);
            parameters.put("USERNAME", httpRequest.getUserPrincipal() == null ? "" : httpRequest.getUserPrincipal().getName());

            Locale locale = new Locale.Builder().setLanguage("pt").setRegion("BR").build();
            parameters.put("REPORT_LOCALE", locale);

            BufferedImage imfg = null;
            try {
                InputStream in = new ByteArrayInputStream(purchaseOrderReportAltamiraimage);
                imfg = ImageIO.read(in);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            parameters.put("altamira_logo", imfg);

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
            response.header("Content-Disposition","inline; filename=Planning Report.pdf");
            
            return response.build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (jasperPrint != null) {
                    // store generated report in database
                    orderDao.insertGeneratedPurchaseOrderReport(jasperPrint);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not insert generated report in database.");
            }
        }
    }
}
