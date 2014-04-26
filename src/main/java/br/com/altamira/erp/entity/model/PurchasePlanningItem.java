/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
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
@Table(name = "PURCHASE_PLANNING_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchasePlanningItem.findAll", query = "SELECT p FROM PurchasePlanningItem p"),
    @NamedQuery(name = "PurchasePlanningItem.findById", query = "SELECT p FROM PurchasePlanningItem p WHERE p.id = :id"),
    @NamedQuery(name = "PurchasePlanningItem.findByDate", query = "SELECT p FROM PurchasePlanningItem p WHERE p.date = :date"),
    @NamedQuery(name = "PurchasePlanningItem.findByWeight", query = "SELECT p FROM PurchasePlanningItem p WHERE p.weight = :weight")})
public class PurchasePlanningItem implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "PlanningItemSequence", sequenceName = "PLANNING_ITEM_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PlanningItemSequence")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "\"DATE\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @JoinColumn(name = "SUPPLIER", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Supplier supplier;
    @JoinColumn(name = "REQUEST_ITEM", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RequestItem requestItem;
    @JoinColumn(name = "PLANNING", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PurchasePlanning planning;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planningItem", fetch = FetchType.LAZY)
    private Set<PurchaseOrderItem> purchaseOrderItemSet;

    public PurchasePlanningItem() {
    }

    public PurchasePlanningItem(Long id) {
        this.id = id;
    }

    public PurchasePlanningItem(Long id, Date date, BigDecimal weight) {
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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public RequestItem getRequestItem() {
        return requestItem;
    }

    public void setRequestItem(RequestItem requestItem) {
        this.requestItem = requestItem;
    }

    public PurchasePlanning getPlanning() {
        return planning;
    }

    public void setPlanning(PurchasePlanning planning) {
        this.planning = planning;
    }

    @XmlTransient
    @JsonIgnore
    public Set<PurchaseOrderItem> getPurchaseOrderItemSet() {
        return purchaseOrderItemSet;
    }

    public void setPurchaseOrderItemSet(
            Set<PurchaseOrderItem> purchaseOrderItemSet) {
        this.purchaseOrderItemSet = purchaseOrderItemSet;
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
        if (!(object instanceof PurchasePlanningItem)) {
            return false;
        }
        PurchasePlanningItem other = (PurchasePlanningItem) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.PurchasePlanningItem[ id="
                + id + " ]";
    }

}
