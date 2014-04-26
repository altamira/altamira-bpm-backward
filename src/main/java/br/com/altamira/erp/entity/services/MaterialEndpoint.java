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

import br.com.altamira.erp.entity.model.Material;

/**
 *
 */
@Stateless
@Path("/materials")
public class MaterialEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    public Response create(Material entity) {
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(MaterialEndpoint.class)
                .path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        Material entity = em.find(Material.class, id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        em.remove(entity);
        em.flush();
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("id") long id) {
        TypedQuery<Material> findByIdQuery = em
                .createQuery(
                        "SELECT DISTINCT m FROM Material m LEFT JOIN FETCH m.materialStandardSet LEFT JOIN FETCH m.supplierPriceListSet LEFT JOIN FETCH m.requestItemSet WHERE m.id = :entityId ORDER BY m.id",
                        Material.class);
        findByIdQuery.setParameter("entityId", id);
        Material entity;
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
    public List<Material> listAll(@QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<Material> findAllQuery = em
                .createQuery(
                        "SELECT DISTINCT m FROM Material m LEFT JOIN FETCH m.materialStandardSet LEFT JOIN FETCH m.supplierPriceListSet LEFT JOIN FETCH m.requestItemSet ORDER BY m.id",
                        Material.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<Material> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(Material entity) {
        entity = em.merge(entity);
        return Response.noContent().build();
    }
}
