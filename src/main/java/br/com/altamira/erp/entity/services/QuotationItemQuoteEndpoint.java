package br.com.altamira.erp.entity.services;

import java.net.URI;
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
import br.com.altamira.erp.entity.model.QuotationItemQuote;
import br.com.altamira.erp.entity.model.Supplier;

/**
 *
 */
@Stateless
@Path("/quotations/{quotation:[0-9][0-9]*}/items/{item:[0-9][0-9]*}/quotes")
public class QuotationItemQuoteEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;

    @POST
    @Consumes("application/json")
    public Response create(@PathParam("quotation") long quotationId, @PathParam("item") long quotationItemId, QuotationItemQuote entity) {
    	TypedQuery<QuotationItem> findByIdQuery = em.createQuery("SELECT qi FROM QuotationItem qi JOIN FETCH qi.quotation WHERE qi.id = :id", QuotationItem.class);
        findByIdQuery.setParameter("id", quotationItemId);
        QuotationItem quotationItem;
        try {
        	quotationItem = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
        	quotationItem = null;
        }
        if (quotationItem == null || quotationItem.getQuotation().getId() != quotationId) {
            return Response.status(Status.NOT_FOUND).build();
        }        
    	entity.setId(null);
    	entity.setQuotationItem(quotationItem);
    	quotationItem.getQuotationItemQuote().add(entity);
        em.persist(entity);
        /*return Response.created(
                UriBuilder.fromResource(QuotationItemQuoteEndpoint.class)
                .resolveTemplate("quotation", quotationId)
                .resolveTemplate("item", quotationItemId)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();*/
        return Response.ok().entity(entity).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") long id) {
        QuotationItemQuote entity = em.find(QuotationItemQuote.class, id);
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
        TypedQuery<QuotationItemQuote> findByIdQuery = em.createNamedQuery("QuotationItemQuote.findById", QuotationItemQuote.class);
        findByIdQuery.setParameter("id", id);
        QuotationItemQuote entity;
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
    public List<QuotationItemQuote> listAll(
            @QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<QuotationItemQuote> findAllQuery = em.createNamedQuery("QuotationItemQuote.findAll", QuotationItemQuote.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        final List<QuotationItemQuote> results = findAllQuery.getResultList();
        return results;
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(@PathParam("quotation") long quotationId, @PathParam("item") long quotationItemId, @PathParam("id") long id, QuotationItemQuote entity) {
    	entity.setQuotationItem(em.find(QuotationItem.class, quotationItemId));
    	entity.setId(id);
        entity = em.merge(entity);
        /*return Response.ok(UriBuilder.fromResource(QuotationItemQuoteEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();*/
        return Response.ok().entity(entity).build();
    }
}
