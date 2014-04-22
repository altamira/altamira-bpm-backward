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
import br.com.altamira.erp.entity.model.PurchasePlanningItem;

/**
 * 
 */
@Stateless
@Path("/purchaseplanningitems")
public class PurchasePlanningItemEndpoint
{
   @PersistenceContext(unitName = "altamira-bpm-PU")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(PurchasePlanningItem entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(PurchasePlanningItemEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") long id)
   {
      PurchasePlanningItem entity = em.find(PurchasePlanningItem.class, id);
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
      TypedQuery<PurchasePlanningItem> findByIdQuery = em.createQuery("SELECT DISTINCT p FROM PurchasePlanningItem p LEFT JOIN FETCH p.supplier LEFT JOIN FETCH p.requestItem LEFT JOIN FETCH p.planning LEFT JOIN FETCH p.purchaseOrderItemSet WHERE p.id = :entityId ORDER BY p.id", PurchasePlanningItem.class);
      findByIdQuery.setParameter("entityId", id);
      PurchasePlanningItem entity;
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
   public List<PurchasePlanningItem> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<PurchasePlanningItem> findAllQuery = em.createQuery("SELECT DISTINCT p FROM PurchasePlanningItem p LEFT JOIN FETCH p.supplier LEFT JOIN FETCH p.requestItem LEFT JOIN FETCH p.planning LEFT JOIN FETCH p.purchaseOrderItemSet ORDER BY p.id", PurchasePlanningItem.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<PurchasePlanningItem> results = findAllQuery.getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(PurchasePlanningItem entity)
   {
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}