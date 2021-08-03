package com.revature.models;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.stream.Stream;

public class Session {
	
	private Role activeRole;
	private LinkedList<Method> methods = new LinkedList<Method>();
	
	Session(Role role){
		this.activeRole = role;
		populateMethods();
	}

	private void populateMethods() {
		Stream.of(activeRole.getClass().getDeclaredMethods()).forEach(m-> methods.add(m));
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
