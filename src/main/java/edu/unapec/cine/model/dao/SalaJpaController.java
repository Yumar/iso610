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
import edu.unapec.cine.model.Cine;
import edu.unapec.cine.model.Sala;
import edu.unapec.cine.model.Tanda;
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
public class SalaJpaController implements Serializable {

    public SalaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sala sala) throws PreexistingEntityException, Exception {
        if (sala.getTandaCollection() == null) {
            sala.setTandaCollection(new ArrayList<Tanda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cine idcine = sala.getIdcine();
            if (idcine != null) {
                idcine = em.getReference(idcine.getClass(), idcine.getIdcine());
                sala.setIdcine(idcine);
            }
            Collection<Tanda> attachedTandaCollection = new ArrayList<Tanda>();
            for (Tanda tandaCollectionTandaToAttach : sala.getTandaCollection()) {
                tandaCollectionTandaToAttach = em.getReference(tandaCollectionTandaToAttach.getClass(), tandaCollectionTandaToAttach.getIdtanda());
                attachedTandaCollection.add(tandaCollectionTandaToAttach);
            }
            sala.setTandaCollection(attachedTandaCollection);
            em.persist(sala);
            if (idcine != null) {
                idcine.getSalaCollection().add(sala);
                idcine = em.merge(idcine);
            }
            for (Tanda tandaCollectionTanda : sala.getTandaCollection()) {
                Sala oldIdsalaOfTandaCollectionTanda = tandaCollectionTanda.getIdsala();
                tandaCollectionTanda.setIdsala(sala);
                tandaCollectionTanda = em.merge(tandaCollectionTanda);
                if (oldIdsalaOfTandaCollectionTanda != null) {
                    oldIdsalaOfTandaCollectionTanda.getTandaCollection().remove(tandaCollectionTanda);
                    oldIdsalaOfTandaCollectionTanda = em.merge(oldIdsalaOfTandaCollectionTanda);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSala(sala.getIdsala()) != null) {
                throw new PreexistingEntityException("Sala " + sala + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sala sala) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sala persistentSala = em.find(Sala.class, sala.getIdsala());
            Cine idcineOld = persistentSala.getIdcine();
            Cine idcineNew = sala.getIdcine();
            Collection<Tanda> tandaCollectionOld = persistentSala.getTandaCollection();
            Collection<Tanda> tandaCollectionNew = sala.getTandaCollection();
            List<String> illegalOrphanMessages = null;
            for (Tanda tandaCollectionOldTanda : tandaCollectionOld) {
                if (!tandaCollectionNew.contains(tandaCollectionOldTanda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tanda " + tandaCollectionOldTanda + " since its idsala field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idcineNew != null) {
                idcineNew = em.getReference(idcineNew.getClass(), idcineNew.getIdcine());
                sala.setIdcine(idcineNew);
            }
            Collection<Tanda> attachedTandaCollectionNew = new ArrayList<Tanda>();
            for (Tanda tandaCollectionNewTandaToAttach : tandaCollectionNew) {
                tandaCollectionNewTandaToAttach = em.getReference(tandaCollectionNewTandaToAttach.getClass(), tandaCollectionNewTandaToAttach.getIdtanda());
                attachedTandaCollectionNew.add(tandaCollectionNewTandaToAttach);
            }
            tandaCollectionNew = attachedTandaCollectionNew;
            sala.setTandaCollection(tandaCollectionNew);
            sala = em.merge(sala);
            if (idcineOld != null && !idcineOld.equals(idcineNew)) {
                idcineOld.getSalaCollection().remove(sala);
                idcineOld = em.merge(idcineOld);
            }
            if (idcineNew != null && !idcineNew.equals(idcineOld)) {
                idcineNew.getSalaCollection().add(sala);
                idcineNew = em.merge(idcineNew);
            }
            for (Tanda tandaCollectionNewTanda : tandaCollectionNew) {
                if (!tandaCollectionOld.contains(tandaCollectionNewTanda)) {
                    Sala oldIdsalaOfTandaCollectionNewTanda = tandaCollectionNewTanda.getIdsala();
                    tandaCollectionNewTanda.setIdsala(sala);
                    tandaCollectionNewTanda = em.merge(tandaCollectionNewTanda);
                    if (oldIdsalaOfTandaCollectionNewTanda != null && !oldIdsalaOfTandaCollectionNewTanda.equals(sala)) {
                        oldIdsalaOfTandaCollectionNewTanda.getTandaCollection().remove(tandaCollectionNewTanda);
                        oldIdsalaOfTandaCollectionNewTanda = em.merge(oldIdsalaOfTandaCollectionNewTanda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sala.getIdsala();
                if (findSala(id) == null) {
                    throw new NonexistentEntityException("The sala with id " + id + " no longer exists.");
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
            Sala sala;
            try {
                sala = em.getReference(Sala.class, id);
                sala.getIdsala();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sala with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Tanda> tandaCollectionOrphanCheck = sala.getTandaCollection();
            for (Tanda tandaCollectionOrphanCheckTanda : tandaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sala (" + sala + ") cannot be destroyed since the Tanda " + tandaCollectionOrphanCheckTanda + " in its tandaCollection field has a non-nullable idsala field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cine idcine = sala.getIdcine();
            if (idcine != null) {
                idcine.getSalaCollection().remove(sala);
                idcine = em.merge(idcine);
            }
            em.remove(sala);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sala> findSalaEntities() {
        return findSalaEntities(true, -1, -1);
    }

    public List<Sala> findSalaEntities(int maxResults, int firstResult) {
        return findSalaEntities(false, maxResults, firstResult);
    }

    private List<Sala> findSalaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sala.class));
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

    public Sala findSala(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sala.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sala> rt = cq.from(Sala.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
