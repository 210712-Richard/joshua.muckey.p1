package com.revature.models;

import java.util.Objects;

public class Employee extends Role {

	private String name = "Employee";

	public Employee() {
		super();
	}

	private Ticket createTicket() {
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + "]";
	}

}
