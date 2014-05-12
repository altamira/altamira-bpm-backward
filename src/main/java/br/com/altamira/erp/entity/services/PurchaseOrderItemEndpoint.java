package br.com.altamira.erp.entity.services;

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

import br.com.altamira.erp.entity.model.PurchaseOrder;
import br.com.altamira.erp.entity.model.PurchaseOrderItem;
import br.com.altamira.erp.entity.model.PurchasePlanning;

/**
 *
 */
@Stateless
@Path("/purchaseorders/{purchaseOrder:[0-9][0-9]*}/items")
public class PurchaseOrderItemEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    public Response create(@PathParam("purchaseOrder") long purchaseOrderId, PurchaseOrderItem entity) {
    	entity.setId(null);
    	PurchaseOrder purchaseOrder = em.find(PurchaseOrder.class, purchaseOrderId);
    	purchaseOrder.getPurchaseOrderItem().add(entity);
		entity.setPurchaseOrder(purchaseOrder);
        em.persist(entity);
        /*return Response.created(
                UriBuilder.fromResource(RequestItemEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();*/
        return Response.ok().entity(entity).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("purchaseOrder") long purchaseOrderId, @PathParam("id") long id) {
        PurchaseOrderItem entity = em.find(PurchaseOrderItem.class, id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        em.remove(entity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("purchaseOrder") long purchaseOrderId, @PathParam("id") long id) {
        TypedQuery<PurchaseOrderItem> findByIdQuery = em.createNamedQuery("PurchaseOrderItem.findById", PurchaseOrderItem.class);
        findByIdQuery.setParameter("id", id);
        PurchaseOrderItem entity;
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
    public List<PurchaseOrderItem> listAll(
            @QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<PurchaseOrderItem> findAllQuery = em.createNamedQuery("PurchaseOrderItem.findAll", PurchaseOrderItem.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<PurchaseOrderItem> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("purchaseOrder") long purchaseOrderId, @PathParam("id") long id, PurchaseOrderItem entity) {
    	entity.setPurchaseOrder(em.find(PurchaseOrder.class, purchaseOrderId));
    	entity.setId(id);
        entity = em.merge(entity);
        /*return Response.ok(UriBuilder.fromResource(PurchaseOrderItemEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();*/
        return Response.ok().entity(entity).build();
    }
}
