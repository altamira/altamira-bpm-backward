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
import br.com.altamira.erp.entity.model.PurchaseOrderItem;

/**
 * 
 */
@Stateless
@Path("/purchaseorderitems")
public class PurchaseOrderItemEndpoint
{
   @PersistenceContext(unitName = "altamira-bpm-PU")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(PurchaseOrderItem entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(PurchaseOrderItemEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") long id)
   {
      PurchaseOrderItem entity = em.find(PurchaseOrderItem.class, id);
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
      TypedQuery<PurchaseOrderItem> findByIdQuery = em.createQuery("SELECT DISTINCT p FROM PurchaseOrderItem p LEFT JOIN FETCH p.planningItem LEFT JOIN FETCH p.purchaseOrder WHERE p.id = :entityId ORDER BY p.id", PurchaseOrderItem.class);
      findByIdQuery.setParameter("entityId", id);
      PurchaseOrderItem entity;
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
   public List<PurchaseOrderItem> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<PurchaseOrderItem> findAllQuery = em.createQuery("SELECT DISTINCT p FROM PurchaseOrderItem p LEFT JOIN FETCH p.planningItem LEFT JOIN FETCH p.purchaseOrder ORDER BY p.id", PurchaseOrderItem.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<PurchaseOrderItem> results = findAllQuery.getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(PurchaseOrderItem entity)
   {
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}