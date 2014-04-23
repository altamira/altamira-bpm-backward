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
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Alessandro
 */
@Entity
@Table(name = "USER_PREFERENCE")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "UserPreference.findAll", query = "SELECT u FROM UserPreference u"),
		@NamedQuery(name = "UserPreference.findByName", query = "SELECT u FROM UserPreference u WHERE u.name = :name") })
public class UserPreference implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "NAME", columnDefinition = "nvarchar2(64)")
	private String name;
	@Lob
	@Column(name = "PREFERENCES")
	private String preferences;

	public UserPreference() {
	}

	public UserPreference(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPreferences() {
		return preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (name != null ? name.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof UserPreference)) {
			return false;
		}
		UserPreference other = (UserPreference) object;
		if ((this.name == null && other.name != null)
				|| (this.name != null && !this.name.equals(other.name))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.com.altamira.erp.entity.model.UserPreference[ name=" + name
				+ " ]";
	}

}
