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

import br.com.altamira.erp.entity.model.RequestItem;
import br.com.altamira.erp.entity.model.Standard;

/**
 *
 */
@Stateless
@Path("/standards")
public class StandardEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response create(Standard entity) {
    	entity.setId(null);
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(StandardEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        Standard entity = em.find(Standard.class, id);
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
        TypedQuery<Standard> findByIdQuery = em.createNamedQuery("Standard.findById", Standard.class);
        findByIdQuery.setParameter("id", id);
        Standard entity;
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
    public List<Standard> listAll(@QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<Standard> findAllQuery = em.createNamedQuery("Standard.findAll", Standard.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<Standard> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("id") long id, Standard entity) {
        entity = em.merge(entity);
        return Response.ok(UriBuilder.fromResource(StandardEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }
}
