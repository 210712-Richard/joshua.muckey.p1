package com.revature.models;


public class Employee implements Role {

	private final String name = "Employee";
	public Employee() {
	}

	@Create
	public void createHook() { //Hook for Controller
		}

	@Override
	public String getName() {
		return name;
	}

}
