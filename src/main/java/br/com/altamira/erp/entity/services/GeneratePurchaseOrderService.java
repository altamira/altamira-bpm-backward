/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.dao.OrderDao;
import br.com.altamira.erp.entity.dao.PurchasePlanningDao;
import br.com.altamira.erp.entity.dao.SupplierDao;
import br.com.altamira.erp.entity.model.PaymentConditionItem;
import br.com.altamira.erp.entity.model.PurchaseOrder;
import br.com.altamira.erp.entity.model.PurchaseOrderItem;
import br.com.altamira.erp.entity.model.PurchaseOrderPayment;
import br.com.altamira.erp.entity.model.PurchasePlanning;
import br.com.altamira.erp.entity.model.PurchasePlanningItem;
import br.com.altamira.erp.entity.model.Supplier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import org.camunda.bpm.engine.delegate.DelegateExecution;

/**
 *
 * @author PARTH
 */
@Stateless
@Named("generatePurchaseOrder")
public class GeneratePurchaseOrderService {

    @Inject
    private OrderDao orderDao;
    
    @Inject
    private PurchasePlanningDao planningDao;
    
    @Inject
    private SupplierDao supplierDao;

    @Transactional
    public void execute(DelegateExecution de) throws Exception {

        System.out.println("Generate Order service task execution started...");

        BigDecimal planningId = new BigDecimal(de.getVariable("planningId").toString());
        
        List<BigDecimal> supplierList = orderDao.selectDistinctSuppliersFromPurchasePlan(planningId.longValue());

        List<String> purchaseOrderIdList = new ArrayList<String>();

        try {

            for (BigDecimal supplierId : supplierList) {

                PurchasePlanning planning = planningDao.findPurchasePlanningById(planningId.longValue());
                Supplier supplier = supplierDao.find(supplierId.longValue());

                List<Object[]> list = orderDao.selectOrderItemDetailsBySupplierId(planningId.longValue(), supplierId.longValue());

                // seperate orderItems based on company
                Map<BigDecimal, List<Object[]>> seperatedOrderItemsMap = new HashMap<BigDecimal, List<Object[]>>();
                for (Object[] rs : list) {
                    
                    BigDecimal company = (BigDecimal) rs[6];

                    if (seperatedOrderItemsMap.containsKey(company)) {
                        List<Object[]> tempList = seperatedOrderItemsMap.get(company);
                        tempList.add(rs);
                    } else {
                        List<Object[]> tempList = new ArrayList<Object[]>();
                        tempList.add(rs);
                        seperatedOrderItemsMap.put(company, tempList);
                    }
                }

                // use seperate orderItems list to generate purchase ordres
                for (Map.Entry<BigDecimal, List<Object[]>> entry : seperatedOrderItemsMap.entrySet()) {
                    
                    BigDecimal company = entry.getKey();
                    List<Object[]> orderItemList = entry.getValue();

                    // insert purchase order
                    PurchaseOrder purchaseOrder = new PurchaseOrder();
                    purchaseOrder.setPurchasePlanning(planning);
                    purchaseOrder.setSupplier(supplier);
                    purchaseOrder.setCreatedDate(new Date());
                    purchaseOrder.setCompanyShipping(company.toBigInteger());
                    purchaseOrder.setCompanyInvoice(BigInteger.ONE);
                    purchaseOrder.setCompanyBilling(BigInteger.ONE);
                    if(!company.toBigInteger().equals(BigInteger.ONE))
                    {
                        purchaseOrder.setComments("ATENÇÃO: Entrega através de operação triangular.");
                    }
                    else
                    {
                        purchaseOrder.setComments(" ");
                    }
                    Long purchaseOrderId = orderDao.insertPurchaseOrder(purchaseOrder);
                    purchaseOrderIdList.add(purchaseOrderId.toString());
                    List<Long> purchaseOrderItemList = new ArrayList<Long>();
                    
                    // insert records for Purchase Order Payment
                    List<PaymentConditionItem> conditionItems = orderDao.findPaymentConditionItemsByPaymentCondition(supplier.getPaymentCondition());
                    for(PaymentConditionItem pci : conditionItems)
                    {
                        BigInteger tempPercentage = pci.getPercentage();
                        BigInteger tempPeriod = pci.getPeriod();
                        
                        PurchaseOrderPayment pop = new PurchaseOrderPayment();
                        pop.setPurchaseOrder(purchaseOrder);
                        pop.setPercentage(new BigDecimal(tempPercentage));
                        pop.setPeriod(tempPeriod.shortValue());
                        
                        orderDao.insertPurchaseOrderPayment(pop);
                    }

                    // insert Purchase Order Items
                    for (Object[] rs : orderItemList) {

                        BigDecimal planningItemId = (BigDecimal) rs[0];
                        Date date = (Date) rs[1];
                        BigDecimal weight = ((BigDecimal) rs[2]);
                        BigDecimal price = (BigDecimal) rs[3];
                        BigDecimal tax = (BigDecimal) rs[4];
                        BigDecimal standard = (BigDecimal) rs[5];

                        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
                        purchaseOrderItem.setPurchaseOrder(purchaseOrder);

                        PurchasePlanningItem planningItem = planningDao.findPurchasePlanningItemById(planningItemId.longValue());
                        purchaseOrderItem.setPlanningItem(planningItem);

                        purchaseOrderItem.setDate(date);
                        purchaseOrderItem.setWeight(weight);
                        purchaseOrderItem.setPrice(price);
                        purchaseOrderItem.setTax(tax);
                        purchaseOrderItem.setStandard(standard);

                        Long purchaseOrderItemId = orderDao.insertPurchaseOrderItem(purchaseOrderItem);

                        purchaseOrderItemList.add(purchaseOrderItemId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        de.setVariable("purchaseOrderId", purchaseOrderIdList);

    }

}
