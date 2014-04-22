/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "MATERIAL_STANDARD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaterialStandard.findAll", query = "SELECT m FROM MaterialStandard m"),
    @NamedQuery(name = "MaterialStandard.findByMaterial", query = "SELECT m FROM MaterialStandard m WHERE m.materialStandardPK.material = :material"),
    @NamedQuery(name = "MaterialStandard.findByStandard", query = "SELECT m FROM MaterialStandard m WHERE m.materialStandardPK.standard = :standard"),
    @NamedQuery(name = "MaterialStandard.findByAccept", query = "SELECT m FROM MaterialStandard m WHERE m.accept = :accept")})
public class MaterialStandard implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected MaterialStandardPK materialStandardPK;
    @Basic(optional = false)
    @Column(name = "ACCEPT")
    private Character accept;
    @JoinColumn(name = "STANDARD", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Standard standard;
    @JoinColumn(name = "MATERIAL", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Material material;

    public MaterialStandard() {
    }

    public MaterialStandard(MaterialStandardPK materialStandardPK) {
        this.materialStandardPK = materialStandardPK;
    }

    public MaterialStandard(MaterialStandardPK materialStandardPK, Character accept) {
        this.materialStandardPK = materialStandardPK;
        this.accept = accept;
    }

    public MaterialStandard(long material, long standard) {
        this.materialStandardPK = new MaterialStandardPK(material, standard);
    }

    public MaterialStandardPK getMaterialStandardPK() {
        return materialStandardPK;
    }

    public void setMaterialStandardPK(MaterialStandardPK materialStandardPK) {
        this.materialStandardPK = materialStandardPK;
    }

    public Character getAccept() {
        return accept;
    }

    public void setAccept(Character accept) {
        this.accept = accept;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public Material getMaterial1() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (materialStandardPK != null ? materialStandardPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaterialStandard)) {
            return false;
        }
        MaterialStandard other = (MaterialStandard) object;
        if ((this.materialStandardPK == null && other.materialStandardPK != null) || (this.materialStandardPK != null && !this.materialStandardPK.equals(other.materialStandardPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.MaterialStandard[ materialStandardPK=" + materialStandardPK + " ]";
    }
    
}
