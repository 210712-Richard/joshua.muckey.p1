package com.revature.models;

import java.util.HashSet;
import java.util.Objects;


public class User {
	
	private String username;
	private String password;
	private BasicInfo info;
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
		info = new BasicInfo(firstName, lastName,email);
		roles.add(role);
		this.dept = dept;
		this.directSupervisor = directSupervisor;
	}
	public User(String username, String password, String email, String firstName, String lastName, HashSet<Role> roles,
			String dept, String directSupervisor, double pendingbalance, double awardedbalance) {
		this.username = username;
		this.password = password;
		info = new BasicInfo(firstName, lastName,email);
		this.roles = roles;
		this.dept = Department.valueOf(dept);
		this.directSupervisor = directSupervisor;
		this.pendingBalance = pendingbalance;
		this.awardedBalance = awardedbalance;
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

	public BasicInfo getInfo() {
		return info;
	}

	public void setInfo(BasicInfo info) {
		this.info = info;
	}

	public HashSet<Role> getRole() {
		return roles;
	}

	public void setRole(HashSet<Role> roles) {
		this.roles = roles;
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

	public Session createSession(Role role) {
		session = new Session(role);
		return session;
		
	}
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", info=" + info + ", role=" + roles
				+ ", dept=" + dept + ", directSupervisor=" + directSupervisor + ", pendingBalance=" + pendingBalance
				+ ", awardedBalance=" + awardedBalance + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(awardedBalance, dept, directSupervisor, info, password, pendingBalance, roles,
				username);
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
				&& Objects.equals(directSupervisor, other.directSupervisor) && Objects.equals(info, other.info)
				&& Objects.equals(password, other.password) && Objects.equals(pendingBalance, other.pendingBalance)
				&& Objects.equals(roles, other.roles) && Objects.equals(username, other.username);
	}


}
