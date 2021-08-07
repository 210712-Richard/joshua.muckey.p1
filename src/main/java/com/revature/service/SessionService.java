package com.revature.service;


import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.stream.Stream;

import com.revature.models.Role;

public class SessionService {

	private Role activeRole;
	private LinkedList<Method> methods = new LinkedList<Method>();

	public SessionService(Role role) {
		this.activeRole = role;
		populateMethods();
	}

	private void populateMethods() {
		Stream.of(activeRole.getClass().getDeclaredMethods()).forEach(m -> methods.add(m));
	}

	public LinkedList<Method> getMethods() {
		return methods;
	}

	public void setMethods(LinkedList<Method> methods) {
		this.methods = methods;
	}

	public Role getActiveRole() {
		return activeRole;
	}

	public void setActiveRole(Role activeRole) {
		this.activeRole = activeRole;
	}


}
