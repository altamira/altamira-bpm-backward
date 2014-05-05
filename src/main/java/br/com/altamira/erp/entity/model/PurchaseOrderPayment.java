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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "PURCHASE_ORDER_PAYMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderPayment.findAll", query = "SELECT p FROM PurchaseOrderPayment p"),
    @NamedQuery(name = "PurchaseOrderPayment.findById", query = "SELECT p FROM PurchaseOrderPayment p WHERE p.id = :id"),
    @NamedQuery(name = "PurchaseOrderPayment.findByPeriod", query = "SELECT p FROM PurchaseOrderPayment p WHERE p.period = :period"),
    @NamedQuery(name = "PurchaseOrderPayment.findByPercentage", query = "SELECT p FROM PurchaseOrderPayment p WHERE p.percentage = :percentage")})
public class PurchaseOrderPayment implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERIOD")
    private short period;
    @Column(name = "PERCENTAGE")
    private BigDecimal percentage;
    @JoinColumn(name = "PURCHASE_ORDER", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PurchaseOrder purchaseOrder;

    public PurchaseOrderPayment() {
    }

    public PurchaseOrderPayment(BigDecimal id) {
        this.id = id;
    }

    public PurchaseOrderPayment(BigDecimal id, short period) {
        this.id = id;
        this.period = period;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public short getPeriod() {
        return period;
    }

    public void setPeriod(short period) {
        this.period = period;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
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
        if (!(object instanceof PurchaseOrderPayment)) {
            return false;
        }
        PurchaseOrderPayment other = (PurchaseOrderPayment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.PurchaseOrderPayment[ id=" + id + " ]";
    }
    
}
