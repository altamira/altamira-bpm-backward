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
@Table(name = "PAYMENT_CONDITION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaymentCondition.findAll", query = "SELECT p FROM PaymentCondition p"),
    @NamedQuery(name = "PaymentCondition.findById", query = "SELECT p FROM PaymentCondition p WHERE p.id = :id"),
    @NamedQuery(name = "PaymentCondition.findByName", query = "SELECT p FROM PaymentCondition p WHERE p.name = :name")})
public class PaymentCondition implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NAME")
    private String name;
    @OneToMany(mappedBy = "paymentCondition", fetch = FetchType.LAZY)
    private Set<Supplier> supplierSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentCondition", fetch = FetchType.LAZY)
    private Set<PaymentConditionItem> paymentConditionItemSet;

    public PaymentCondition() {
    }

    public PaymentCondition(Long id) {
        this.id = id;
    }

    public PaymentCondition(Long id, String name) {
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

    @XmlTransient
    @JsonIgnore
    public Set<Supplier> getSupplierSet() {
        return supplierSet;
    }

    public void setSupplierSet(Set<Supplier> supplierSet) {
        this.supplierSet = supplierSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<PaymentConditionItem> getPaymentConditionItemSet() {
        return paymentConditionItemSet;
    }

    public void setPaymentConditionItemSet(Set<PaymentConditionItem> paymentConditionItemSet) {
        this.paymentConditionItemSet = paymentConditionItemSet;
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
        if (!(object instanceof PaymentCondition)) {
            return false;
        }
        PaymentCondition other = (PaymentCondition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.PaymentCondition[ id=" + id + " ]";
    }
    
}
