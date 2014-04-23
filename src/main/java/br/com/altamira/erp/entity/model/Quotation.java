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
@Table(name = "QUOTATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Quotation.findAll", query = "SELECT q FROM Quotation q"),
    @NamedQuery(name = "Quotation.findById", query = "SELECT q FROM Quotation q WHERE q.id = :id"),
    @NamedQuery(name = "Quotation.findByCreatedDate", query = "SELECT q FROM Quotation q WHERE q.createdDate = :createdDate"),
    @NamedQuery(name = "Quotation.findByCreatorName", query = "SELECT q FROM Quotation q WHERE q.creatorName = :creatorName"),
    @NamedQuery(name = "Quotation.findByClosedDate", query = "SELECT q FROM Quotation q WHERE q.closedDate = :closedDate")})
public class Quotation implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "QuotationSequence", sequenceName = "QUOTATION_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QuotationSequence")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "CREATOR_NAME", columnDefinition = "nvarchar2(255)")
    private String creatorName;
    @Column(name = "CLOSED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quotation", fetch = FetchType.LAZY)
    private Set<PurchasePlanning> purchasePlanningSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quotation", fetch = FetchType.LAZY)
    private Set<QuotationRequest> quotationRequestSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quotation", fetch = FetchType.LAZY)
    private Set<QuotationItem> quotationItemSet;

    public Quotation() {
    }

    public Quotation(Long id) {
        this.id = id;
    }

    public Quotation(Long id, Date createdDate, String creatorName) {
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

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    @XmlTransient
    @JsonIgnore
    public Set<PurchasePlanning> getPurchasePlanningSet() {
        return purchasePlanningSet;
    }

    public void setPurchasePlanningSet(Set<PurchasePlanning> purchasePlanningSet) {
        this.purchasePlanningSet = purchasePlanningSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<QuotationRequest> getQuotationRequestSet() {
        return quotationRequestSet;
    }

    public void setQuotationRequestSet(Set<QuotationRequest> quotationRequestSet) {
        this.quotationRequestSet = quotationRequestSet;
    }

    @XmlTransient
    @JsonIgnore
    public Set<QuotationItem> getQuotationItemSet() {
        return quotationItemSet;
    }

    public void setQuotationItemSet(Set<QuotationItem> quotationItemSet) {
        this.quotationItemSet = quotationItemSet;
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
        if (!(object instanceof Quotation)) {
            return false;
        }
        Quotation other = (Quotation) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.Quotation[ id=" + id + " ]";
    }

}
