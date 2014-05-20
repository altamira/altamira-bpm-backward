package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.dao.QuotationDao;
import br.com.altamira.erp.entity.dao.RequestDao;

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

import br.com.altamira.erp.entity.model.Quotation;
import br.com.altamira.erp.entity.model.QuotationRequest;
import br.com.altamira.erp.entity.model.Request;
import br.com.altamira.erp.entity.model.RequestReportData;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.camunda.bpm.engine.RuntimeService;
import org.joda.time.DateTime;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

/**
 *
 */
@Stateless
@Path("/requests")
public class RequestEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @Inject
    private RuntimeService runtimeService;
    
    @Inject
    private TaskService taskService;
    
    @Inject
    private RequestDao requestDao;
    
    @Inject
    private QuotationDao quotationDao;
    
    @Context
    private HttpServletRequest httpRequest;

    /*@POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response create(Request entity) {
    	entity.setId(null);
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(Request.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }*/

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        Request entity = em.find(Request.class, id);
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
        TypedQuery<Request> findByIdQuery = em.createNamedQuery("Request.findById", Request.class);
        findByIdQuery.setParameter("id", id);
        Request entity;
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
    public List<Request> listAll(@QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<Request> findAllQuery = em.createNamedQuery("Request.findAll", Request.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<Request> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(@PathParam("id") long id/*, Request entity*/) {
    	
    	Request request = requestDao.getCurrent();
    	
    	request.setSendDate(DateTime.now().toDate());
        
    	Request entity = em.merge(request);
        em.flush();
        
        // call CREATE_QUOTATION procedure
        Quotation quotation = quotationDao.getCurrent();
        
        // Check if quotation is found in any active quotation task?
        List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals("quotationId", quotation.getId()).list();
        
        if(!tasks.isEmpty())
        {
            // add requestId to the existing process instance
            Task task = tasks.get(0);
            String instanceId = task.getProcessInstanceId();
            
            List<Long> requestIdList = (List<Long>) runtimeService.getVariable(instanceId, "requestId");
            requestIdList.add(request.getId());
            runtimeService.setVariable(instanceId, "requestId", requestIdList);
        }
        else
        {
            // Start process instance
            List<Long> requestIdList = new ArrayList<Long>();
            requestIdList.add(request.getId());
            
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("requestId", requestIdList);
            
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("SteelRawMaterialPurchasingRequest", variables);
            String instanceId = processInstance.getProcessInstanceId();
            runtimeService.setVariable(instanceId, "quotationId", quotation.getId());
        }
        
        return Response.ok(UriBuilder.fromResource(RequestEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }

    @GET
    @Path("/current")
    @Produces("application/json")
    public Response getCurrent() {
    	Request entity = requestDao.getCurrent();
    	return Response.ok(UriBuilder.fromResource(RequestEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }
    
    @GET
    @Path("/{id:[0-9][0-9]*}/report")
    @Produces("application/pdf")
    public Response getRequestReportInPdf(@PathParam("id") long requestId) {

        // generate report
        JasperPrint jasperPrint = null;

        try {
            byte[] requestReportJasper = requestDao.getRequestReportJasperFile();
            byte[] requestReportAltamiraimage = requestDao.getRequestReportAltamiraImage();
            byte[] pdf = null;

            ByteArrayInputStream reportStream = new ByteArrayInputStream(requestReportJasper);
            Map<String, Object> parameters = new HashMap<String, Object>();

            List<Object[]> list = requestDao.selectRequestReportDataById(requestId);

            //Vector requestReportList = new Vector();
            ArrayList requestReportList = new ArrayList();
            List<Date> dateList = new ArrayList<Date>();

            BigDecimal lastMaterialId = new BigDecimal(0);
            int count = 0;
            BigDecimal sumRequestWeight = new BigDecimal(0);
            BigDecimal totalWeight = new BigDecimal(0);

            RequestReportData r = new RequestReportData();
            r.setId(null);
            r.setLamination(null);
            r.setLength(null);
            r.setThickness(null);
            r.setTreatment(null);
            r.setWidth(null);
            r.setArrivalDate(null);
            r.setWeight(null);

            requestReportList.add(r);

            for (Object[] rs : list) {
                RequestReportData rr = new RequestReportData();

                BigDecimal currentMaterialId = new BigDecimal(rs[0].toString());

                if (lastMaterialId.compareTo(currentMaterialId) == 0) {
                    rr.setWeight(new BigDecimal(rs[6].toString()));
                    rr.setArrivalDate((Date) rs[7]);

                    // copy REQUEST_DATE into dateList
                    dateList.add((Date) rs[7]);

                    System.out.println(new BigDecimal(rs[6].toString()));
                    totalWeight = totalWeight.add(new BigDecimal(rs[6].toString()));
                    sumRequestWeight = sumRequestWeight.add(new BigDecimal(rs[6].toString()));
                    count++;
                } else {
                    rr.setId(new BigDecimal(rs[0].toString()));
                    rr.setLamination((String) rs[1]);
                    rr.setTreatment((String) rs[2]);
                    rr.setThickness(new BigDecimal(rs[3].toString()));
                    rr.setWidth(new BigDecimal(rs[4].toString()));

                    if (rs[5] != null) {
                        rr.setLength(new BigDecimal(rs[5].toString()));
                    }

                    rr.setWeight(new BigDecimal(rs[6].toString()));
                    rr.setArrivalDate((Date) rs[7]);

                    // copy ARRIVAL_DATE into dateList
                    dateList.add((Date) rs[7]);

                    totalWeight = totalWeight.add(new BigDecimal(rs[6].toString()));
                    lastMaterialId = currentMaterialId;

                    if (count != 0) {
                        RequestReportData addition = new RequestReportData();
                        addition.setWeight(sumRequestWeight);

                        requestReportList.add(addition);
                    }

                    sumRequestWeight = new BigDecimal(rs[6].toString());
                    count = 0;
                }

                requestReportList.add(rr);
            }

            if (count > 0) {
                RequestReportData addition = new RequestReportData();
                addition.setWeight(sumRequestWeight);

                requestReportList.add(addition);
            }

            BufferedImage imfg = null;
            try {
                InputStream in = new ByteArrayInputStream(requestReportAltamiraimage);
                imfg = ImageIO.read(in);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Collections.sort(dateList);

            parameters.put("REQUEST_START_DATE", dateList.get(0));
            parameters.put("REQUEST_END_DATE", dateList.get(dateList.size() - 1));
            parameters.put("REQUEST_ID", requestId);
            parameters.put("TOTAL_WEIGHT", totalWeight);
            parameters.put("altamira_logo", imfg);
            parameters.put("USERNAME", httpRequest.getUserPrincipal() == null ? "" : httpRequest.getUserPrincipal().getName());

            Locale locale = new Locale.Builder().setLanguage("pt").setRegion("BR").build();
            parameters.put("REPORT_LOCALE", locale);

            JRDataSource dataSource = new JRBeanCollectionDataSource(requestReportList, false);

            jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);

            pdf = JasperExportManager.exportReportToPdf(jasperPrint);

            ByteArrayInputStream pdfStream = new ByteArrayInputStream(pdf);

            Response.ResponseBuilder response = Response.ok(pdfStream);
            response.header("Content-Disposition", "inline; filename=Request Report.pdf");

            return response.build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (jasperPrint != null) {
                    // store generated report in database
                    requestDao.insertGeneratedRequestReport(jasperPrint);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not insert generated report in database.");
            }
        }
    }
}
