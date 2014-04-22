package br.com.altamira.erp.entity.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import br.com.altamira.erp.entity.model.Request;

/**
 * 
 */
@Stateless
@Path("/requests")
public class RequestEndpoint
{
   @PersistenceContext(unitName = "altamira-bpm-PU")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(Request entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(RequestEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") long id)
   {
      Request entity = em.find(Request.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") long id)
   {
      TypedQuery<Request> findByIdQuery = em.createQuery("SELECT DISTINCT r FROM Request r LEFT JOIN FETCH r.requestItemSet LEFT JOIN FETCH r.quotationRequest WHERE r.id = :entityId ORDER BY r.id", Request.class);
      findByIdQuery.setParameter("entityId", id);
      Request entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.ok(entity).build();
   }

   @GET
   @Produces("application/json")
   public List<Request> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<Request> findAllQuery = em.createQuery("SELECT DISTINCT r FROM Request r LEFT JOIN FETCH r.requestItemSet LEFT JOIN FETCH r.quotationRequest ORDER BY r.id", Request.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<Request> results = findAllQuery.getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(Request entity)
   {
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}