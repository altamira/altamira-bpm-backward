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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "REQUEST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Request.findAll", query = "SELECT r FROM Request r"),
    @NamedQuery(name = "Request.findById", query = "SELECT r FROM Request r WHERE r.id = :id"),
    @NamedQuery(name = "Request.findByCreatedDate", query = "SELECT r FROM Request r WHERE r.createdDate = :createdDate"),
    @NamedQuery(name = "Request.findByCreatorName", query = "SELECT r FROM Request r WHERE r.creatorName = :creatorName"),
    @NamedQuery(name = "Request.findBySendDate", query = "SELECT r FROM Request r WHERE r.sendDate = :sendDate")})
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private long id;
    @Basic(optional = false)
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "CREATOR_NAME")
    private String creatorName;
    @Column(name = "SEND_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "request", fetch = FetchType.LAZY)
    private Set<RequestItem> requestItemSet;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "request", fetch = FetchType.LAZY)
    private QuotationRequest quotationRequest;

    public Request() {
    }

    public Request(long id) {
        this.id = id;
    }

    public Request(long id, Date createdDate, String creatorName) {
        this.id = id;
        this.createdDate = createdDate;
        this.creatorName = creatorName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    @XmlTransient
    @JsonIgnore
    public Set<RequestItem> getRequestItemSet() {
        return requestItemSet;
    }

    public void setRequestItemSet(Set<RequestItem> requestItemSet) {
        this.requestItemSet = requestItemSet;
    }

    public QuotationRequest getQuotationRequest() {
        return quotationRequest;
    }

    public void setQuotationRequest(QuotationRequest quotationRequest) {
        this.quotationRequest = quotationRequest;
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
        if (!(object instanceof Request)) {
            return false;
        }
        Request other = (Request) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }*/

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.Request[ id=" + id + " ]";
    }
    
}
