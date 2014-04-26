package com.nz.simplecrud.service;

import java.util.HashMap;
import java.util.Map;

import com.nz.simplecrud.entity.Result;
import com.nz.simplecrud.entity.User;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Emre Simtay <emre@simtay.com>
 */

@Stateless
public class UserService extends DataAccessService<User>{
    
    public UserService(){
        super(User.class);
    }   
    
    public User findByUsername(String username) {
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		User user = (User) getOneResult(User.FIND_BY_USERNAME, params);
		return user;
    }
    
    public User findByEmail(String email) {
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		User user = (User) getOneResult(User.FIND_BY_EMAIL, params);
		return user;
    }
    
    public int getCountOfTeachers() {
		int countOfTeachers = countTotalRecord(User.TOTAL_TEACHERS);
		return countOfTeachers;
    }
    
    public int getCountOfStudents() {
    	int countOfStudents = countTotalRecord(User.TOTAL_STUDENTS);
		return countOfStudents;
    }
}
