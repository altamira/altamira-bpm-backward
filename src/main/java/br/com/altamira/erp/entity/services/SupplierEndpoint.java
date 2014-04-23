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

import br.com.altamira.erp.entity.model.Supplier;

/**
 * 
 */
@Stateless
@Path("/suppliers")
public class SupplierEndpoint {
	@PersistenceContext(unitName = "altamira-bpm-PU")
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(Supplier entity) {
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(SupplierEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") long id) {
		Supplier entity = em.find(Supplier.class, id);
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
		TypedQuery<Supplier> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT s FROM Supplier s LEFT JOIN FETCH s.quotationItemQuoteSet LEFT JOIN FETCH s.supplierPriceListSet LEFT JOIN FETCH s.supplierContactSet LEFT JOIN FETCH s.purchaseOrderSet LEFT JOIN FETCH s.supplierStandardSet LEFT JOIN FETCH s.purchasePlanningItemSet WHERE s.id = :entityId ORDER BY s.id",
						Supplier.class);
		findByIdQuery.setParameter("entityId", id);
		Supplier entity;
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
	public List<Supplier> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<Supplier> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT s FROM Supplier s LEFT JOIN FETCH s.quotationItemQuoteSet LEFT JOIN FETCH s.supplierPriceListSet LEFT JOIN FETCH s.supplierContactSet LEFT JOIN FETCH s.purchaseOrderSet LEFT JOIN FETCH s.supplierStandardSet LEFT JOIN FETCH s.purchasePlanningItemSet ORDER BY s.id",
						Supplier.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Supplier> results = findAllQuery.getResultList();
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(Supplier entity) {
		entity = em.merge(entity);
		return Response.noContent().build();
	}
}