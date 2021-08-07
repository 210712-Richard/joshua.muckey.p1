package com.revature.service;

import java.util.stream.Stream;

import com.revature.data.TicketDAO;
import com.revature.models.Department;
import com.revature.models.Ticket;

public class TicketService {

	private TicketDAO ticketDao = new TicketDAO();

	public Ticket createTicket(Ticket ticket, String supervisor) {
		Department dept = headChecker(supervisor);
		if(dept != null) {
			return ticketDao.createHead(ticket,dept);
		}
		return ticketDao.createSuper(ticket, supervisor);
	}
	
	private Department headChecker(String username) {
		return Stream.of(Department.values()).filter(p -> p.getHead().equals(username)).findFirst().orElse(null);
	}
	
}
