package com.revature.service;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.revature.data.TicketDAO;
import com.revature.models.Department;
import com.revature.models.Ticket;

public class TicketService {

	private TicketDAO ticketDao = new TicketDAO();

	public Ticket createTicket(Ticket ticket, String supervisor, Department dept) {
		
		if(headChecker(supervisor) || !ticket.getAttachments().isEmpty()) {
			return ticketDao.createHead(ticket,dept);
		}
		return ticketDao.createSuper(ticket, supervisor, dept);
	}
	
	private boolean headChecker(String username) {
		return Stream.of(Department.values()).anyMatch(p -> p.getHead().equals(username));
	}

	public List<Ticket> getMyTickets(String username) {
		return ticketDao.getMyTickets(username);
	}
	public List<Ticket> getSuperTickets(String supervisor){
		return ticketDao.getSuperTickets(supervisor);
	}

	public List<Ticket> getHeadTickets(Department dept) {
		// TODO Auto-generated method stub
		return ticketDao.getHeadTickets(dept);
	}
	public List<Ticket> getBenTickets() {
		// TODO Auto-generated method stub
		return ticketDao.getBenCoTickets();
	}
	

	public Ticket approveSuperTicket(String supervisor, String queryParam) {
		return ticketDao.approveSuperTicket(supervisor, queryParam);
		
	}

	public Ticket approveHeadTicket(Department dept, String queryParam) {
		return ticketDao.approveHeadTicket(dept, queryParam);
	}

	public Ticket approveBenCoTicket(String queryParam) {
	
		return ticketDao.approveBenCoTicket(queryParam);
	}

	public InputStream getTicketFiles(String id) {
		
		return ticketDao.getTicketFile(id);
	}

	public Ticket insertGrade(String fileid, String username, String ticketid) {
		
		return ticketDao.insertGrade(fileid, username, ticketid);
	}
	public Ticket approveGrade(String id, double percentage) {
		return ticketDao.approveGrade(id, percentage);
	}

}
