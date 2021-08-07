package com.revature.models;


public class Employee implements Role {

	private final String name = "Employee";
	public Employee() {
	}

	@Create
	public Ticket create(Ticket t) {
		
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

}
