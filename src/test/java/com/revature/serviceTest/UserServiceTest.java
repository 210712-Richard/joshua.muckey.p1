package com.revature.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.data.UserDAO;
import com.revature.models.User;
import com.revature.service.UserService;

public class UserServiceTest {
	@InjectMocks
	private static UserService userService;
	@Mock
	private static UserDAO userDao;
	@Mock
	private static User u;
	
	@BeforeEach
	public void setupTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void userCreationTest() {
		
		when(userDao.createUser(u)).thenReturn(u);
		assertEquals(userService.createUser(u), u);
		
	}
	@Test
	public void userUpdateTest() {
		when(userDao.updateUser(u, u)).thenReturn(u);
		assertEquals(userService.updateUser(u, u), u);
		User u1 = Mockito.mock(User.class);
		when(userDao.updateUser(u, u1)).thenReturn(u1);
		assertEquals(userService.updateUser(u, u1), u1);
	}

}
