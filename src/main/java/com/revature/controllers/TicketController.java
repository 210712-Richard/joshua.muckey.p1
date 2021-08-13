package com.revature.controllers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revature.models.ApproveBenCoTicket;
import com.revature.models.ApproveGrade;
import com.revature.models.ApproveHeadTicket;
import com.revature.models.ApproveSuperTicket;
import com.revature.models.Create;
import com.revature.models.GetBenCoTicket;
import com.revature.models.GetHeadTickets;
import com.revature.models.GetMyTickets;
import com.revature.models.GetSuperTickets;
import com.revature.models.GetTicketFiles;
import com.revature.models.InsertGrade;
import com.revature.models.Ticket;
import com.revature.models.User;
import com.revature.service.SessionService;
import com.revature.service.TicketService;
import com.revature.util.S3Util;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

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
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}

		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(Create.class)).orElse(null);
		if (o == null) {
			ctx.status(409);
		}

		String json = ctx.formParam("ticketJson");
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		Ticket ticket = null;
		try {
			ticket = mapper.readValue(json, Ticket.class);
		} catch (JsonProcessingException e) {
			log.debug(e.getMessage());
			ctx.status(409);
		}
		if (ticket == null) {
			ctx.status(409);
		}
		ticket.setInfo(loggedUser.getInfo());
		ticket.setUser(loggedUser.getUsername());
		ticket.setDept(loggedUser.getDept());
		log.debug(ctx.formParamMap().get("filename").get(0).isEmpty());
		if(!(ctx.formParamMap().get("filename").get(0).isEmpty())) {
			List<UploadedFile> files = ctx.uploadedFiles().stream().collect(Collectors.toList());
			if (!files.isEmpty()) {
				UploadedFile file = files.get(0);
				UUID id = UUID.randomUUID();
				byte[] bytes = null;
				try {
					bytes = new byte[file.getContent().available()];
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					file.getContent().read(bytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				S3Util.getInstance().uploadToBucket(id.toString(), bytes);
				ticket.getAttachments().add(id);
			}
		}

		Ticket rTicket = ts.createTicket(ticket, loggedUser.getDirectSupervisor(), loggedUser.getDept());
		if (rTicket != null) {
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
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}

		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(GetMyTickets.class))
				.orElse(null);
		if (o == null) {
			ctx.status(409);
		}
		List<Ticket> tickets = ts.getMyTickets(loggedUser.getUsername());
		ctx.json(tickets);

	}

	public void getSuper(Context ctx) {
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}

		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(GetSuperTickets.class))
				.orElse(null);
		if (o == null) {
			ctx.status(409);
		}
		List<Ticket> tickets = ts.getSuperTickets(loggedUser.getUsername());
		log.debug(tickets);
		ctx.json(tickets);
	}

	public void getHead(Context ctx) {
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}

		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(GetHeadTickets.class))
				.orElse(null);
		if (o == null) {
			ctx.status(409);
		}
		List<Ticket> tickets = ts.getHeadTickets(loggedUser.getDept());
		log.debug(tickets);
		ctx.json(tickets);
	}

	public void getBen(Context ctx) {
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}

		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(GetBenCoTicket.class))
				.orElse(null);
		if (o == null) {
			ctx.status(409);
		}
		List<Ticket> tickets = ts.getBenTickets();
		log.debug(tickets);
		ctx.json(tickets);
	}

	@Override
	public void getOne(Context ctx, String str) {
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}

		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(GetTicketFiles.class))
				.orElse(null);
		if (o == null) {
			ctx.status(409);
		}
		String id = ctx.pathParam("ticket-id");
		ctx.result(ts.getTicketFiles(id));
	}

	@Override
	public void update(Context ctx, String str) {
		// TODO Auto-generated method stub

	}

	public void approveSuperTicket(Context ctx) {
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}
		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(ApproveSuperTicket.class))
				.orElse(null);
		if (o == null) {
			ctx.status(409);
		}
		ctx.json(ts.approveSuperTicket(loggedUser.getUsername(), ctx.queryParam("id")));

	}

	public void approveHeadTicket(Context ctx) {
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}
		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(ApproveHeadTicket.class))
				.orElse(null);
		if (o == null) {
			ctx.status(409);
		}
		ctx.json(ts.approveHeadTicket(loggedUser.getDept(), ctx.pathParam("id")));

	}

	public void approveBenCoTicket(Context ctx) {
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}
		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(ApproveBenCoTicket.class))
				.orElse(null);
		if (o == null) {
			ctx.status(409);
		}
		ctx.json(ts.approveBenCoTicket(ctx.pathParam("id")));
	}
	
	public void insertGrade(Context ctx) {
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}
		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(InsertGrade.class))
				.orElse(null);
		if (o == null) {
			ctx.status(409);
		}
		if(!(ctx.formParamMap().get("filename").get(0).isEmpty())) {
			List<UploadedFile> files = ctx.uploadedFiles().stream().collect(Collectors.toList());
			if (!files.isEmpty()) {
				UploadedFile file = files.get(0);
				UUID id = UUID.randomUUID();
				byte[] bytes = null;
				try {
					bytes = new byte[file.getContent().available()];
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					file.getContent().read(bytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				S3Util.getInstance().uploadToBucket(id.toString(), bytes);
				ctx.json(ts.insertGrade(ctx.formParam("fileid"), loggedUser.getUsername(), ctx.formParam("ticketid")));
			}
		}
	}

	public void approveGrade(Context ctx) {
		setup(ctx);
		User loggedUser = ctx.sessionAttribute("loginUser");
		if (ss == null || loggedUser == null) {
			ctx.status(409);
		}
		Object o = ss.getMethods().stream().findFirst().filter(p -> p.isAnnotationPresent(ApproveGrade.class))
				.orElse(null);
		if (o == null) {
			ctx.status(409);
		}
		Double x = Double.parseDouble(ctx.formParam("percent"));
		ctx.json(ts.approveGrade(ctx.formParam("id"), x));
	}
	private void setup(Context ctx) {
		ss = ctx.sessionAttribute("session");
	}
}
