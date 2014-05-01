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
@Table(name = "PURCHASE_PLANNING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchasePlanning.findAll", query = "SELECT p FROM PurchasePlanning p"),
    @NamedQuery(name = "PurchasePlanning.findById", query = "SELECT p FROM PurchasePlanning p WHERE p.id = :id"),
    @NamedQuery(name = "PurchasePlanning.findByCreatedDate", query = "SELECT p FROM PurchasePlanning p WHERE p.createdDate = :createdDate"),
    @NamedQuery(name = "PurchasePlanning.findByCreatorName", query = "SELECT p FROM PurchasePlanning p WHERE p.creatorName = :creatorName"),
    @NamedQuery(name = "PurchasePlanning.findByApproveDate", query = "SELECT p FROM PurchasePlanning p WHERE p.approveDate = :approveDate"),
    @NamedQuery(name = "PurchasePlanning.findByApproveUser", query = "SELECT p FROM PurchasePlanning p WHERE p.approveUser = :approveUser"),
    @NamedQuery(name = "PurchasePlanning.getCurrent", query = "SELECT p FROM PurchasePlanning p WHERE p.id = (SELECT MAX(pp.id) FROM PurchasePlanning pp WHERE pp.approveDate IS NULL)")})
public class PurchasePlanning implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "PurchasePlanningSequence", sequenceName = "PURCHASE_PLANNING_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PurchasePlanningSequence")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "CREATOR_NAME", columnDefinition = "nvarchar2(255)")
    private String creatorName;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDate;
    @Column(name = "APPROVE_USER", columnDefinition = "nvarchar2(255)")
    private String approveUser;
    @JoinColumn(name = "QUOTATION", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Quotation quotation;
    @OneToMany(/*cascade = CascadeType.ALL,*/mappedBy = "purchasePlanning", fetch = FetchType.LAZY)
    private Set<PurchaseOrder> purchaseOrder;
    @OneToMany(/*cascade = CascadeType.ALL,*/mappedBy = "purchasePlanning", fetch = FetchType.EAGER)
    private Set<PurchasePlanningItem> purchasePlanningItem;

    public PurchasePlanning() {
    }

    public PurchasePlanning(Long id) {
        this.id = id;
    }

    public PurchasePlanning(Long id, Date createdDate, String creatorName) {
        this.id = id;
        this.createdDate = createdDate;
        this.creatorName = creatorName;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    @XmlTransient
    @JsonIgnore
    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    @XmlTransient
    @JsonIgnore
    public Set<PurchaseOrder> getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrderSet(Set<PurchaseOrder> purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @XmlTransient
    //@JsonIgnore
    public Set<PurchasePlanningItem> getPurchasePlanningItem() {
        return purchasePlanningItem;
    }

    public void setPurchasePlanningItem(
            Set<PurchasePlanningItem> purchasePlanningItem) {
        this.purchasePlanningItem = purchasePlanningItem;
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
        if (!(object instanceof PurchasePlanning)) {
            return false;
        }
        PurchasePlanning other = (PurchasePlanning) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.PurchasePlanning[ id=" + id
                + " ]";
    }

}
