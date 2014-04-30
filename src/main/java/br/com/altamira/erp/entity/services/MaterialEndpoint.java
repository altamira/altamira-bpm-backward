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
import br.com.altamira.erp.entity.model.Material;

/**
 *
 */
@Stateless
@Path("/materials")
public class MaterialEndpoint {

    @PersistenceContext(unitName = "altamira-bpm-PU")
    private EntityManager em;
    
    @Inject 
    private MaterialDao materialDao;

    @POST
    @Consumes("application/json")
    public Response create(Material entity) {
    	
    	Material material = materialDao.find(entity);
    	
    	if (material == null) {
	    	entity.setId(null);
	
	        em.persist(entity);

	        return Response.created(
	                UriBuilder.fromResource(MaterialEndpoint.class)
	                .path(String.valueOf(entity.getId())).build())
	                .entity(entity)
	                .build();
    	}
    	
        return Response.ok(
                UriBuilder.fromResource(MaterialEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(material).build();

        
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
        TypedQuery<Material> findByIdQuery = em.createNamedQuery("Material.findById", Material.class);
        findByIdQuery.setParameter("id", id);
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
    public List<Material> listAll(
    		@QueryParam("start") Integer startPosition,
            @QueryParam("max") Integer maxResult) {
        TypedQuery<Material> findAllQuery = em.createNamedQuery("Material.findAll", Material.class);
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
    //@Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(Material entity) {
        entity = em.merge(entity);
        return Response.ok(UriBuilder.fromResource(MaterialEndpoint.class)
                .path(String.valueOf(entity.getId())).build())
                .entity(entity)
                .build();
    }
}
