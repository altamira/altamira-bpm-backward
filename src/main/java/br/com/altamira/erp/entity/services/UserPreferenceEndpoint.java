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
import br.com.altamira.erp.entity.model.UserPreference;

/**
 *
 */
@Stateless
@Path("/userpreferences")
public class UserPreferenceEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    public Response create(UserPreference entity) {
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(UserPreferenceEndpoint.class)
                .path(String.valueOf(entity.getName())).build())
                .entity(entity)
                .build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") String id) {
        UserPreference entity = em.find(UserPreference.class, id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        em.remove(entity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{name:[a-z][A-Z]*}")
    @Produces("application/json")
    public Response findByName(@PathParam("name") String name) {
        TypedQuery<UserPreference> findByIdQuery = em.createNamedQuery("UserPreference.findByName", UserPreference.class);
        findByIdQuery.setParameter("name", name);
        UserPreference entity;
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
    public List<UserPreference> listAll(
            @QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<UserPreference> findAllQuery = em.createNamedQuery("UserPreference.findAll", UserPreference.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<UserPreference> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    //@Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(UserPreference entity) {
        entity = em.merge(entity);
        return Response.ok(UriBuilder.fromResource(UserPreferenceEndpoint.class)
                .path(String.valueOf(entity.getName())).build())
                .entity(entity)
                .build();
    }
}
