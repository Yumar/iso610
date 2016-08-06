/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author yumarx
 */
@Entity
@Table(name = "tanda")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tanda.findAll", query = "SELECT t FROM Tanda t"),
    @NamedQuery(name = "Tanda.findByIdtanda", query = "SELECT t FROM Tanda t WHERE t.idtanda = :idtanda"),
    @NamedQuery(name = "Tanda.findByFecha", query = "SELECT t FROM Tanda t WHERE t.fecha = :fecha"),
    @NamedQuery(name = "Tanda.findByHoraInicio", query = "SELECT t FROM Tanda t WHERE t.horaInicio = :horaInicio"),
    @NamedQuery(name = "Tanda.findByHoraFin", query = "SELECT t FROM Tanda t WHERE t.horaFin = :horaFin"),
    @NamedQuery(name = "Tanda.findByCosto", query = "SELECT t FROM Tanda t WHERE t.costo = :costo")})
public class Tanda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idtanda")
    private Integer idtanda;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "hora_inicio")
    @Temporal(TemporalType.TIME)
    private Date horaInicio;
    @Column(name = "hora_fin")
    @Temporal(TemporalType.TIME)
    private Date horaFin;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo")
    private BigDecimal costo;
    @JoinColumn(name = "idpelicula", referencedColumnName = "idpelicula")
    @ManyToOne(optional = false)
    private Pelicula idpelicula;
    @JoinColumn(name = "idsala", referencedColumnName = "idsala")
    @ManyToOne(optional = false)
    private Sala idsala;

    public Tanda() {
    }

    public Tanda(Integer idtanda) {
        this.idtanda = idtanda;
    }

    public Integer getIdtanda() {
        return idtanda;
    }

    public void setIdtanda(Integer idtanda) {
        this.idtanda = idtanda;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Pelicula getIdpelicula() {
        return idpelicula;
    }

    public void setIdpelicula(Pelicula idpelicula) {
        this.idpelicula = idpelicula;
    }

    public Sala getIdsala() {
        return idsala;
    }

    public void setIdsala(Sala idsala) {
        this.idsala = idsala;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtanda != null ? idtanda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tanda)) {
            return false;
        }
        Tanda other = (Tanda) object;
        if ((this.idtanda == null && other.idtanda != null) || (this.idtanda != null && !this.idtanda.equals(other.idtanda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.unapec.cine.model.Tanda[ idtanda=" + idtanda + " ]";
    }
    
}
