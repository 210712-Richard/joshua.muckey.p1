package com.revature.controllers;

import com.revature.models.User;
import com.revature.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;

public class UserController implements CrudHandler{
	
	UserService us = new UserService();
	private Logger log = LogManager.getLogger(UserController.class);

	
	public void login(Context ctx) {
		
		System.out.println("here");
		User u = ctx.bodyAsClass(User.class);
		
		u = us.login(u);
		
		if(u != null) {
			ctx.sessionAttribute("loginUser", u);
			ctx.json(u);
			log.debug(u);
			return;
		}
		ctx.status(401);
	}
	
	@Override
	public void create(Context ctx) {
		User u = ctx.bodyAsClass(User.class);
		User newUser = us.createUser(u);
		if(newUser != null) {
			ctx.status(201);
			ctx.json(newUser);
		} else {
			ctx.status(409);
			ctx.html("Username already taken.");
		}
		
	}
	@Override
	public void delete(Context ctx, String arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAll(Context ctx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getOne(Context ctx, String arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(Context ctx, String arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
}
