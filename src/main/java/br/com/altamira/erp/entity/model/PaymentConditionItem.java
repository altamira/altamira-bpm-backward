/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "PAYMENT_CONDITION_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaymentConditionItem.findAll", query = "SELECT p FROM PaymentConditionItem p"),
    @NamedQuery(name = "PaymentConditionItem.findById", query = "SELECT p FROM PaymentConditionItem p WHERE p.id = :id"),
    @NamedQuery(name = "PaymentConditionItem.findByPeriod", query = "SELECT p FROM PaymentConditionItem p WHERE p.period = :period"),
    @NamedQuery(name = "PaymentConditionItem.findByPercentage", query = "SELECT p FROM PaymentConditionItem p WHERE p.percentage = :percentage")})
public class PaymentConditionItem implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERIOD")
    private BigInteger period;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERCENTAGE")
    private BigInteger percentage;
    @JoinColumn(name = "PAYMENT_CONDITION", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PaymentCondition paymentCondition;

    public PaymentConditionItem() {
    }

    public PaymentConditionItem(Long id) {
        this.id = id;
    }

    public PaymentConditionItem(Long id, BigInteger period, BigInteger percentage) {
        this.id = id;
        this.period = period;
        this.percentage = percentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getPeriod() {
        return period;
    }

    public void setPeriod(BigInteger period) {
        this.period = period;
    }

    public BigInteger getPercentage() {
        return percentage;
    }

    public void setPercentage(BigInteger percentage) {
        this.percentage = percentage;
    }

    public PaymentCondition getPaymentCondition() {
        return paymentCondition;
    }

    public void setPaymentCondition(PaymentCondition paymentCondition) {
        this.paymentCondition = paymentCondition;
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
        if (!(object instanceof PaymentConditionItem)) {
            return false;
        }
        PaymentConditionItem other = (PaymentConditionItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.PaymentConditionItem[ id=" + id + " ]";
    }
    
}
