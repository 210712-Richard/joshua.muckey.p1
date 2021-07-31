package com.revature.data;

import com.datastax.oss.driver.api.core.CqlSession;
import com.revature.models.User;
import com.revature.util.CassandraUtil;

public class UserDAO {
	
	private CqlSession session = CassandraUtil.getInstance().getSession();

	public User createUser(User u) {
		
		return null;
	}

	public User updateUser(User u, User u2) {
		// TODO Auto-generated method stub
		return null;
	}

}
