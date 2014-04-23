/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Alessandro
 */
@Entity
@Table(name = "QUOTATION_REQUEST")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "QuotationRequest.findAll", query = "SELECT q FROM QuotationRequest q"),
		@NamedQuery(name = "QuotationRequest.findById", query = "SELECT q FROM QuotationRequest q WHERE q.id = :id") })
public class QuotationRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Id
	@Basic(optional = false)
	@Column(name = "ID")
	private Long id;
	@JoinColumn(name = "REQUEST", referencedColumnName = "ID")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Request request;
	@JoinColumn(name = "QUOTATION", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Quotation quotation;

	public QuotationRequest() {
	}

	public QuotationRequest(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
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
		if (!(object instanceof QuotationRequest)) {
			return false;
		}
		QuotationRequest other = (QuotationRequest) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.com.altamira.erp.entity.model.QuotationRequest[ id=" + id
				+ " ]";
	}

}
