package com.japhet.application.residentsincome.model;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity

public class User implements Serializable {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Long id;
	private String uuid;
	private String firstName;
	private String lastName;
	private String email;
	private String mobile;
	private String userName;
	private String passw0rd;   
	private LocalDate dateOfBirth;
	private UserRole role;
	private LocalDateTime updatedOn;
	private LocalDateTime createdOn;
	private static final long serialVersionUID = 1L;

	public User() {
		super();
	}   
	
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}   
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}   
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}   
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}   
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}   
	public String getPassw0rd() {
		return this.passw0rd;
	}

	public void setPassw0rd(String passw0rd) {
		this.passw0rd = passw0rd;
	}   
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}   
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}   
	public LocalDate getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdOn, dateOfBirth, email, firstName, id,
					lastName, mobile, passw0rd, role, updatedOn, userName,
					uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User user = (User) obj;
		return Objects.equals(createdOn, user.createdOn)
					&& Objects.equals(dateOfBirth, user.dateOfBirth)
					&& Objects.equals(email, user.email)
					&& Objects.equals(firstName, user.firstName)
					&& Objects.equals(id, user.id)
					&& Objects.equals(lastName, user.lastName)
					&& Objects.equals(mobile, user.mobile)
					&& Objects.equals(passw0rd, user.passw0rd)
					&& role == user.role
					&& Objects.equals(updatedOn, user.updatedOn)
					&& Objects.equals(userName, user.userName)
					&& Objects.equals(uuid, user.uuid);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", uuid=" + uuid + ", firstName=" + firstName
					+ ", lastName=" + lastName + ", email=" + email
					+ ", mobile=" + mobile + ", userName=" + userName
					+ ", passw0rd=" + passw0rd + ", dateOfBirth=" + dateOfBirth
					+ ", role=" + role + ", updatedOn=" + updatedOn
					+ ", createdOn=" + createdOn + "]";
	}
   
	
}
