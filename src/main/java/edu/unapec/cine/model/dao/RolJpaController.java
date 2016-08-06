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
import edu.unapec.cine.model.Artista;
import edu.unapec.cine.model.Pelicula;
import edu.unapec.cine.model.Rol;
import edu.unapec.cine.model.dao.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author yumarx
 */
public class RolJpaController implements Serializable {

    public RolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Artista idartista = rol.getIdartista();
            if (idartista != null) {
                idartista = em.getReference(idartista.getClass(), idartista.getIdartista());
                rol.setIdartista(idartista);
            }
            Pelicula idpelicula = rol.getIdpelicula();
            if (idpelicula != null) {
                idpelicula = em.getReference(idpelicula.getClass(), idpelicula.getIdpelicula());
                rol.setIdpelicula(idpelicula);
            }
            em.persist(rol);
            if (idartista != null) {
                idartista.getRolCollection().add(rol);
                idartista = em.merge(idartista);
            }
            if (idpelicula != null) {
                idpelicula.getRolCollection().add(rol);
                idpelicula = em.merge(idpelicula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol persistentRol = em.find(Rol.class, rol.getIdrol());
            Artista idartistaOld = persistentRol.getIdartista();
            Artista idartistaNew = rol.getIdartista();
            Pelicula idpeliculaOld = persistentRol.getIdpelicula();
            Pelicula idpeliculaNew = rol.getIdpelicula();
            if (idartistaNew != null) {
                idartistaNew = em.getReference(idartistaNew.getClass(), idartistaNew.getIdartista());
                rol.setIdartista(idartistaNew);
            }
            if (idpeliculaNew != null) {
                idpeliculaNew = em.getReference(idpeliculaNew.getClass(), idpeliculaNew.getIdpelicula());
                rol.setIdpelicula(idpeliculaNew);
            }
            rol = em.merge(rol);
            if (idartistaOld != null && !idartistaOld.equals(idartistaNew)) {
                idartistaOld.getRolCollection().remove(rol);
                idartistaOld = em.merge(idartistaOld);
            }
            if (idartistaNew != null && !idartistaNew.equals(idartistaOld)) {
                idartistaNew.getRolCollection().add(rol);
                idartistaNew = em.merge(idartistaNew);
            }
            if (idpeliculaOld != null && !idpeliculaOld.equals(idpeliculaNew)) {
                idpeliculaOld.getRolCollection().remove(rol);
                idpeliculaOld = em.merge(idpeliculaOld);
            }
            if (idpeliculaNew != null && !idpeliculaNew.equals(idpeliculaOld)) {
                idpeliculaNew.getRolCollection().add(rol);
                idpeliculaNew = em.merge(idpeliculaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rol.getIdrol();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
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
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getIdrol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            Artista idartista = rol.getIdartista();
            if (idartista != null) {
                idartista.getRolCollection().remove(rol);
                idartista = em.merge(idartista);
            }
            Pelicula idpelicula = rol.getIdpelicula();
            if (idpelicula != null) {
                idpelicula.getRolCollection().remove(rol);
                idpelicula = em.merge(idpelicula);
            }
            em.remove(rol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
