package com.japhet.application.residentsincome.model;

import java.io.Serializable;
import java.lang.Long;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Entity implementation class for Entity: User
 *
 */
@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "phoneNumber", "userName"}))
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Long id;
	
	@Size(min = 1, max = 256)
	@Column(length = 256)
	private String uuid;
	
	@NotNull
	@Size(min = 2, max = 32)
	@Column(length = 32, name = "first_name", nullable = false)
	@Pattern(regexp = "[A-Za-z]*", message = "should not be empty and contains only letters")
	private String firstName;
	
	@NotNull
	@Size(min = 2, max = 32)
	@Column(length = 32, name = "last_name", nullable = false)
	@Pattern(regexp = "[A-Za-z]*", message = "should not be empty and contains only letters")
	private String lastName;
	
	@NotNull
	@NotEmpty
	@Email(message = "enter valid and well-formed email address")
	private String email;
	
	@NotNull
	@Size(min = 9, max = 12, message = "phone number must be a min of 9 and max of 12 digits")
	@Digits(fraction = 0, integer = 12)
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@NotNull
	@Size(min = 1, max = 12)
	@Column(length = 12, name = "user_name", nullable = false)
	@Pattern(regexp = "[A-Za-z0-9_@.$&+-]*", message = "should contains only alphanumeric and special characters")
	private String userName;
	
	@NotNull
	@Size(min = 1, max = 12)
	@Column(length = 12, name = "user_name", nullable = false)
	@Pattern(regexp = "[A-Za-z0-9_@.$&+-]*", message = "should contains only alphanumeric and special characters")
	private String passw0rd;
	
	@Past
	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;
	
	@Enumerated
	@Column(name = "user_name")
	private UserRole role;
	
	@Column(name = "updated_on")
	private LocalDateTime updatedOn;
	
	@Column(name = "created_on")
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
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
					lastName, phoneNumber, passw0rd, role, updatedOn, userName,
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
					&& Objects.equals(phoneNumber, user.phoneNumber)
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
					+ ", phoneNumber=" + phoneNumber + ", userName=" + userName
					+ ", passw0rd=" + passw0rd + ", dateOfBirth=" + dateOfBirth
					+ ", role=" + role + ", updatedOn=" + updatedOn
					+ ", createdOn=" + createdOn + "]";
	}
   
	
}
