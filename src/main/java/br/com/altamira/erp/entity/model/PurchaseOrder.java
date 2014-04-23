/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.erp.entity.model;

import java.io.Serializable;
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
import javax.persistence.Lob;
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
@Table(name = "PURCHASE_ORDER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrder.findAll", query = "SELECT p FROM PurchaseOrder p"),
    @NamedQuery(name = "PurchaseOrder.findById", query = "SELECT p FROM PurchaseOrder p WHERE p.id = :id"),
    @NamedQuery(name = "PurchaseOrder.findByCreatedDate", query = "SELECT p FROM PurchaseOrder p WHERE p.createdDate = :createdDate")})
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "PurchaseOrderSequence", sequenceName = "PURCHASE_ORDER_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PurchaseOrderSequence")
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Lob
    @Column(name = "COMMENTS")
    private String comments;
    @JoinColumn(name = "SUPPLIER", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Supplier supplier;
    @JoinColumn(name = "PURCHASE_PLANNING", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PurchasePlanning purchasePlanning;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrder", fetch = FetchType.LAZY)
    private Set<PurchaseOrderItem> purchaseOrderItemSet;

    public PurchaseOrder() {
    }

    public PurchaseOrder(Long id) {
        this.id = id;
    }

    public PurchaseOrder(Long id, Date createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public PurchasePlanning getPurchasePlanning() {
        return purchasePlanning;
    }

    public void setPurchasePlanning(PurchasePlanning purchasePlanning) {
        this.purchasePlanning = purchasePlanning;
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
        if (!(object instanceof PurchaseOrder)) {
            return false;
        }
        PurchaseOrder other = (PurchaseOrder) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.PurchaseOrder[ id=" + id
                + " ]";
    }

}
