package com.revature.controllers;

import static io.javalin.apibuilder.ApiBuilder.crud;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

import io.javalin.Javalin;

public class Driver {

	public static void main(String[] args) {

		Javalin app = Javalin.create().start(8080);

		UserController uc = new UserController();
		TicketController tc = new TicketController();
		
		app.routes(() -> {
			crud("users/:username", uc);
			get("users/:username/session", uc::createSession);
			path("users/:username/session", ()->{
				crud(":ticket-id", tc);
			});

		});
		app.get("/login", uc::login);
		
	}
}
