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
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "CONTACT_PERSON_MAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactPersonMail.findAll", query = "SELECT c FROM ContactPersonMail c"),
    @NamedQuery(name = "ContactPersonMail.findById", query = "SELECT c FROM ContactPersonMail c WHERE c.id = :id"),
    @NamedQuery(name = "ContactPersonMail.findByAddress", query = "SELECT c FROM ContactPersonMail c WHERE c.address = :address")})
public class ContactPersonMail implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "ADDRESS", columnDefinition = "varchar2(200)")
    private String address;
    @JoinColumn(name = "CONTACT_PERSON", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ContactPerson contactPerson;

    public ContactPersonMail() {
    }

    public ContactPersonMail(BigDecimal id) {
        this.id = id;
    }

    public ContactPersonMail(BigDecimal id, String address) {
        this.id = id;
        this.address = address;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlTransient
    @JsonIgnore
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
        if (!(object instanceof ContactPersonMail)) {
            return false;
        }
        ContactPersonMail other = (ContactPersonMail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.ContactPersonMail[ id=" + id + " ]";
    }
    
}
