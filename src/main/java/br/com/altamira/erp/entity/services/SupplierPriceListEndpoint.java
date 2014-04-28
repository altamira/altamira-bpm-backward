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

import br.com.altamira.erp.entity.model.SupplierPriceList;

/**
 *
 */
@Stateless
@Path("/supplierpricelists")
public class SupplierPriceListEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    public Response create(SupplierPriceList entity) {
    	entity.setId(null);
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(SupplierPriceListEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        SupplierPriceList entity = em.find(SupplierPriceList.class, id);
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
        TypedQuery<SupplierPriceList> findByIdQuery = em
                .createQuery(
                        "SELECT DISTINCT s FROM SupplierPriceList s LEFT JOIN FETCH s.supplier LEFT JOIN FETCH s.material WHERE s.id = :entityId ORDER BY s.id",
                        SupplierPriceList.class);
        findByIdQuery.setParameter("entityId", id);
        SupplierPriceList entity;
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
    public List<SupplierPriceList> listAll(
            @QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<SupplierPriceList> findAllQuery = em
                .createQuery(
                        "SELECT DISTINCT s FROM SupplierPriceList s LEFT JOIN FETCH s.supplier LEFT JOIN FETCH s.material ORDER BY s.id",
                        SupplierPriceList.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<SupplierPriceList> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    //@Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(SupplierPriceList entity) {
        entity = em.merge(entity);
        return Response.ok(UriBuilder.fromResource(SupplierPriceListEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }
}
