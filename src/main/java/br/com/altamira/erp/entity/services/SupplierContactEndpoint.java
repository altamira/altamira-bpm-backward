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

import br.com.altamira.erp.entity.model.SupplierContact;

/**
 *
 */
@Stateless
@Path("/suppliercontacts")
public class SupplierContactEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    public Response create(SupplierContact entity) {
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(SupplierContactEndpoint.class)
                .path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        SupplierContact entity = em.find(SupplierContact.class, id);
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
        TypedQuery<SupplierContact> findByIdQuery = em
                .createQuery(
                        "SELECT DISTINCT s FROM SupplierContact s LEFT JOIN FETCH s.supplier WHERE s.id = :entityId ORDER BY s.id",
                        SupplierContact.class);
        findByIdQuery.setParameter("entityId", id);
        SupplierContact entity;
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
    public List<SupplierContact> listAll(
            @QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<SupplierContact> findAllQuery = em
                .createQuery(
                        "SELECT DISTINCT s FROM SupplierContact s LEFT JOIN FETCH s.supplier ORDER BY s.id",
                        SupplierContact.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<SupplierContact> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(SupplierContact entity) {
        entity = em.merge(entity);
        return Response.noContent().build();
    }
}
