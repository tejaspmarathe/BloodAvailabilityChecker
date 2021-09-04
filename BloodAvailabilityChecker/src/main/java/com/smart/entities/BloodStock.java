package com.smart.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "BLOODSTOCK")
public class BloodStock {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int bloodstockid;
	private String apos;
	private String aneg;
	private String bpos;
	private String bneg;
	private String abpos;
	private String abneg;
	private String opos;
	private String oneg;

	@OneToOne
	@JsonIgnore
	private User user;

	public BloodStock() {
		super();
	}

	public int getBloodstockid() {
		return bloodstockid;
	}

	public void setBloodstockid(int bloodstockid) {
		this.bloodstockid = bloodstockid;
	}

	public String getApos() {
		return apos;
	}

	public void setApos(String apos) {
		this.apos = apos;
	}

	public String getAneg() {
		return aneg;
	}

	public void setAneg(String aneg) {
		this.aneg = aneg;
	}

	public String getBpos() {
		return bpos;
	}

	public void setBpos(String bpos) {
		this.bpos = bpos;
	}

	public String getBneg() {
		return bneg;
	}

	public void setBneg(String bneg) {
		this.bneg = bneg;
	}

	public String getAbpos() {
		return abpos;
	}

	public void setAbpos(String abpos) {
		this.abpos = abpos;
	}

	public String getAbneg() {
		return abneg;
	}

	public void setAbneg(String abneg) {
		this.abneg = abneg;
	}

	public String getOpos() {
		return opos;
	}

	public void setOpos(String opos) {
		this.opos = opos;
	}

	public String getOneg() {
		return oneg;
	}

	public void setOneg(String oneg) {
		this.oneg = oneg;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.bloodstockid==((BloodStock)obj).getBloodstockid();
	}

}
