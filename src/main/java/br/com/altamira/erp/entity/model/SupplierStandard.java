/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.erp.entity.model;

import java.io.Serializable;

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
@Table(name = "SUPPLIER_STANDARD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SupplierStandard.findAll", query = "SELECT s FROM SupplierStandard s"),
    @NamedQuery(name = "SupplierStandard.findBySupplier", query = "SELECT s FROM SupplierStandard s WHERE s.supplierStandardPK.supplier = :supplier"),
    @NamedQuery(name = "SupplierStandard.findByStandard", query = "SELECT s FROM SupplierStandard s WHERE s.supplierStandardPK.standard = :standard"),
    @NamedQuery(name = "SupplierStandard.findByAvailable", query = "SELECT s FROM SupplierStandard s WHERE s.available = :available")})
public class SupplierStandard implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SupplierStandardPK supplierStandardPK;
    @Basic(optional = false)
    @Column(name = "AVAILABLE")
    private Character available;
    @JoinColumn(name = "SUPPLIER", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Supplier supplier;
    @JoinColumn(name = "STANDARD", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Standard standard;

    public SupplierStandard() {
    }

    public SupplierStandard(SupplierStandardPK supplierStandardPK) {
        this.supplierStandardPK = supplierStandardPK;
    }

    public SupplierStandard(SupplierStandardPK supplierStandardPK,
            Character available) {
        this.supplierStandardPK = supplierStandardPK;
        this.available = available;
    }

    public SupplierStandard(Long supplier, Long standard) {
        this.supplierStandardPK = new SupplierStandardPK(supplier, standard);
    }

    public SupplierStandardPK getSupplierStandardPK() {
        return supplierStandardPK;
    }

    public void setSupplierStandardPK(SupplierStandardPK supplierStandardPK) {
        this.supplierStandardPK = supplierStandardPK;
    }

    public Character getAvailable() {
        return available;
    }

    public void setAvailable(Character available) {
        this.available = available;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Standard getStandard1() {
        return standard;
    }

    public void setStandard1(Standard standard) {
        this.standard = standard;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (supplierStandardPK != null ? supplierStandardPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof SupplierStandard)) {
            return false;
        }
        SupplierStandard other = (SupplierStandard) object;
        if ((this.supplierStandardPK == null && other.supplierStandardPK != null)
                || (this.supplierStandardPK != null && !this.supplierStandardPK
                .equals(other.supplierStandardPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.SupplierStandard[ supplierStandardPK="
                + supplierStandardPK + " ]";
    }

}
