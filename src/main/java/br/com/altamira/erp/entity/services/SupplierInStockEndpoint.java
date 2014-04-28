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

import br.com.altamira.erp.entity.model.SupplierInStock;

/**
 *
 */
@Stateless
@Path("/supplierinstocks")
public class SupplierInStockEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    public Response create(SupplierInStock entity) {
    	entity.setId(null);
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(SupplierInStockEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        SupplierInStock entity = em.find(SupplierInStock.class, id);
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
        TypedQuery<SupplierInStock> findByIdQuery = em
                .createQuery(
                        "SELECT DISTINCT s FROM SupplierInStock s LEFT JOIN FETCH s.quotationItemQuote WHERE s.id = :entityId ORDER BY s.id",
                        SupplierInStock.class);
        findByIdQuery.setParameter("entityId", id);
        SupplierInStock entity;
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
    public List<SupplierInStock> listAll(
            @QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<SupplierInStock> findAllQuery = em
                .createQuery(
                        "SELECT DISTINCT s FROM SupplierInStock s LEFT JOIN FETCH s.quotationItemQuote ORDER BY s.id",
                        SupplierInStock.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<SupplierInStock> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    //@Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(SupplierInStock entity) {
        entity = em.merge(entity);
        return Response.ok(UriBuilder.fromResource(SupplierInStockEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }
}
