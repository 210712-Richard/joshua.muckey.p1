package com.revature.models;

public class RoleFactory {
	
	public static Role getRole(String role) {
		
		return new Employee();
	}

	public static String setRole(Role a) {
		// TODO Auto-generated method stub
		return a.getName();
	}

}
