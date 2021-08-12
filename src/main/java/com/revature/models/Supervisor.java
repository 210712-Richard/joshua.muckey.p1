package com.revature.models;

public class Supervisor extends Employee implements Role {
	private final String name = "Supervisor";
	
	public Supervisor() {
	}

	@GetSuperTickets
	public void getSuperTicketHook() { //Hook for Controller
	}
	@ApproveSuperTicket
	public void getAprroveTicketHook() { //Hook for Controller
	}
	
	@Override
	public String getName() {
		return name;
	}
}
