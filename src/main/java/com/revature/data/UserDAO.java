package com.revature.data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.revature.models.Role;
import com.revature.models.RoleFactory;
import com.revature.models.User;
import com.revature.util.CassandraUtil;

public class UserDAO {
	
	private CqlSession session = CassandraUtil.getInstance().getSession();

	public User createUser(User u) {
		
		String query = "Insert into users (username, password, email, firstname, lastname, roles, dept, directsupervisor, pendingbalance, awardedbalance) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s)
				.bind(u.getUsername(), u.getPassword(), u.getInfo().getEmail(), u.getInfo().getFirstName(), u.getInfo().getLastName(), setRoles(u.getRole()), u.getDept().toString(),u.getDirectSupervisor() ,u.getPendingBalance(), u.getAwardedBalance());
		session.execute(bound);
		return u;
	}

	public User updateUser(User u, User u2) {
		// TODO Auto-generated method stub
		return null;
	}

	public User login(User u) {
		String query = "Select username, password, email, firstname, lastname, roles, dept, directsupervisor, pendingbalance, awardedbalance from users where username=?";
		SimpleStatement s = new SimpleStatementBuilder(query).build();
		BoundStatement bound = session.prepare(s).bind(u.getUsername());
		ResultSet result = session.execute(bound);
		Row row = result.one();
		if(row == null)
			return null;
		User user = new User(row.getString("username"),row.getString("password"),row.getString("email"),row.getString("firstname"),row.getString("lastname"),getRoles(row.getSet("roles", String.class)),row.getString("dept"),row.getString("directsupervisor"),row.getDouble("pendingBalance"),row.getDouble("awardedBalance"));
		if(user.getPassword().equals(u.getPassword()))
			return user;
					
		return null;
	}
	
	private HashSet<Role> getRoles(Set<String> strings){
		
		HashSet<Role> roles = strings.stream().map(a -> RoleFactory.getRole(a)).collect(Collectors.toCollection(HashSet::new));
		return roles;
		
	}
	private Set<String> setRoles(HashSet<Role> roles){
		
		Set<String> strings = roles.stream().map(a -> RoleFactory.setRole(a)).collect(Collectors.toSet());
		return strings;
		
	}

}
