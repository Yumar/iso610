/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author yumarx
 */
@Entity
@Table(name = "artista")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Artista.findAll", query = "SELECT a FROM Artista a"),
    @NamedQuery(name = "Artista.findByIdartista", query = "SELECT a FROM Artista a WHERE a.idartista = :idartista"),
    @NamedQuery(name = "Artista.findByNombre", query = "SELECT a FROM Artista a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Artista.findByFechaNacimiento", query = "SELECT a FROM Artista a WHERE a.fechaNacimiento = :fechaNacimiento")})
public class Artista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idartista")
    private Integer idartista;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idartista")
    private Collection<Rol> rolCollection;

    public Artista() {
    }

    public Artista(Integer idartista) {
        this.idartista = idartista;
    }

    public Integer getIdartista() {
        return idartista;
    }

    public void setIdartista(Integer idartista) {
        this.idartista = idartista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @XmlTransient
    public Collection<Rol> getRolCollection() {
        return rolCollection;
    }

    public void setRolCollection(Collection<Rol> rolCollection) {
        this.rolCollection = rolCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idartista != null ? idartista.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artista)) {
            return false;
        }
        Artista other = (Artista) object;
        if ((this.idartista == null && other.idartista != null) || (this.idartista != null && !this.idartista.equals(other.idartista))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unapec.cine.model.Artista[ idartista=" + idartista + " ]";
    }
    
}
