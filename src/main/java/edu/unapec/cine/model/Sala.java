/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author yumarx
 */
@Entity
@Table(name = "sala")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sala.findAll", query = "SELECT s FROM Sala s"),
    @NamedQuery(name = "Sala.findByIdsala", query = "SELECT s FROM Sala s WHERE s.idsala = :idsala"),
    @NamedQuery(name = "Sala.findByNombre", query = "SELECT s FROM Sala s WHERE s.nombre = :nombre")})
public class Sala implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idsala")
    private Integer idsala;
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "idcine", referencedColumnName = "idcine")
    @ManyToOne(optional = false)
    private Cine idcine;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idsala")
    private Collection<Tanda> tandaCollection;

    public Sala() {
    }

    public Sala(Integer idsala) {
        this.idsala = idsala;
    }

    public Integer getIdsala() {
        return idsala;
    }

    public void setIdsala(Integer idsala) {
        this.idsala = idsala;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Cine getIdcine() {
        return idcine;
    }

    public void setIdcine(Cine idcine) {
        this.idcine = idcine;
    }

    @XmlTransient
    public Collection<Tanda> getTandaCollection() {
        return tandaCollection;
    }

    public void setTandaCollection(Collection<Tanda> tandaCollection) {
        this.tandaCollection = tandaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsala != null ? idsala.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sala)) {
            return false;
        }
        Sala other = (Sala) object;
        if ((this.idsala == null && other.idsala != null) || (this.idsala != null && !this.idsala.equals(other.idsala))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unapec.cine.model.Sala[ idsala=" + idsala + " ]";
    }
    
}
