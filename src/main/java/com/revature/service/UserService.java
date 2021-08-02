package com.revature.service;

import com.revature.data.UserDAO;
import com.revature.models.User;

public class UserService {

	private UserDAO userdao = new UserDAO();

	public User createUser(User u) {
		return userdao.createUser(u);
	}

	public User updateUser(User u, User u2) {
		
		return userdao.updateUser(u, u2);
	}

	public User login(User u) {
		
		return userdao.login(u);
	}

}
