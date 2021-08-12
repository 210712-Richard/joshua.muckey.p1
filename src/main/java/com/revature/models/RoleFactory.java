package com.revature.models;

public class RoleFactory {
	
	public static Role getRole(String role) {
		switch(role) {
		case "Supervisor":
			return new Supervisor();
		case "Employee":
			return new Employee();
		case "Head":
			return new Head();
		case "Benco":
			return new Benco();
		default:
			return new Employee();
		}
		
	}

	public static String setRole(Role a) {
		// TODO Auto-generated method stub
		return a.getName();
	}

}
