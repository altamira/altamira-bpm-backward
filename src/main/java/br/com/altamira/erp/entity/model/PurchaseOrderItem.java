/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "PURCHASE_ORDER_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderItem.findAll", query = "SELECT p FROM PurchaseOrderItem p"),
    @NamedQuery(name = "PurchaseOrderItem.findById", query = "SELECT p FROM PurchaseOrderItem p WHERE p.id = :id"),
    @NamedQuery(name = "PurchaseOrderItem.findByDate", query = "SELECT p FROM PurchaseOrderItem p WHERE p.date = :date"),
    @NamedQuery(name = "PurchaseOrderItem.findByWeight", query = "SELECT p FROM PurchaseOrderItem p WHERE p.weight = :weight"),
    @NamedQuery(name = "PurchaseOrderItem.findByPrice", query = "SELECT p FROM PurchaseOrderItem p WHERE p.price = :price"),
    @NamedQuery(name = "PurchaseOrderItem.findByTax", query = "SELECT p FROM PurchaseOrderItem p WHERE p.tax = :tax")})
public class PurchaseOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "PurchaseOrderItemSequence", sequenceName = "PURCHASE_ORDER_ITEM_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PurchaseOrderItemSequence")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "\"DATE\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "TAX")
    private BigDecimal tax;
    @Column(name = "STANDARD")
    private BigDecimal standard;
    @JoinColumn(name = "PLANNING_ITEM", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PurchasePlanningItem planningItem;
    @JoinColumn(name = "PURCHASE_ORDER", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PurchaseOrder purchaseOrder;

    public PurchaseOrderItem() {
    }

    public PurchaseOrderItem(Long id) {
        this.id = id;
    }

    public PurchaseOrderItem(Long id, Date date, BigDecimal weight) {
        this.id = id;
        this.date = date;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
    
    public BigDecimal getStandard() {
        return standard;
    }

    public void setStandard(BigDecimal standard) {
        this.standard = standard;
    }

    @XmlTransient
    @JsonIgnore
    public PurchasePlanningItem getPlanningItem() {
        return planningItem;
    }

    public void setPlanningItem(PurchasePlanningItem planningItem) {
        this.planningItem = planningItem;
    }

    @XmlTransient
    @JsonIgnore
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
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof PurchaseOrderItem)) {
            return false;
        }
        PurchaseOrderItem other = (PurchaseOrderItem) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.PurchaseOrderItem[ id=" + id
                + " ]";
    }

}
