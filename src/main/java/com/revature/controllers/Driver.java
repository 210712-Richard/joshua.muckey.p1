package com.revature.controllers;

import static io.javalin.apibuilder.ApiBuilder.crud;
import static io.javalin.apibuilder.ApiBuilder.get;

import io.javalin.Javalin;

public class Driver {

	public static void main(String[] args) {

		Javalin app = Javalin.create().start(8080);

		UserController uc = new UserController();

		app.routes(() -> {
			crud("users/:username", uc);
			get("users/:username/session", uc::createSession);

		});
		app.get("/login", uc::login);
		
	}
}
