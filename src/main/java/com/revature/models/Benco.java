package com.revature.models;

public class Benco extends Employee {

	private final String name = "Benco";
	public Benco() {
	}

	@ApproveBenCoTicket
	public void approveBenCoTicketHook() { //Hook
		
	}
	@GetBenCoTicket
	public void getBenCoTicketHook() { //Hook
		
	}
	@ApproveGrade
	public void approveGrade() { //Hook
	}
	@Override
	public String getName() {
		return name;
	}
}
