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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "REQUEST_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RequestItem.findAll", query = "SELECT r FROM RequestItem r"),
    @NamedQuery(name = "RequestItem.findById", query = "SELECT r FROM RequestItem r WHERE r.id = :id"),
    @NamedQuery(name = "RequestItem.findByArrivalDate", query = "SELECT r FROM RequestItem r WHERE r.arrivalDate = :arrivalDate"),
    @NamedQuery(name = "RequestItem.findByWeight", query = "SELECT r FROM RequestItem r WHERE r.weight = :weight")})
public class RequestItem implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private long id;
    @Basic(optional = false)
    @Column(name = "ARRIVAL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalDate;
    @Basic(optional = false)
    @Column(name = "WEIGHT")
    private BigDecimal weight;
    @JoinColumn(name = "REQUEST", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Request request;
    @JoinColumn(name = "MATERIAL", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Material material;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requestItem", fetch = FetchType.LAZY)
    private Set<PurchasePlanningItem> purchasePlanningItemSet;

    public RequestItem() {
    }

    public RequestItem(long id) {
        this.id = id;
    }

    public RequestItem(long id, Date arrivalDate, BigDecimal weight) {
        this.id = id;
        this.arrivalDate = arrivalDate;
        this.weight = weight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @XmlTransient
    @JsonIgnore
    public Set<PurchasePlanningItem> getPurchasePlanningItemSet() {
        return purchasePlanningItemSet;
    }

    public void setPurchasePlanningItemSet(Set<PurchasePlanningItem> purchasePlanningItemSet) {
        this.purchasePlanningItemSet = purchasePlanningItemSet;
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
        if (!(object instanceof RequestItem)) {
            return false;
        }
        RequestItem other = (RequestItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }*/

    @Override
    public String toString() {
        return "br.com.altamira.erp.entity.model.RequestItem[ id=" + id + " ]";
    }
    
}
