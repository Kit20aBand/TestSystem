package com.nz.simplecrud.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.nz.simplecrud.service.QuestionService;
import com.nz.simplecrud.service.UserService;

@Named
@SessionScoped
public class AdditionalContentController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private UserService userDao;
	
	@Inject
	private QuestionService questionDao;

	public int getCountOfTeachers() {
		return userDao.getCountOfTeachers();
	}

	public int getCountOfStudents() {
		return userDao.getCountOfStudents();
	}

	public int getCountOfQuestions() {
		return questionDao.getCountOfQuestions();
	}
	
}
