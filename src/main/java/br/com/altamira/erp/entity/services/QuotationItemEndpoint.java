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
import br.com.altamira.erp.entity.model.QuotationItem;

/**
 *
 */
@Stateless
@Path("/quotations/current/items")
public class QuotationItemEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(QuotationItem entity) {
    	entity.setId(null);
        Quotation quotation = em.createNamedQuery("Quotation.getCurrent", Quotation.class).getSingleResult();
        quotation.getQuotationItem().add(entity);
        entity.setQuotation(quotation);
        em.persist(entity);
        return Response.created(
                UriBuilder.fromResource(QuotationItemEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        QuotationItem entity = em.find(QuotationItem.class, id);
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
        TypedQuery<QuotationItem> findByIdQuery = em.createNamedQuery("QuotationItem.findById", QuotationItem.class);
        findByIdQuery.setParameter("id", id);
        QuotationItem entity;
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
    public List<QuotationItem> listAll(
            @QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<QuotationItem> findAllQuery = em.createNamedQuery("QuotationItem.findAll", QuotationItem.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<QuotationItem> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(@PathParam("id") long id, QuotationItem entity) {
        Quotation quotation = em.createNamedQuery("Quotation.getCurrent", Quotation.class).getSingleResult();
        entity.setQuotation(quotation);
        entity = em.merge(entity);
        return Response.ok(UriBuilder.fromResource(QuotationItemEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }
}
