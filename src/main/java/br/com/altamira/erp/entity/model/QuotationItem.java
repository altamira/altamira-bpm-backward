/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "QUOTATION_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuotationItem.findAll", query = "SELECT q FROM QuotationItem q"),
    @NamedQuery(name = "QuotationItem.findById", query = "SELECT q FROM QuotationItem q WHERE q.id = :id"),
    @NamedQuery(name = "QuotationItem.findByLamination", query = "SELECT q FROM QuotationItem q WHERE q.lamination = :lamination"),
    @NamedQuery(name = "QuotationItem.findByTreatment", query = "SELECT q FROM QuotationItem q WHERE q.treatment = :treatment"),
    @NamedQuery(name = "QuotationItem.findByThickness", query = "SELECT q FROM QuotationItem q WHERE q.thickness = :thickness"),
    @NamedQuery(name = "QuotationItem.findByStandard", query = "SELECT q FROM QuotationItem q WHERE q.standard = :standard"),
    @NamedQuery(name = "QuotationItem.findByWeight", query = "SELECT q FROM QuotationItem q WHERE q.weight = :weight")})
public class QuotationItem implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private long id;
    @Basic(optional = false)
    @Column(name = "LAMINATION")
    private String lamination;
    @Basic(optional = false)
    @Column(name = "TREATMENT")
    private String treatment;
    @Basic(optional = false)
    @Column(name = "THICKNESS")
    private BigDecimal thickness;
    @Basic(optional = false)
    @Column(name = "STANDARD")
    private BigInteger standard;
    @Basic(optional = false)
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @OneToMany(mappedBy = "quotationItem", fetch = FetchType.LAZY)
    private Set<QuotationItemQuote> quotationItemQuoteSet;
    @JoinColumn(name = "QUOTATION", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Quotation quotation;

    public QuotationItem() {
    }

    public QuotationItem(long id) {
        this.id = id;
    }

    public QuotationItem(long id, String lamination, String treatment, BigDecimal thickness, BigInteger standard, BigDecimal weight) {
        this.id = id;
        this.lamination = lamination;
        this.treatment = treatment;
        this.thickness = thickness;
        this.standard = standard;
        this.weight = weight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLamination() {
        return lamination;
    }

    public void setLamination(String lamination) {
        this.lamination = lamination;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public BigDecimal getThickness() {
        return thickness;
    }

    public void setThickness(BigDecimal thickness) {
        this.thickness = thickness;
    }

    public BigInteger getStandard() {
        return standard;
    }

    public void setStandard(BigInteger standard) {
        this.standard = standard;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    @XmlTransient
    @JsonIgnore
    public Set<QuotationItemQuote> getQuotationItemQuoteSet() {
        return quotationItemQuoteSet;
    }

    public void setQuotationItemQuoteSet(Set<QuotationItemQuote> quotationItemQuoteSet) {
        this.quotationItemQuoteSet = quotationItemQuoteSet;
    }

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    /*@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuotationItem)) {
            return false;
        }
        QuotationItem other = (QuotationItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }*/

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.QuotationItem[ id=" + id + " ]";
    }
    
}
