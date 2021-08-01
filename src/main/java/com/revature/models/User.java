package com.revature.models;

import java.util.HashSet;
import java.util.Objects;

public class User {

	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private HashSet<Role> roles = new HashSet<Role>();
	private Department dept;
	private String directSupervisor;
	private Double pendingBalance;
	private Double awardedBalance;
	private Session session;

	public User(String username, String password, String email, String firstName, String lastName, Role role,
			Department dept, String directSupervisor) {
		this();
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		roles.add(role);
		this.dept = dept;
		this.directSupervisor = directSupervisor;
	}

	public User() {
		super();
		this.pendingBalance = 0.0;
		this.awardedBalance = 0.0;
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean checkRole(Role role) {
		return roles.contains(role);
	}

	public boolean addRole(Role role) {
		return roles.add(role);
	}
	public boolean removeRole(Role role) {
		return roles.remove(role);
	}

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

	public String getDirectSupervisor() {
		return directSupervisor;
	}

	public void setDirectSupervisor(String directSupervisor) {
		this.directSupervisor = directSupervisor;
	}

	public Double getPendingBalance() {
		return pendingBalance;
	}

	public void setPendingBalance(Double pendingBalance) {
		this.pendingBalance = pendingBalance;
	}

	public Double getAwardedBalance() {
		return awardedBalance;
	}

	public void setAwardedBalance(Double awardedBalance) {
		this.awardedBalance = awardedBalance;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", email=" + email + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", roles=" + roles + ", dept=" + dept + ", directSupervisor="
				+ directSupervisor + ", pendingBalance=" + pendingBalance + ", awardedBalance=" + awardedBalance
				+ ", session=" + session + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(awardedBalance, dept, directSupervisor, email, firstName, lastName, password,
				pendingBalance, roles, session, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(awardedBalance, other.awardedBalance) && dept == other.dept
				&& Objects.equals(directSupervisor, other.directSupervisor) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(password, other.password) && Objects.equals(pendingBalance, other.pendingBalance)
				&& Objects.equals(roles, other.roles) && Objects.equals(session, other.session)
				&& Objects.equals(username, other.username);
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
