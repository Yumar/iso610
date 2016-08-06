/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.model.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import edu.unapec.cine.model.Pelicula;
import edu.unapec.cine.model.Sala;
import edu.unapec.cine.model.Tanda;
import edu.unapec.cine.model.dao.exceptions.NonexistentEntityException;
import edu.unapec.cine.model.dao.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yumarx
 */
public class TandaJpaController implements Serializable {

    public TandaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tanda tanda) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pelicula idpelicula = tanda.getIdpelicula();
            if (idpelicula != null) {
                idpelicula = em.getReference(idpelicula.getClass(), idpelicula.getIdpelicula());
                tanda.setIdpelicula(idpelicula);
            }
            Sala idsala = tanda.getIdsala();
            if (idsala != null) {
                idsala = em.getReference(idsala.getClass(), idsala.getIdsala());
                tanda.setIdsala(idsala);
            }
            em.persist(tanda);
            if (idpelicula != null) {
                idpelicula.getTandaCollection().add(tanda);
                idpelicula = em.merge(idpelicula);
            }
            if (idsala != null) {
                idsala.getTandaCollection().add(tanda);
                idsala = em.merge(idsala);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTanda(tanda.getIdtanda()) != null) {
                throw new PreexistingEntityException("Tanda " + tanda + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tanda tanda) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tanda persistentTanda = em.find(Tanda.class, tanda.getIdtanda());
            Pelicula idpeliculaOld = persistentTanda.getIdpelicula();
            Pelicula idpeliculaNew = tanda.getIdpelicula();
            Sala idsalaOld = persistentTanda.getIdsala();
            Sala idsalaNew = tanda.getIdsala();
            if (idpeliculaNew != null) {
                idpeliculaNew = em.getReference(idpeliculaNew.getClass(), idpeliculaNew.getIdpelicula());
                tanda.setIdpelicula(idpeliculaNew);
            }
            if (idsalaNew != null) {
                idsalaNew = em.getReference(idsalaNew.getClass(), idsalaNew.getIdsala());
                tanda.setIdsala(idsalaNew);
            }
            tanda = em.merge(tanda);
            if (idpeliculaOld != null && !idpeliculaOld.equals(idpeliculaNew)) {
                idpeliculaOld.getTandaCollection().remove(tanda);
                idpeliculaOld = em.merge(idpeliculaOld);
            }
            if (idpeliculaNew != null && !idpeliculaNew.equals(idpeliculaOld)) {
                idpeliculaNew.getTandaCollection().add(tanda);
                idpeliculaNew = em.merge(idpeliculaNew);
            }
            if (idsalaOld != null && !idsalaOld.equals(idsalaNew)) {
                idsalaOld.getTandaCollection().remove(tanda);
                idsalaOld = em.merge(idsalaOld);
            }
            if (idsalaNew != null && !idsalaNew.equals(idsalaOld)) {
                idsalaNew.getTandaCollection().add(tanda);
                idsalaNew = em.merge(idsalaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tanda.getIdtanda();
                if (findTanda(id) == null) {
                    throw new NonexistentEntityException("The tanda with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tanda tanda;
            try {
                tanda = em.getReference(Tanda.class, id);
                tanda.getIdtanda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tanda with id " + id + " no longer exists.", enfe);
            }
            Pelicula idpelicula = tanda.getIdpelicula();
            if (idpelicula != null) {
                idpelicula.getTandaCollection().remove(tanda);
                idpelicula = em.merge(idpelicula);
            }
            Sala idsala = tanda.getIdsala();
            if (idsala != null) {
                idsala.getTandaCollection().remove(tanda);
                idsala = em.merge(idsala);
            }
            em.remove(tanda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tanda> findTandaEntities() {
        return findTandaEntities(true, -1, -1);
    }

    public List<Tanda> findTandaEntities(int maxResults, int firstResult) {
        return findTandaEntities(false, maxResults, firstResult);
    }

    private List<Tanda> findTandaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tanda.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tanda findTanda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tanda.class, id);
        } finally {
            em.close();
        }
    }

    public int getTandaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tanda> rt = cq.from(Tanda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
