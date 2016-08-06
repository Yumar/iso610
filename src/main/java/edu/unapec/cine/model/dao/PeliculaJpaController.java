/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.model.dao;

import edu.unapec.cine.model.Pelicula;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import edu.unapec.cine.model.Rol;
import java.util.ArrayList;
import java.util.Collection;
import edu.unapec.cine.model.Tanda;
import edu.unapec.cine.model.dao.exceptions.IllegalOrphanException;
import edu.unapec.cine.model.dao.exceptions.NonexistentEntityException;
import edu.unapec.cine.model.dao.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yumarx
 */
public class PeliculaJpaController implements Serializable {

    public PeliculaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pelicula pelicula) throws PreexistingEntityException, Exception {
        if (pelicula.getRolCollection() == null) {
            pelicula.setRolCollection(new ArrayList<Rol>());
        }
        if (pelicula.getTandaCollection() == null) {
            pelicula.setTandaCollection(new ArrayList<Tanda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Rol> attachedRolCollection = new ArrayList<Rol>();
            for (Rol rolCollectionRolToAttach : pelicula.getRolCollection()) {
                rolCollectionRolToAttach = em.getReference(rolCollectionRolToAttach.getClass(), rolCollectionRolToAttach.getIdrol());
                attachedRolCollection.add(rolCollectionRolToAttach);
            }
            pelicula.setRolCollection(attachedRolCollection);
            Collection<Tanda> attachedTandaCollection = new ArrayList<Tanda>();
            for (Tanda tandaCollectionTandaToAttach : pelicula.getTandaCollection()) {
                tandaCollectionTandaToAttach = em.getReference(tandaCollectionTandaToAttach.getClass(), tandaCollectionTandaToAttach.getIdtanda());
                attachedTandaCollection.add(tandaCollectionTandaToAttach);
            }
            pelicula.setTandaCollection(attachedTandaCollection);
            em.persist(pelicula);
            for (Rol rolCollectionRol : pelicula.getRolCollection()) {
                Pelicula oldIdpeliculaOfRolCollectionRol = rolCollectionRol.getIdpelicula();
                rolCollectionRol.setIdpelicula(pelicula);
                rolCollectionRol = em.merge(rolCollectionRol);
                if (oldIdpeliculaOfRolCollectionRol != null) {
                    oldIdpeliculaOfRolCollectionRol.getRolCollection().remove(rolCollectionRol);
                    oldIdpeliculaOfRolCollectionRol = em.merge(oldIdpeliculaOfRolCollectionRol);
                }
            }
            for (Tanda tandaCollectionTanda : pelicula.getTandaCollection()) {
                Pelicula oldIdpeliculaOfTandaCollectionTanda = tandaCollectionTanda.getIdpelicula();
                tandaCollectionTanda.setIdpelicula(pelicula);
                tandaCollectionTanda = em.merge(tandaCollectionTanda);
                if (oldIdpeliculaOfTandaCollectionTanda != null) {
                    oldIdpeliculaOfTandaCollectionTanda.getTandaCollection().remove(tandaCollectionTanda);
                    oldIdpeliculaOfTandaCollectionTanda = em.merge(oldIdpeliculaOfTandaCollectionTanda);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPelicula(pelicula.getIdpelicula()) != null) {
                throw new PreexistingEntityException("Pelicula " + pelicula + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pelicula pelicula) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pelicula persistentPelicula = em.find(Pelicula.class, pelicula.getIdpelicula());
            Collection<Rol> rolCollectionOld = persistentPelicula.getRolCollection();
            Collection<Rol> rolCollectionNew = pelicula.getRolCollection();
            Collection<Tanda> tandaCollectionOld = persistentPelicula.getTandaCollection();
            Collection<Tanda> tandaCollectionNew = pelicula.getTandaCollection();
            List<String> illegalOrphanMessages = null;
            for (Rol rolCollectionOldRol : rolCollectionOld) {
                if (!rolCollectionNew.contains(rolCollectionOldRol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rol " + rolCollectionOldRol + " since its idpelicula field is not nullable.");
                }
            }
            for (Tanda tandaCollectionOldTanda : tandaCollectionOld) {
                if (!tandaCollectionNew.contains(tandaCollectionOldTanda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tanda " + tandaCollectionOldTanda + " since its idpelicula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Rol> attachedRolCollectionNew = new ArrayList<Rol>();
            for (Rol rolCollectionNewRolToAttach : rolCollectionNew) {
                rolCollectionNewRolToAttach = em.getReference(rolCollectionNewRolToAttach.getClass(), rolCollectionNewRolToAttach.getIdrol());
                attachedRolCollectionNew.add(rolCollectionNewRolToAttach);
            }
            rolCollectionNew = attachedRolCollectionNew;
            pelicula.setRolCollection(rolCollectionNew);
            Collection<Tanda> attachedTandaCollectionNew = new ArrayList<Tanda>();
            for (Tanda tandaCollectionNewTandaToAttach : tandaCollectionNew) {
                tandaCollectionNewTandaToAttach = em.getReference(tandaCollectionNewTandaToAttach.getClass(), tandaCollectionNewTandaToAttach.getIdtanda());
                attachedTandaCollectionNew.add(tandaCollectionNewTandaToAttach);
            }
            tandaCollectionNew = attachedTandaCollectionNew;
            pelicula.setTandaCollection(tandaCollectionNew);
            pelicula = em.merge(pelicula);
            for (Rol rolCollectionNewRol : rolCollectionNew) {
                if (!rolCollectionOld.contains(rolCollectionNewRol)) {
                    Pelicula oldIdpeliculaOfRolCollectionNewRol = rolCollectionNewRol.getIdpelicula();
                    rolCollectionNewRol.setIdpelicula(pelicula);
                    rolCollectionNewRol = em.merge(rolCollectionNewRol);
                    if (oldIdpeliculaOfRolCollectionNewRol != null && !oldIdpeliculaOfRolCollectionNewRol.equals(pelicula)) {
                        oldIdpeliculaOfRolCollectionNewRol.getRolCollection().remove(rolCollectionNewRol);
                        oldIdpeliculaOfRolCollectionNewRol = em.merge(oldIdpeliculaOfRolCollectionNewRol);
                    }
                }
            }
            for (Tanda tandaCollectionNewTanda : tandaCollectionNew) {
                if (!tandaCollectionOld.contains(tandaCollectionNewTanda)) {
                    Pelicula oldIdpeliculaOfTandaCollectionNewTanda = tandaCollectionNewTanda.getIdpelicula();
                    tandaCollectionNewTanda.setIdpelicula(pelicula);
                    tandaCollectionNewTanda = em.merge(tandaCollectionNewTanda);
                    if (oldIdpeliculaOfTandaCollectionNewTanda != null && !oldIdpeliculaOfTandaCollectionNewTanda.equals(pelicula)) {
                        oldIdpeliculaOfTandaCollectionNewTanda.getTandaCollection().remove(tandaCollectionNewTanda);
                        oldIdpeliculaOfTandaCollectionNewTanda = em.merge(oldIdpeliculaOfTandaCollectionNewTanda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pelicula.getIdpelicula();
                if (findPelicula(id) == null) {
                    throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.");
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
            Pelicula pelicula;
            try {
                pelicula = em.getReference(Pelicula.class, id);
                pelicula.getIdpelicula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Rol> rolCollectionOrphanCheck = pelicula.getRolCollection();
            for (Rol rolCollectionOrphanCheckRol : rolCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pelicula (" + pelicula + ") cannot be destroyed since the Rol " + rolCollectionOrphanCheckRol + " in its rolCollection field has a non-nullable idpelicula field.");
            }
            Collection<Tanda> tandaCollectionOrphanCheck = pelicula.getTandaCollection();
            for (Tanda tandaCollectionOrphanCheckTanda : tandaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pelicula (" + pelicula + ") cannot be destroyed since the Tanda " + tandaCollectionOrphanCheckTanda + " in its tandaCollection field has a non-nullable idpelicula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pelicula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pelicula> findPeliculaEntities() {
        return findPeliculaEntities(true, -1, -1);
    }

    public List<Pelicula> findPeliculaEntities(int maxResults, int firstResult) {
        return findPeliculaEntities(false, maxResults, firstResult);
    }

    private List<Pelicula> findPeliculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pelicula.class));
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

    public Pelicula findPelicula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pelicula.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeliculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pelicula> rt = cq.from(Pelicula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
