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
@Table(name = "CONTACT_PERSON_FONE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactPersonFone.findAll", query = "SELECT c FROM ContactPersonFone c"),
    @NamedQuery(name = "ContactPersonFone.findById", query = "SELECT c FROM ContactPersonFone c WHERE c.id = :id"),
    @NamedQuery(name = "ContactPersonFone.findByAreaCode", query = "SELECT c FROM ContactPersonFone c WHERE c.areaCode = :areaCode"),
    @NamedQuery(name = "ContactPersonFone.findByPrefix", query = "SELECT c FROM ContactPersonFone c WHERE c.prefix = :prefix"),
    @NamedQuery(name = "ContactPersonFone.findByNumber", query = "SELECT c FROM ContactPersonFone c WHERE c.number = :number"),
    @NamedQuery(name = "ContactPersonFone.findByType", query = "SELECT c FROM ContactPersonFone c WHERE c.type = :type"),
    @NamedQuery(name = "ContactPersonFone.findByCountryCode", query = "SELECT c FROM ContactPersonFone c WHERE c.countryCode = :countryCode"),
    @NamedQuery(name = "ContactPersonFone.findByExtension", query = "SELECT c FROM ContactPersonFone c WHERE c.extension = :extension")})
public class ContactPersonFone implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "AREA_CODE")
    private Integer areaCode;
    @Column(name = "PREFIX")
    private Integer prefix;
    @Column(name = "NUMBER_")
    private Short number;
    @Size(max = 5)
    @Column(name = "TYPE", columnDefinition = "char(5)")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COUNTRY_CODE")
    private short countryCode;
    @Size(max = 10)
    @Column(name = "EXTENSION", columnDefinition = "nvarchar2(10)")
    private String extension;
    @JoinColumn(name = "CONTACT_PERSON", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ContactPerson contactPerson;

    public ContactPersonFone() {
    }

    public ContactPersonFone(BigDecimal id) {
        this.id = id;
    }

    public ContactPersonFone(BigDecimal id, short countryCode) {
        this.id = id;
        this.countryCode = countryCode;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getPrefix() {
        return prefix;
    }

    public void setPrefix(Integer prefix) {
        this.prefix = prefix;
    }

    public Short getNumber() {
        return number;
    }

    public void setNumber(Short number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public short getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(short countryCode) {
        this.countryCode = countryCode;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
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
        if (!(object instanceof ContactPersonFone)) {
            return false;
        }
        ContactPersonFone other = (ContactPersonFone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.ContactPersonFone[ id=" + id + " ]";
    }
    
}
