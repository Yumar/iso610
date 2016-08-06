/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.model.dao;

import edu.unapec.cine.model.Cine;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import edu.unapec.cine.model.Sala;
import edu.unapec.cine.model.dao.exceptions.IllegalOrphanException;
import edu.unapec.cine.model.dao.exceptions.NonexistentEntityException;
import edu.unapec.cine.model.dao.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yumarx
 */
public class CineJpaController implements Serializable {

    public CineJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cine cine) throws PreexistingEntityException, Exception {
        if (cine.getSalaCollection() == null) {
            cine.setSalaCollection(new ArrayList<Sala>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Sala> attachedSalaCollection = new ArrayList<Sala>();
            for (Sala salaCollectionSalaToAttach : cine.getSalaCollection()) {
                salaCollectionSalaToAttach = em.getReference(salaCollectionSalaToAttach.getClass(), salaCollectionSalaToAttach.getIdsala());
                attachedSalaCollection.add(salaCollectionSalaToAttach);
            }
            cine.setSalaCollection(attachedSalaCollection);
            em.persist(cine);
            for (Sala salaCollectionSala : cine.getSalaCollection()) {
                Cine oldIdcineOfSalaCollectionSala = salaCollectionSala.getIdcine();
                salaCollectionSala.setIdcine(cine);
                salaCollectionSala = em.merge(salaCollectionSala);
                if (oldIdcineOfSalaCollectionSala != null) {
                    oldIdcineOfSalaCollectionSala.getSalaCollection().remove(salaCollectionSala);
                    oldIdcineOfSalaCollectionSala = em.merge(oldIdcineOfSalaCollectionSala);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCine(cine.getIdcine()) != null) {
                throw new PreexistingEntityException("Cine " + cine + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cine cine) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cine persistentCine = em.find(Cine.class, cine.getIdcine());
            Collection<Sala> salaCollectionOld = persistentCine.getSalaCollection();
            Collection<Sala> salaCollectionNew = cine.getSalaCollection();
            List<String> illegalOrphanMessages = null;
            for (Sala salaCollectionOldSala : salaCollectionOld) {
                if (!salaCollectionNew.contains(salaCollectionOldSala)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sala " + salaCollectionOldSala + " since its idcine field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Sala> attachedSalaCollectionNew = new ArrayList<Sala>();
            for (Sala salaCollectionNewSalaToAttach : salaCollectionNew) {
                salaCollectionNewSalaToAttach = em.getReference(salaCollectionNewSalaToAttach.getClass(), salaCollectionNewSalaToAttach.getIdsala());
                attachedSalaCollectionNew.add(salaCollectionNewSalaToAttach);
            }
            salaCollectionNew = attachedSalaCollectionNew;
            cine.setSalaCollection(salaCollectionNew);
            cine = em.merge(cine);
            for (Sala salaCollectionNewSala : salaCollectionNew) {
                if (!salaCollectionOld.contains(salaCollectionNewSala)) {
                    Cine oldIdcineOfSalaCollectionNewSala = salaCollectionNewSala.getIdcine();
                    salaCollectionNewSala.setIdcine(cine);
                    salaCollectionNewSala = em.merge(salaCollectionNewSala);
                    if (oldIdcineOfSalaCollectionNewSala != null && !oldIdcineOfSalaCollectionNewSala.equals(cine)) {
                        oldIdcineOfSalaCollectionNewSala.getSalaCollection().remove(salaCollectionNewSala);
                        oldIdcineOfSalaCollectionNewSala = em.merge(oldIdcineOfSalaCollectionNewSala);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cine.getIdcine();
                if (findCine(id) == null) {
                    throw new NonexistentEntityException("The cine with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cine cine;
            try {
                cine = em.getReference(Cine.class, id);
                cine.getIdcine();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cine with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Sala> salaCollectionOrphanCheck = cine.getSalaCollection();
            for (Sala salaCollectionOrphanCheckSala : salaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cine (" + cine + ") cannot be destroyed since the Sala " + salaCollectionOrphanCheckSala + " in its salaCollection field has a non-nullable idcine field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cine);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cine> findCineEntities() {
        return findCineEntities(true, -1, -1);
    }

    public List<Cine> findCineEntities(int maxResults, int firstResult) {
        return findCineEntities(false, maxResults, firstResult);
    }

    private List<Cine> findCineEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cine.class));
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

    public Cine findCine(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cine.class, id);
        } finally {
            em.close();
        }
    }

    public int getCineCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cine> rt = cq.from(Cine.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
