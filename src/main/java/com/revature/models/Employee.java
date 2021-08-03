package com.revature.models;


public class Employee implements Role {

	private final String name = "Employee";
	public Employee() {
	}

	private Ticket createTicket() {
		return null;
	}

	@Override
	public String toString() {
		return "Employee";
	}

	@Override
	public String getName() {
		return name;
	}

}
