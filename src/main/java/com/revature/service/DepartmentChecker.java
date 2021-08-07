package com.revature.service;

import java.util.stream.Stream;

import com.revature.models.Department;

public class DepartmentChecker {
	
	public Department headOfDepartment(String username) {
		return Stream.of(Department.values()).filter(p -> p.getHead().equals(username)).findFirst().orElse(null);
	}

}
