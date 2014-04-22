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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "SUPPLIER_CONTACT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SupplierContact.findAll", query = "SELECT s FROM SupplierContact s"),
    @NamedQuery(name = "SupplierContact.findById", query = "SELECT s FROM SupplierContact s WHERE s.id = :id"),
    @NamedQuery(name = "SupplierContact.findByName", query = "SELECT s FROM SupplierContact s WHERE s.name = :name"),
    @NamedQuery(name = "SupplierContact.findByMailAddress", query = "SELECT s FROM SupplierContact s WHERE s.mailAddress = :mailAddress")})
public class SupplierContact implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME", columnDefinition="nvarchar2(100)")
    private String name;
    @Basic(optional = false)
    @Column(name = "MAIL_ADDRESS", columnDefinition="nvarchar2(150)")
    private String mailAddress;
    @JoinColumn(name = "SUPPLIER", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Supplier supplier;

    public SupplierContact() {
    }

    public SupplierContact(Long id) {
        this.id = id;
    }

    public SupplierContact(Long id, String name, String mailAddress) {
        this.id = id;
        this.name = name;
        this.mailAddress = mailAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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
        if (!(object instanceof SupplierContact)) {
            return false;
        }
        SupplierContact other = (SupplierContact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.SupplierContact[ id=" + id + " ]";
    }
    
}
