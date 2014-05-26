/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.services;

import br.com.altamira.erp.entity.model.PurchasePlanning;
import br.com.altamira.erp.entity.model.Quotation;
import br.com.altamira.erp.entity.model.Request;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.joda.time.DateTime;

/**
 *
 * @author PARTH
 */

@Stateless
@Path("/servicetask")
public class ServiceTaskTest_Endpoint {
    
    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @Inject
    private RuntimeService runtimeService;
    
    @Inject
    private TaskService taskService;
    
    @POST
    @Path("/start")
    @Consumes("application/json")
    @Produces("application/json")
    public Response start() {
        
        Long requestId = new Long("12");
            
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("requestId", requestId);
            
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ServiceTask_test", variables);
        String instanceId = processInstance.getProcessInstanceId();
        
        
        return Response.ok().build();
    }
    
    @POST
    @Path("/closeQuotation")
    @Consumes("application/json")
    @Produces("application/json")
    public Response closeQuotation() {
        
        List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals("requestId",new Long("12")).list();
        
        if(!tasks.isEmpty())
        {
            Task task = tasks.get(0);
            String instanceId = task.getProcessInstanceId();
            taskService.complete(task.getId());
        }
        
        return Response.ok().build();
    }
    
    @POST
    @Path("/closeApproval")
    @Consumes("application/json")
    @Produces("application/json")
    public Response closeApproval() {
        
        List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals("requestId",new Long("12")).list();
        
        if(!tasks.isEmpty())
        {
            Task task = tasks.get(0);
            String instanceId = task.getProcessInstanceId();
            runtimeService.setVariable(instanceId, "approved", true);
            runtimeService.setVariable(instanceId, "reason", "approved");
            
            taskService.complete(task.getId());
        }
        
        
        return Response.ok().build();
    }
    
}
