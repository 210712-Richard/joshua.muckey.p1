package com.revature.controllers;

import static io.javalin.apibuilder.ApiBuilder.crud;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

public class Driver {

	public static void main(String[] args) {

		ObjectMapper jackson = new ObjectMapper();
		jackson.registerModule(new JavaTimeModule());
		jackson.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		JavalinJackson.configure(jackson);
		
		Javalin app = Javalin.create().start(8080);

		UserController uc = new UserController();
		TicketController tc = new TicketController();
		
		app.routes(() -> {
			crud("users/:username", uc);
			get("users/:username/session", uc::createSession);
			path("users/:username/session", ()->{
				crud("tickets/:ticket-id", tc);
				get("supertickets",tc::getSuper);
				get("headtickets",tc::getHead);
				get("bencotickets", tc::getBen);
				patch("supertickets/:id", tc::approveSuperTicket);
				patch("supertickets/:id/disapprove", tc::disapproveSuperTicket);
				patch("headtickets/:id", tc::approveHeadTicket);
				patch("headtickets/:id/disapprove", tc::disapproveHeadTicket);
				patch("bencotickets/:id", tc::approveBenCoTicket);
				patch("bencotickets/:id/disapprove", tc::disapproveBenCoTicket);
				patch("tickets/uploadGrade", tc::insertGrade);
				put("bencotickets/approveGrade", tc::approveGrade);
			});

		});
		app.get("/login", uc::login);
		app.delete("/logout", uc::logout);
		app.delete("users/closeSession", uc::closeSession);
	}
}
