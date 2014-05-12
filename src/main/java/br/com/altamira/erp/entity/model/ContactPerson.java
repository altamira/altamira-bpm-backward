/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "CONTACT_PERSON")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactPerson.findAll", query = "SELECT c FROM ContactPerson c"),
    @NamedQuery(name = "ContactPerson.findById", query = "SELECT c FROM ContactPerson c WHERE c.id = :id"),
    @NamedQuery(name = "ContactPerson.findByFirstName", query = "SELECT c FROM ContactPerson c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "ContactPerson.findByLastName", query = "SELECT c FROM ContactPerson c WHERE c.lastName = :lastName")})
public class ContactPerson implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "FIRST_NAME", columnDefinition = "nvarchar2(30)")
    private String firstName;
    @Size(max = 30)
    @Column(name = "LAST_NAME", columnDefinition = "nvarchar2(30)")
    private String lastName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactPerson", fetch = FetchType.EAGER)
    private Set<ContactPersonFone> contactPersonFone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactPerson", fetch = FetchType.EAGER)
    private Set<ContactPersonMail> contactPersonMail;
    @OneToMany(mappedBy = "contactPerson", fetch = FetchType.LAZY)
    private Set<SupplierContactPerson> supplierContactPerson;

    public ContactPerson() {
    }

    public ContactPerson(Long id) {
        this.id = id;
    }

    public ContactPerson(Long id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @XmlTransient
    //@JsonIgnore
    public Set<ContactPersonFone> getContactPersonFone() {
        return contactPersonFone;
    }

    public void setContactPersonFone(Set<ContactPersonFone> contactPersonFone) {
        this.contactPersonFone = contactPersonFone;
    }

    @XmlTransient
    //@JsonIgnore
    public Set<ContactPersonMail> getContactPersonMail() {
        return contactPersonMail;
    }

    public void setContactPersonMail(Set<ContactPersonMail> contactPersonMail) {
        this.contactPersonMail = contactPersonMail;
    }

    @XmlTransient
    @JsonIgnore
    public Set<SupplierContactPerson> getSupplierContactPerson() {
        return supplierContactPerson;
    }

    public void setSupplierContactPerson(Set<SupplierContactPerson> supplierContactPerson) {
        this.supplierContactPerson = supplierContactPerson;
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
        if (!(object instanceof ContactPerson)) {
            return false;
        }
        ContactPerson other = (ContactPerson) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.ContactPerson[ id=" + id + " ]";
    }
    
}
