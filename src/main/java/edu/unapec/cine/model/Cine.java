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
@Table(name = "cine")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cine.findAll", query = "SELECT c FROM Cine c"),
    @NamedQuery(name = "Cine.findByIdcine", query = "SELECT c FROM Cine c WHERE c.idcine = :idcine"),
    @NamedQuery(name = "Cine.findByNombre", query = "SELECT c FROM Cine c WHERE c.nombre = :nombre")})
public class Cine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idcine")
    private Integer idcine;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcine")
    private Collection<Sala> salaCollection;

    public Cine() {
    }

    public Cine(Integer idcine) {
        this.idcine = idcine;
    }

    public Integer getIdcine() {
        return idcine;
    }

    public void setIdcine(Integer idcine) {
        this.idcine = idcine;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Sala> getSalaCollection() {
        return salaCollection;
    }

    public void setSalaCollection(Collection<Sala> salaCollection) {
        this.salaCollection = salaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcine != null ? idcine.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cine)) {
            return false;
        }
        Cine other = (Cine) object;
        if ((this.idcine == null && other.idcine != null) || (this.idcine != null && !this.idcine.equals(other.idcine))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unapec.cine.model.Cine[ idcine=" + idcine + " ]";
    }
    
}
