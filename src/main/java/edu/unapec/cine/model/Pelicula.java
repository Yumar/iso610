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
import javax.persistence.Lob;
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
@Table(name = "pelicula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pelicula.findAll", query = "SELECT p FROM Pelicula p"),
    @NamedQuery(name = "Pelicula.findByIdpelicula", query = "SELECT p FROM Pelicula p WHERE p.idpelicula = :idpelicula"),
    @NamedQuery(name = "Pelicula.findByTitulo", query = "SELECT p FROM Pelicula p WHERE p.titulo = :titulo"),
    @NamedQuery(name = "Pelicula.findByAnoProduccion", query = "SELECT p FROM Pelicula p WHERE p.anoProduccion = :anoProduccion"),
    @NamedQuery(name = "Pelicula.findByNacionalidad", query = "SELECT p FROM Pelicula p WHERE p.nacionalidad = :nacionalidad")})
public class Pelicula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idpelicula")
    private Integer idpelicula;
    @Column(name = "titulo")
    private String titulo;
    @Lob
    @Column(name = "sinopsis")
    private String sinopsis;
    @Column(name = "ano_produccion")
    @Temporal(TemporalType.DATE)
    private Date anoProduccion;
    @Column(name = "nacionalidad")
    private String nacionalidad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idpelicula")
    private Collection<Rol> rolCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idpelicula")
    private Collection<Tanda> tandaCollection;

    public Pelicula() {
    }

    public Pelicula(Integer idpelicula) {
        this.idpelicula = idpelicula;
    }

    public Integer getIdpelicula() {
        return idpelicula;
    }

    public void setIdpelicula(Integer idpelicula) {
        this.idpelicula = idpelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Date getAnoProduccion() {
        return anoProduccion;
    }

    public void setAnoProduccion(Date anoProduccion) {
        this.anoProduccion = anoProduccion;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @XmlTransient
    public Collection<Rol> getRolCollection() {
        return rolCollection;
    }

    public void setRolCollection(Collection<Rol> rolCollection) {
        this.rolCollection = rolCollection;
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
        hash += (idpelicula != null ? idpelicula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pelicula)) {
            return false;
        }
        Pelicula other = (Pelicula) object;
        if ((this.idpelicula == null && other.idpelicula != null) || (this.idpelicula != null && !this.idpelicula.equals(other.idpelicula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unapec.cine.model.Pelicula[ idpelicula=" + idpelicula + " ]";
    }
    
}
