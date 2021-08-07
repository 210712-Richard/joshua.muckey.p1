package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Create;
import com.revature.models.Ticket;
import com.revature.models.User;
import com.revature.service.SessionService;
import com.revature.service.TicketService;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;

public class TicketController implements CrudHandler {
	
	private TicketService ts = new TicketService();
	private Logger log = LogManager.getLogger(SessionService.class);
	private SessionService ss;
	
	@Override
	public void create(Context ctx) {
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		log.debug(ss);
		log.debug(loggedUser);
		if(ss == null || loggedUser == null) {
			ctx.status(409);
		}
		
		Object o = ss.getMethods().stream().findFirst().filter(p->p.isAnnotationPresent(Create.class)).orElse(null);
		if(o == null) {
			ctx.status(409);
		}
		Ticket ticket = ctx.bodyAsClass(Ticket.class);
		Ticket rTicket = ts.createTicket(ticket, loggedUser.getDirectSupervisor());
		if(rTicket != null) {
			ctx.status(201);
			ctx.json(rTicket);
		} else {
			ctx.status(409);
		}
	}

	@Override
	public void delete(Context ctx, String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAll(Context ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOne(Context ctx, String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Context ctx, String str) {
		// TODO Auto-generated method stub
		
	}

	private void setup(Context ctx) {
		ss = ctx.sessionAttribute("session");
	}
}
