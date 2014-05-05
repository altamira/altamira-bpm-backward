/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "SUPPLIER_CONTACT_PERSON")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SupplierContactPerson.findAll", query = "SELECT s FROM SupplierContactPerson s"),
    @NamedQuery(name = "SupplierContactPerson.findById", query = "SELECT s FROM SupplierContactPerson s WHERE s.id = :id"),
    @NamedQuery(name = "SupplierContactPerson.findByDepartment", query = "SELECT s FROM SupplierContactPerson s WHERE s.department = :department")})
public class SupplierContactPerson implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Size(max = 20)
    @Column(name = "DEPARTMENT", columnDefinition = "varchar2(20)")
    private String department;
    @JoinColumn(name = "SUPPLIER", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
    @JoinColumn(name = "CONTACT_PERSON", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private ContactPerson contactPerson;

    public SupplierContactPerson() {
    }

    public SupplierContactPerson(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SupplierContactPerson)) {
            return false;
        }
        SupplierContactPerson other = (SupplierContactPerson) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.SupplierContactPerson[ id=" + id + " ]";
    }
    
}
