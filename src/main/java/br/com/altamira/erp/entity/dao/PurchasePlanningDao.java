/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.erp.entity.dao;

import br.com.altamira.erp.entity.model.PlanningReportLog;
import br.com.altamira.erp.entity.model.PurchasePlanning;
import br.com.altamira.erp.entity.model.PurchasePlanningItem;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author PARTH
 */
public class PurchasePlanningDao {

    @PersistenceContext(name = "persistence/altamira-bpm", unitName = "altamira-bpm-PU")
    private EntityManager entityManager;

    public byte[] getPlanningReportJasperFile() throws SQLException {
        Blob tempBlob = (Blob) entityManager.createNativeQuery("SELECT JASPER_FILE FROM PLANNING_REPORT WHERE REPORT = (SELECT MAX(REPORT) FROM PLANNING_REPORT)")
                .getSingleResult();

        return tempBlob.getBytes(1, (int) tempBlob.length());
    }

    public byte[] getPlanningReportAltamiraImage() throws SQLException {
        Blob tempBlob = (Blob) entityManager.createNativeQuery("SELECT ALTAMIRA_LOGO FROM PLANNING_REPORT WHERE REPORT = (SELECT MAX(REPORT) FROM PLANNING_REPORT)")
                .getSingleResult();

        return tempBlob.getBytes(1, (int) tempBlob.length());
    }

    public boolean insertGeneratedPlanningReport(JasperPrint print) {
        byte[] bArray = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(print);
            oos.close();
            baos.close();

            bArray = baos.toByteArray();
        } catch (Exception e) {
            System.out.println("Error converting JasperPrint object to byte[] array");
            e.printStackTrace();
            return false;
        }

        try {
            PlanningReportLog log = new PlanningReportLog();
            log.setReportInstance(bArray);

            entityManager.persist(log);
        } catch (Exception e) {
            System.out.println("Error while inserting generated report in database.");
            e.printStackTrace();
            return false;
        }

        return true;
    }
    
    public PurchasePlanning findPurchasePlanningById(Long id)
    {
        TypedQuery<PurchasePlanning> query = entityManager.createNamedQuery("PurchasePlanning.findById", PurchasePlanning.class)
                                                          .setParameter("id", id);
        
        List<PurchasePlanning> results = query.getResultList();
        
        return results.get(0);
    }
    
    public PurchasePlanningItem findPurchasePlanningItemById(Long id)
    {
        TypedQuery<PurchasePlanningItem> query = entityManager.createNamedQuery("PurchasePlanningItem.findById",PurchasePlanningItem.class)
                                                              .setParameter("id", id);
        
        List<PurchasePlanningItem> results = query.getResultList();
        
        return results.get(0);
    }

}
