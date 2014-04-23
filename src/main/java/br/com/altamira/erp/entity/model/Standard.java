/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "STANDARD", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Standard.findAll", query = "SELECT s FROM Standard s"),
    @NamedQuery(name = "Standard.findById", query = "SELECT s FROM Standard s WHERE s.id = :id"),
    @NamedQuery(name = "Standard.findByName", query = "SELECT s FROM Standard s WHERE s.name = :name")})
public class Standard implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "StandardSequence", sequenceName = "STANDARD_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StandardSequence")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME", columnDefinition = "nvarchar2(20)")
    private String name;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standard", fetch = FetchType.LAZY)
    private Set<MaterialStandard> materialStandardSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "standard", fetch = FetchType.LAZY)
    private Set<SupplierStandard> supplierStandardSet;

    public Standard() {
    }

    public Standard(Long id) {
        this.id = id;
    }

    public Standard(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    @JsonIgnore
    public Set<MaterialStandard> getMaterialStandardSet() {
        return materialStandardSet;
    }

    public void setMaterialStandardSet(Set<MaterialStandard> materialStandardSet) {
        this.materialStandardSet = materialStandardSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<SupplierStandard> getSupplierStandardSet() {
        return supplierStandardSet;
    }

    public void setSupplierStandardSet(Set<SupplierStandard> supplierStandardSet) {
        this.supplierStandardSet = supplierStandardSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof Standard)) {
            return false;
        }
        Standard other = (Standard) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.Standard[ id=" + id + " ]";
    }

}
