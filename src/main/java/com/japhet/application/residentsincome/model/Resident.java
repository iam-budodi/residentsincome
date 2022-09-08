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

import com.japhet.application.residentsincome.util.PasswordDigest;

/**
 * Entity implementation class for Entity: Resident
 *
 */
@Entity
@Table(name = "Resident", uniqueConstraints = @UniqueConstraint(columnNames = {
		"id", "phone_number", "user_name" }))
@NamedQueries({
		@NamedQuery(name = Resident.FIND_BY_EMAIL, query = "SELECT user FROM Resident user WHERE user.email = :email"),
		@NamedQuery(name = Resident.FIND_BY_UUID, query = "SELECT user FROM Resident user WHERE user.uuid = :uuid"),
		@NamedQuery(name = Resident.FIND_BY_USERNAME, query = "SELECT user FROM Resident user WHERE user.userName = :username"),
		@NamedQuery(name = Resident.FIND_BY_USERNAME_PASSWORD, query = "SELECT user FROM Resident user WHERE user.userName= :username AND user.password = :password")
		 })
public class Resident implements Serializable {

	public static final String FIND_BY_EMAIL = "Resident.findByEmail";
	public static final String FIND_BY_USERNAME = "Resident.findByUserName";
	public static final String FIND_BY_UUID = "Resident.findByUUID";
	public static final String FIND_BY_USERNAME_PASSWORD = "Resident.findByUserNameAndPassword";

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
	@Size(min = 9, max = 13, message = "phone number must be a min of 9 and max of 13 digits")
	@Digits(fraction = 0, integer = 13)
	@Column(name = "phone_number")
	private String phoneNumber;

	@NotNull
	@Size(min = 1, max = 12)
	@Column(length = 12, name = "user_name", nullable = false)
//	@Pattern(regexp = "[A-Za-z0-9_@.$&+-]*", message = "should contains only alphanumeric and special characters")
	private String userName;

	@NotNull
	@Size(min = 8, max = 255)
	@Column(length = 255, name = "password", nullable = false)
//	@Pattern(regexp = "[A-Za-z0-9_@.$&+-]*", message = "should contains only alphanumeric and special characters")
	private String password;

	@Past
	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Enumerated
	@Column(name = "user_role")
	private UserRole role;

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	private static final long serialVersionUID = 1L;

	@PrePersist
	@PreUpdate
	private void digestPassword() {
		password = PasswordDigest.digestPassword(password);
	}

	public Resident() {
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

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
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
					lastName, phoneNumber, password, role, updatedOn, userName,
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
		Resident resident = (Resident) obj;
		return Objects.equals(createdOn, resident.createdOn)
					&& Objects.equals(dateOfBirth, resident.dateOfBirth)
					&& Objects.equals(email, resident.email)
					&& Objects.equals(firstName, resident.firstName)
					&& Objects.equals(id, resident.id)
					&& Objects.equals(lastName, resident.lastName)
					&& Objects.equals(phoneNumber, resident.phoneNumber)
					&& Objects.equals(password, resident.password)
					&& role == resident.role
					&& Objects.equals(updatedOn, resident.updatedOn)
					&& Objects.equals(userName, resident.userName)
					&& Objects.equals(uuid, resident.uuid);
	}

	@Override
	public String toString() {
		return "Resident [id=" + id + ", uuid=" + uuid + ", firstName=" + firstName
					+ ", lastName=" + lastName + ", email=" + email
					+ ", phoneNumber=" + phoneNumber + ", userName=" + userName
					+ ", password=" + password + ", dateOfBirth=" + dateOfBirth
					+ ", role=" + role + ", updatedOn=" + updatedOn
					+ ", createdOn=" + createdOn + "]";
	}

}
