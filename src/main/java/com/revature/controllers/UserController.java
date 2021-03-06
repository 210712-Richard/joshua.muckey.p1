package com.revature.controllers;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.service.SessionService;
import com.revature.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;

public class UserController implements CrudHandler{
	
	UserService us = new UserService();
	private Logger log = LogManager.getLogger(UserController.class);

	
	public void login(Context ctx) {
		
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
	public void logout(Context ctx) {
		log.debug("logout!");
		ctx.result("Goodbye! " + ((User)ctx.sessionAttribute("loginUser")).getUsername());
		ctx.req.getSession().invalidate();
	}
	
	public void closeSession(Context ctx) {
		log.debug("Session closed");
		ctx.sessionAttribute("session", null);
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
		User loggedUser = ctx.sessionAttribute("loginUser");
		if(loggedUser == null) {
			ctx.status(409);
		}
		User u = ctx.bodyAsClass(User.class);
		log.debug(u);
		u = us.updateUser(loggedUser, u);
		ctx.sessionAttribute("loginUser", u);
		ctx.json(u);
		
	}
	public void createSession(Context ctx) {
		User u = ctx.sessionAttribute("loginUser");
		log.debug(u);
		if(u == null) {
			ctx.status(401);
		}else {
		Role r = ctx.bodyAsClass(Role.class);
		log.debug(r);
		
		if(!u.checkRole(r)) {
			ctx.status(401);
			return;
		}
		SessionService s = u.createSession(r);
		ctx.sessionAttribute("session", s);
		ctx.result(s.getMethods().toString());
		}
	}
	
	
}
