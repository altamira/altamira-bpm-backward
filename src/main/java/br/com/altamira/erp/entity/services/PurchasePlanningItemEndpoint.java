package br.com.altamira.erp.entity.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

import br.com.altamira.erp.entity.dao.PurchasePlanningDao;
import br.com.altamira.erp.entity.dao.RequestDao;
import br.com.altamira.erp.entity.model.PurchasePlanning;
import br.com.altamira.erp.entity.model.PurchasePlanningItem;
import br.com.altamira.erp.entity.model.Request;

/**
 *
 */
@Stateless
@Path("/purchaseplannings/{purchasePlanning:[0-9][0-9]*}/items")
public class PurchasePlanningItemEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @Inject
    private PurchasePlanningDao purchasePlanningDao;
    
    @POST
    @Consumes("application/json")
    public Response create(@PathParam("purchasePlanning") long purchasePlanningId, PurchasePlanningItem entity) {
    	entity.setId(null);
    	PurchasePlanning purchasePlanning = purchasePlanningDao.getCurrent();
    	purchasePlanning.getPurchasePlanningItem().add(entity);
		entity.setPurchasePlanning(purchasePlanning);
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
    public Response deleteById(@PathParam("purchasePlanning") long purchasePlanningId, @PathParam("id") long id) {
        PurchasePlanningItem entity = em.find(PurchasePlanningItem.class, id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        em.remove(entity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("purchasePlanning") long purchasePlanningId, @PathParam("id") long id) {
        TypedQuery<PurchasePlanningItem> findByIdQuery = em.createNamedQuery("PurchasePlanningItem.findById", PurchasePlanningItem.class);
        findByIdQuery.setParameter("id", id);
        PurchasePlanningItem entity;
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
    public List<PurchasePlanningItem> listAll(
            @QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<PurchasePlanningItem> findAllQuery = em.createNamedQuery("PurchasePlanningItem.findAll", PurchasePlanningItem.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<PurchasePlanningItem> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("purchasePlanning") long purchasePlanningId, @PathParam("id") long id, PurchasePlanningItem entity) {
    	entity.setPurchasePlanning(em.find(PurchasePlanning.class, purchasePlanningId));
    	entity.setId(id);
        entity = em.merge(entity);
        /*return Response.ok(UriBuilder.fromResource(PurchasePlanningItemEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();*/
        return Response.ok().entity(entity).build();
    }
}
