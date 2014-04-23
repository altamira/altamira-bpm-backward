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

import br.com.altamira.erp.entity.model.QuotationRequest;

/**
 * 
 */
@Stateless
@Path("/quotationrequests")
public class QuotationRequestEndpoint {
	@PersistenceContext(unitName = "altamira-bpm-PU")
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(QuotationRequest entity) {
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(QuotationRequestEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") long id) {
		QuotationRequest entity = em.find(QuotationRequest.class, id);
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
		TypedQuery<QuotationRequest> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT q FROM QuotationRequest q LEFT JOIN FETCH q.request LEFT JOIN FETCH q.quotation WHERE q.id = :entityId ORDER BY q.id",
						QuotationRequest.class);
		findByIdQuery.setParameter("entityId", id);
		QuotationRequest entity;
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
	public List<QuotationRequest> listAll(
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<QuotationRequest> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT q FROM QuotationRequest q LEFT JOIN FETCH q.request LEFT JOIN FETCH q.quotation ORDER BY q.id",
						QuotationRequest.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<QuotationRequest> results = findAllQuery.getResultList();
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(QuotationRequest entity) {
		entity = em.merge(entity);
		return Response.noContent().build();
	}
}