package com.nz.simplecrud.util.tests;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.nz.simplecrud.controller.LoginController;
import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.TestService;
import com.nz.simplecrud.service.UserService;

@RequestScoped
public class TestUtil implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject 
	LoginController loginController;
	
	@Inject
	private TestService testDao;
	
	@Inject
	private UserService userDao;
	
	public Test findActiveTest(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		params.put("isActive", true);
		return (Test) testDao.getOneResult(Test.FIND_ACTIVE_TEST, params);
	}
	
	public User findUser() {
		String username = loginController.getUsername();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		User user = (User) userDao.getOneResult(User.FIND_BY_USERNAME, params);
		return user;
	}
	
	public User findWhoCreatedUser(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", user.getWhoCreated());
		return (User) userDao.getOneResult(User.FIND_BY_USERNAME, params);
	}
}
