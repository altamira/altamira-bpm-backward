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

import br.com.altamira.erp.entity.model.Quotation;

/**
 *
 */
@Stateless
@Path("/quotations")
public class QuotationEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    public Response create(Quotation entity) {
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(QuotationEndpoint.class)
                .path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        Quotation entity = em.find(Quotation.class, id);
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
        TypedQuery<Quotation> findByIdQuery = em
                .createQuery(
                        "SELECT DISTINCT q FROM Quotation q LEFT JOIN FETCH q.purchasePlanningSet LEFT JOIN FETCH q.quotationRequestSet LEFT JOIN FETCH q.quotationItemSet WHERE q.id = :entityId ORDER BY q.id",
                        Quotation.class);
        findByIdQuery.setParameter("entityId", id);
        Quotation entity;
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
    public List<Quotation> listAll(@QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<Quotation> findAllQuery = em
                .createQuery(
                        "SELECT DISTINCT q FROM Quotation q LEFT JOIN FETCH q.purchasePlanningSet LEFT JOIN FETCH q.quotationRequestSet LEFT JOIN FETCH q.quotationItemSet ORDER BY q.id",
                        Quotation.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<Quotation> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(Quotation entity) {
        entity = em.merge(entity);
        return Response.noContent().build();
    }
}
