package com.revature.controllers;

import static io.javalin.apibuilder.ApiBuilder.crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.revature.models.Role;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
			    .builder()
			    .allowIfBaseType(Role.class)
			    .build();
		ObjectMapper mapper = JsonMapper.builder().activateDefaultTyping(ptv, DefaultTyping.NON_FINAL).build();
		JavalinJackson.configure(mapper);

		Javalin app = Javalin.create().start(8080);

		UserController uc = new UserController();

		app.routes(() -> {
			crud("users/:username", uc);

		});
		app.get("/login", uc::login);
	}
}
