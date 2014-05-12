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
@Table(name = "LOCATION_ADDRESS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LocationAddress.findAll", query = "SELECT l FROM LocationAddress l"),
    @NamedQuery(name = "LocationAddress.findById", query = "SELECT l FROM LocationAddress l WHERE l.id = :id"),
    @NamedQuery(name = "LocationAddress.findByAddress", query = "SELECT l FROM LocationAddress l WHERE l.address = :address"),
    @NamedQuery(name = "LocationAddress.findByNumber", query = "SELECT l FROM LocationAddress l WHERE l.number = :number"),
    @NamedQuery(name = "LocationAddress.findByComplement", query = "SELECT l FROM LocationAddress l WHERE l.complement = :complement"),
    @NamedQuery(name = "LocationAddress.findByDistrict", query = "SELECT l FROM LocationAddress l WHERE l.district = :district"),
    @NamedQuery(name = "LocationAddress.findByCity", query = "SELECT l FROM LocationAddress l WHERE l.city = :city"),
    @NamedQuery(name = "LocationAddress.findByState", query = "SELECT l FROM LocationAddress l WHERE l.state = :state"),
    @NamedQuery(name = "LocationAddress.findByPostalCode", query = "SELECT l FROM LocationAddress l WHERE l.postalCode = :postalCode")})
public class LocationAddress implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ADDRESS", columnDefinition = "varchar2(100)")
    private String address;
    @Size(max = 10)
    @Column(name = "NUMBER_", columnDefinition = "varchar2(10)")
    private String number;
    @Size(max = 20)
    @Column(name = "COMPLEMENT", columnDefinition = "varchar2(20)")
    private String complement;
    @Size(max = 20)
    @Column(name = "DISTRICT", columnDefinition = "varchar2(20)")
    private String district;
    @Size(max = 20)
    @Column(name = "CITY", columnDefinition = "varchar2(20)")
    private String city;
    @Size(max = 2)
    @Column(name = "STATE", columnDefinition = "char(2)")
    private String state;
    @Size(max = 10)
    @Column(name = "POSTAL_CODE", columnDefinition = "varchar2(10)")
    private String postalCode;
    @OneToMany(mappedBy = "billingAddress", fetch = FetchType.LAZY)
    private Set<Company> companySet;
    @OneToMany(mappedBy = "invoiceAddress", fetch = FetchType.LAZY)
    private Set<Company> companySet1;
    @OneToMany(mappedBy = "locationAddress", fetch = FetchType.LAZY)
    private Set<Company> companySet2;
    @OneToMany(mappedBy = "shippingAddress", fetch = FetchType.LAZY)
    private Set<Company> companySet3;
    @OneToMany(mappedBy = "locationAddress", fetch = FetchType.LAZY)
    private Set<Supplier> supplierSet;

    public LocationAddress() {
    }

    public LocationAddress(Long id) {
        this.id = id;
    }

    public LocationAddress(Long id, String address) {
        this.id = id;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @XmlTransient
    @JsonIgnore
    public Set<Company> getCompanySet() {
        return companySet;
    }

    public void setCompanySet(Set<Company> companySet) {
        this.companySet = companySet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<Company> getCompanySet1() {
        return companySet1;
    }

    public void setCompanySet1(Set<Company> companySet1) {
        this.companySet1 = companySet1;
    }

    @XmlTransient
    @JsonIgnore
    public Set<Company> getCompanySet2() {
        return companySet2;
    }

    public void setCompanySet2(Set<Company> companySet2) {
        this.companySet2 = companySet2;
    }

    @XmlTransient
    @JsonIgnore
    public Set<Company> getCompanySet3() {
        return companySet3;
    }

    public void setCompanySet3(Set<Company> companySet3) {
        this.companySet3 = companySet3;
    }

    @XmlTransient
    @JsonIgnore
    public Set<Supplier> getSupplierSet() {
        return supplierSet;
    }

    public void setSupplierSet(Set<Supplier> supplierSet) {
        this.supplierSet = supplierSet;
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
        if (!(object instanceof LocationAddress)) {
            return false;
        }
        LocationAddress other = (LocationAddress) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.LocationAddress[ id=" + id + " ]";
    }
    
}
