package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.dao.QuotationDao;

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
import br.com.altamira.erp.entity.model.Request;

import org.codehaus.jackson.map.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.joda.time.DateTime;

/**
 *
 */
@Stateless
@Path("/quotations")
public class QuotationEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @Inject
    private QuotationDao quotationDao;

    @POST
    @Path("/current")
    @Consumes("application/json")
    public Response create() {
    	Quotation quotation;

    	List<Quotation> quotations = em
                .createNamedQuery("Quotation.getCurrent", Quotation.class)
                .getResultList();

        if (!quotations.isEmpty()) {

        	quotation = quotations.get(0);
        	quotation.setClosedDate(DateTime.now().toDate());
            
            em.merge(quotation);
            em.flush();

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
        Quotation entity = em.find(Quotation.class, id);
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
        TypedQuery<Quotation> findByIdQuery = em.createNamedQuery("Quotation.findById", Quotation.class);
        findByIdQuery.setParameter("id", id);
        Quotation entity;
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
    public List<Quotation> listAll(@QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<Quotation> findAllQuery = em.createNamedQuery("Quotation.findAll", Quotation.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<Quotation> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Consumes("application/json")
    public Response update(Quotation entity) {
        entity = em.merge(entity);
        return Response.ok(UriBuilder.fromResource(QuotationEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }

    @GET
    @Path("/current")
    @Produces("application/json")
    public Response getCurrent() {
    	Quotation entity = quotationDao.getCurrent();
    	return Response.ok(UriBuilder.fromResource(QuotationEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }
    
    @GET
    @Path("{id:[0-9][0-9]*}/report")
    @Produces("application/pdf")
    public Response getQuotationReportInPdf(@PathParam("id") long quotationId) {

        // generate report
        JasperPrint jasperPrint = null;

        try {
            byte[] quotationReportJasper = quotationDao.getQuotationReportJasperFile();
            byte[] quotationReportAltamiraimage = quotationDao.getQuotationReportAltamiraImage();
            byte[] pdf = null;

            final ByteArrayInputStream reportStream = new ByteArrayInputStream(quotationReportJasper);
            final Map<String, Object> parameters = new HashMap<String, Object>();

            List<Object[]> list = quotationDao.selectQuotationReportDataById(quotationId);
            List<BigDecimal> priceList = new ArrayList<BigDecimal>();

            // set pricelist for quotation items
            for (Object[] rs : list) {
                String materialLamination = rs[0].toString();
                String materialTreatment = rs[1].toString();
                String materialThickness = rs[2].toString();

                String str = "http://localhost:8080/altamira-bpm/rest/quotations/test/priceList?lamination=" + materialLamination + "&treatment=" + materialTreatment + "&thickness=" + materialThickness;

                HttpClient httpClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(str);
                get.addHeader("accept", "application/json");

                HttpResponse httpResponse = httpClient.execute(get);

                BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));

                StringBuffer jsonObject = new StringBuffer();
                String json = new String();
                System.out.println("Output from Server .... \n");

                while ((json = br.readLine()) != null) {
                    jsonObject.append(json);
                    System.out.println(jsonObject);
                }

                // parse json response
                Map<String, Object> quotationItem = new HashMap<String, Object>();
                try {
                    quotationItem = new ObjectMapper().readValue(jsonObject.toString(), new TypeReference<HashMap<String, Object>>() {
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                List<Map> materialList = (List<Map>) ((Map<String, Object>) quotationItem.get("data")).get("materials");

                Map<String, String> map = materialList.get(0);
                String avgPrice = map.get("averageprice");

                priceList.add(new BigDecimal(avgPrice));

            }

            parameters.put("QUOTATION_ID", new BigDecimal(quotationId));
            parameters.put("PRICELIST", priceList);

            // set current pricelist code
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://localhost:8080/altamira-bpm/rest/quotations/test/current");
            get.addHeader("accept", "application/json");

            HttpResponse httpResponse = httpClient.execute(get);

            BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));

            StringBuffer jsonObject = new StringBuffer();
            String json = new String();
            System.out.println("Output from Server .... \n");
            while ((json = br.readLine()) != null) {
                jsonObject.append(json);
                System.out.println(jsonObject);
            }

            httpClient.getConnectionManager().shutdown();

            // parse json response
            Map<String, Object> currentPriceList = new HashMap<String, Object>();
            try {
                currentPriceList = new ObjectMapper().readValue(jsonObject.toString(), new TypeReference<HashMap<String, Object>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            parameters.put("PRICELIST_CODE", ((Map<String, Object>) currentPriceList.get("data")).get("pricelist"));

            // set quotation Date
            Quotation quotation = quotationDao.getQuotationDetailsById(quotationId);
            parameters.put("QUOTATION_DATE", quotation.getCreatedDate());

            Locale locale = new Locale.Builder().setLanguage("pt").setRegion("BR").build();
            parameters.put("REPORT_LOCALE", locale);

            parameters.put("USERNAME", "Parth");

            BufferedImage imfg = null;
            try {
                InputStream in = new ByteArrayInputStream(quotationReportAltamiraimage);
                imfg = ImageIO.read(in);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            parameters.put("altamira_logo", imfg);

            Session session = quotationDao.unwrap();

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
            response.header("Content-Disposition", "inline; filename=Quotation Report.pdf");

            return response.build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (jasperPrint != null) {
                    // store generated report in database
                    quotationDao.insertGeneratedQuotationReport(jasperPrint);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not insert generated report in database.");
            }
        }
    }

}
