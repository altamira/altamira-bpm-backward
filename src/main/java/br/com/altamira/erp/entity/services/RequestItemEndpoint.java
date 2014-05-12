package br.com.altamira.erp.entity.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

import br.com.altamira.erp.entity.dao.MaterialDao;
import br.com.altamira.erp.entity.dao.RequestDao;
import br.com.altamira.erp.entity.model.Company;
import br.com.altamira.erp.entity.model.Material;
import br.com.altamira.erp.entity.model.Request;
import br.com.altamira.erp.entity.model.RequestItem;

/**
 *
 */
@Stateless
@Path("/requests/{request:[0-9][0-9]*}/items")
public class RequestItemEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;
    
    @Inject
    private RequestDao requestDao;
    
    @Inject
    private MaterialDao materialDao;

    @POST
    @Consumes("application/json")
    public Response create(@PathParam("request") Long requestId, RequestItem entity) {
    	entity.setId(null);
    	Material material = materialDao.create(entity.getMaterial());
    	entity.setMaterial(material);
		Request request = requestDao.getCurrent();
		request.getItems().add(entity);
		entity.setRequest(request);
        em.persist(entity);
        /*return Response.created(
                UriBuilder.fromResource(RequestItemEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();*/
        return Response.ok().entity(entity).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("request") long requestId, @PathParam("id") Long id) {
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
    public Response findById(@PathParam("request") long requestId, @PathParam("id") Long id) {
        TypedQuery<RequestItem> findByIdQuery = em.createNamedQuery("RequestItem.findById",RequestItem.class);
        findByIdQuery.setParameter("id", id);
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
        TypedQuery<RequestItem> findAllQuery = em.createNamedQuery("RequestItem.findAll", RequestItem.class);
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
    public Response update(@PathParam("request") long requestId, @PathParam("id") Long id, RequestItem entity) {
    	entity.setRequest(em.find(Request.class, requestId));
    	entity.setId(id);
    	/*if (entity.getRequest() == null) {
    		Request request = requestDao.find(requestId);
    		//request.getItems().add(entity);
    		entity.setRequest(request);
    	}
    	if (entity.getMaterial().getCompany() == null) {
    		entity.getMaterial().setCompany(em.find(Company.class, 1));
    	}*/
        entity = em.merge(entity);
        /*return Response.ok(UriBuilder.fromResource(RequestItemEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();*/
        return Response.ok().entity(entity).build();
    }
}
