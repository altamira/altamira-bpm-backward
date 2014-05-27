/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.bpm.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.hibernate.jdbc.ReturningWork;

import br.com.altamira.erp.entity.dao.OrderDao;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import org.hibernate.Session;

/**
 *
 * @author PARTH
 */
@Stateless
@Named("sendOrders")
public class SendOrdersService {

    @PersistenceContext(name = "persistence/altamira-bpm", unitName = "altamira-bpm-PU")
    private EntityManager entityManager;

    @Inject
    private OrderDao orderDao;
    
    @Context
    private HttpServletRequest httpRequest;

    @Inject
    private MailService mailService;

    @Transactional
    public void execute(DelegateExecution de) throws Exception {

        System.out.println("Send Orders To Suppliers service task execution started...");

        List<String> purchaseOrderIdList = (List<String>) de.getVariable("purchaseOrderId");

        for (String str : purchaseOrderIdList) {

            BigDecimal purchaseOrderId = new BigDecimal(str);

            JasperPrint print = generatePurchaseOrderReport(purchaseOrderId);
            ByteArrayInputStream bais = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(print));

            Map<String, InputStream> emailAttachmentList = new HashMap<String, InputStream>();
            emailAttachmentList.put("PurchaseOrderReport", bais);

            List<Map> supplierInfo = orderDao.getSupplierMailAddressForPurchaseOrder(purchaseOrderId);
            
            BigDecimal pid =  (BigDecimal) (supplierInfo.get(0).get("PURCHASE_ORDER_ID"));
            String supplierName = (String) (supplierInfo.get(0).get("SUPPLIER_NAME"));
            
            String toAddress = new String();
            for (Map supplier : supplierInfo) {
                
                String tempAddress = (String) supplier.get("MAIL_ADDRESS");
                if(toAddress.isEmpty())
                    toAddress+=tempAddress;
                else
                    toAddress+=(","+tempAddress);
            }
            
            String message = "Please find the attachment for the Purchase Order";

            String subject = "Purchase Order ID :"
                           + new DecimalFormat("#00000").format(pid)
                           + " Supplier: "
                           + supplierName;

            mailService.sendMail(toAddress,
                                 null,
                                 null,
                                 subject,
                                 message,
                                 emailAttachmentList);
        }

    }

    private JasperPrint generatePurchaseOrderReport(BigDecimal orderId) {

        // generate report
        JasperPrint jasperPrint = null;

        try {
            byte[] purchaseOrderReportJasper = orderDao.getPurchaseOrderReportJasperFile();
            byte[] purchaseOrderReportAltamiraimage = orderDao.getPurchaseOrderReportAltamiraImage();
            byte[] pdf = null;

            final ByteArrayInputStream reportStream = new ByteArrayInputStream(purchaseOrderReportJasper);
            final Map<String, Object> parameters = new HashMap<String, Object>();

            parameters.put("PURCHASE_ORDER_ID", orderId);

            Date purchaseOrderDate = orderDao.getPurchaseOrderCreatedDateById(orderId.longValue());

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

            Session session = entityManager.unwrap(Session.class);

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

        return jasperPrint;
    }

}
