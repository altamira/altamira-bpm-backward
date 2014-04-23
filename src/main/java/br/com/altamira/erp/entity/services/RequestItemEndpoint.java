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

/**
 * 
 */
@Stateless
@Path("/requestitems")
public class RequestItemEndpoint {
	@PersistenceContext(unitName = "altamira-bpm-PU")
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(RequestItem entity) {
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(RequestItemEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") long id) {
		RequestItem entity = em.find(RequestItem.class, id);
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
		TypedQuery<RequestItem> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT r FROM RequestItem r LEFT JOIN FETCH r.request LEFT JOIN FETCH r.material LEFT JOIN FETCH r.purchasePlanningItemSet WHERE r.id = :entityId ORDER BY r.id",
						RequestItem.class);
		findByIdQuery.setParameter("entityId", id);
		RequestItem entity;
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
	public List<RequestItem> listAll(
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<RequestItem> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT r FROM RequestItem r LEFT JOIN FETCH r.request LEFT JOIN FETCH r.material LEFT JOIN FETCH r.purchasePlanningItemSet ORDER BY r.id",
						RequestItem.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<RequestItem> results = findAllQuery.getResultList();
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(RequestItem entity) {
		entity = em.merge(entity);
		return Response.noContent().build();
	}
}