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
import javax.persistence.Embeddable;

/**
 *
 * @author Alessandro
 */
@Embeddable
public class MaterialStandardPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "MATERIAL")
    private Long material;
    @Basic(optional = false)
    @Column(name = "STANDARD")
    private Long standard;

    public MaterialStandardPK() {
    }

    public MaterialStandardPK(Long material, Long standard) {
        this.material = material;
        this.standard = standard;
    }

    public Long getMaterial() {
        return material;
    }

    public void setMaterial(Long material) {
        this.material = material;
    }

    public Long getStandard() {
        return standard;
    }

    public void setStandard(Long standard) {
        this.standard = standard;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (material != null ? material.hashCode() : 0);
        hash += (standard != null ? standard.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MaterialStandardPK)) {
            return false;
        }
        MaterialStandardPK other = (MaterialStandardPK) object;
        if ((this.material == null && other.material != null) || (this.material != null && !this.material.equals(other.material))) {
            return false;
        }
        if ((this.standard == null && other.standard != null) || (this.standard != null && !this.standard.equals(other.standard))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.MaterialStandardPK[ material=" + material + ", standard=" + standard + " ]";
    }
    
}
