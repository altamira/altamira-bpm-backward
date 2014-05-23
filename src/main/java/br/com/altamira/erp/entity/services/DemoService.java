/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.dao.OrderDao;
import br.com.altamira.erp.entity.dao.PurchasePlanningDao;
import br.com.altamira.erp.entity.dao.SupplierDao;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;

/**
 *
 * @author PARTH
 */

@Stateless
@Named("demoService")
public class DemoService {
    
    @Inject
    private OrderDao orderDao;
    
    @Inject
    private PurchasePlanningDao planningDao;
    
    @Inject
    private SupplierDao supplierDao;
    
    public void execute(DelegateExecution de) throws Exception
    {
        System.out.println("Demo service task execution started...");
        
        boolean approved = (Boolean) de.getVariable("approved");
        
        System.out.println("Approval: "+approved);
        
        List<String> purchaseOrderIdList = new ArrayList<String>();
        
        de.setVariable("execution", "completed");
        de.setVariable("purchaseOrderId", purchaseOrderIdList);
    }
    
}
