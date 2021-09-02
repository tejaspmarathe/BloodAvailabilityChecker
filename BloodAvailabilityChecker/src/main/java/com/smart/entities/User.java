package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "Name should not be blank")
	@Size(min = 2,max = 20,message = "min 2 and max 20 characters are allowed")
	private String name;
	
	@Column(unique = true)
	@Email(regexp ="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+.com+$")
	private String email;
	
	@Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",message = "Invalid password format. ex.Abcd@123")
	private String password;
	
	@Pattern(regexp = "^[7-9][0-9]{9}$",message = "Invalid mobile number format.")
	private String phone;
	
	@NotBlank(message = "Address should not be blank")
	private String address;
	
	@NotBlank(message = "City should not be blank")
	private String location;
	
	private String status;
	
	private String role;
	
	/*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user" , orphanRemoval = true)
	private List<BloodStock> bloodStocks=new ArrayList<BloodStock>();*/
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user" )
	BloodStock bloodStock;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public BloodStock getBloodStock() {
		return bloodStock;
	}

	public void setBloodStock(BloodStock bloodStock) {
		this.bloodStock = bloodStock;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", phone=" + phone
				+ ", address=" + address + ", location=" + location + ", status=" + status + ", role=" + role
				+ ", bloodStock=" + bloodStock + "]";
	}

}
