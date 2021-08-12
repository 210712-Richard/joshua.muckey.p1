package com.revature.controllers;

import static io.javalin.apibuilder.ApiBuilder.crud;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.patch;

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
				patch("headtickets/:id", tc::approveHeadTicket);
				patch("bencotickets/:id", tc::approveBenCoTicket);
			});

		});
		app.get("/login", uc::login);
		
	}
}
