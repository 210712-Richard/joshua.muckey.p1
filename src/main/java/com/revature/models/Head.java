package com.revature.models;

public class Head extends Employee implements Role{

	private final String name = "Head";

	@ApproveHeadTicket
	public void getAprroveTicketHook() { //Hook for Controller
	}
	@GetHeadTickets
	public void getHeadTicketHook() { //Hook for Controller
	}
	
	@Override
	public String getName() {
		return name;
	}
}
