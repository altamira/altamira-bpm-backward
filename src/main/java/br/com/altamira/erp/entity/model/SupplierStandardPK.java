/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.erp.entity.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Alessandro
 */
@Embeddable
public class SupplierStandardPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "SUPPLIER")
    private Long supplier;
    @Basic(optional = false)
    @Column(name = "STANDARD")
    private Long standard;

    public SupplierStandardPK() {
    }

    public SupplierStandardPK(Long supplier, Long standard) {
        this.supplier = supplier;
        this.standard = standard;
    }

    public Long getSupplier() {
        return supplier;
    }

    public void setSupplier(Long supplier) {
        this.supplier = supplier;
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
        hash += (supplier != null ? supplier.hashCode() : 0);
        hash += (standard != null ? standard.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof SupplierStandardPK)) {
            return false;
        }
        SupplierStandardPK other = (SupplierStandardPK) object;
        if ((this.supplier == null && other.supplier != null)
                || (this.supplier != null && !this.supplier
                .equals(other.supplier))) {
            return false;
        }
        if ((this.standard == null && other.standard != null)
                || (this.standard != null && !this.standard
                .equals(other.standard))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.SupplierStandardPK[ supplier="
                + supplier + ", standard=" + standard + " ]";
    }

}
