package com.nz.simplecrud.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import com.nz.simplecrud.entity.Question;
import com.nz.simplecrud.entity.Result;
import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.TestSettings;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.ResultService;
import com.nz.simplecrud.util.tests.TestUtil;

@Named
@RequestScoped
public class CheckConditionsForTestController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private TestUtil testUtil;
	
	@Inject
	private ResultService resultDao;
	
	private Result currentUsersResult;
	
	private User user;
	
	private User whoCreate;
	
	private Test activeTest;
	
	private boolean checkPassTest;
	
	private boolean noAnyTestCheked = false;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	void init() {
		user = testUtil.findUser();
		whoCreate = testUtil.findWhoCreatedUser(user);
		
		try {
			activeTest = testUtil.findActiveTest(whoCreate);
		} catch(EJBException e) {
			noAnyTestCheked = true;
			FacesMessage msg = new FacesMessage("Прохождение теста недоступно", "Преподаватель не выбрал тест, доступный для прохождения");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if (checkPassTestInTestMode()) {
			findCurrentUsersResult();
			checkPassTest = checkTestConditions();
		}
	}

	public boolean checkActiveTest() {
		if (noAnyTestCheked) {
			return false;
		}
		return true;
	}
	
	private void findCurrentUsersResult() {
		try {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		params.put("test", activeTest);
		currentUsersResult = (Result) resultDao.getOneResult(Result.FIND_BY_USER_ID_AND_TEST_ID, params);
		}
		catch (EJBException e) {
			createNewResultRecord();
		}
	}
	
	private void createNewResultRecord() {
		Result result = new Result();
		result.setTest(activeTest);
		result.setUser(user);
		result.setBestResult(0);
		result.setCountOfAttempts(0);
		resultDao.create(result);
		currentUsersResult = result;
	}
	
	private boolean checkPassTestInTestMode() {
		if (activeTest == null) {
			return false;
		}
		if (!activeTest.getTestSettings().isTestMode()) {
			return false;
		}
		return true;
	}
	
	private boolean checkTestConditions() {
		if (checkNumbersOfTries() & checkDateIntervalForTest()) {
			return true;
		}
		return false;
	}
	
	
	private boolean checkNumbersOfTries() {
		String checkCountOfTries = activeTest.getTestSettings().getCountOfTries();
		int countOfTries;
		if (checkCountOfTries.equals(TestSettings.EMPTY_OR_NOT_VALID_MESSAGE)) {
			return true;
		}
		else {
			countOfTries = Integer.parseInt(activeTest.getTestSettings().getCountOfTries());
		}
		int currentCountOfTries;
		currentCountOfTries = currentUsersResult.getCountOfAttempts();
		if (currentCountOfTries < countOfTries) {
			return true;
		}
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Вы не можете пройти тест", "Вы исчерпали лимит попыток");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return false;
	}
	
	private boolean checkDateIntervalForTest() {
		
		if ((activeTest.getTestSettings().getStartDate() == null) && (activeTest.getTestSettings().getFinishDate() == null)) {
			return true;
		}
		Date currentDate = new Date();
		if (activeTest.getTestSettings().getStartDate() == null) {
			if (currentDate.compareTo(activeTest.getTestSettings().getFinishDate()) <= 1) {
				return true;
			}
			else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Вы не можете пройти тест!", "Время прохождения теста уже истекло!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return false;
			}
		}
		if (activeTest.getTestSettings().getFinishDate() == null) {
			if (currentDate.compareTo(activeTest.getTestSettings().getStartDate()) >= 0) {
				return true;
			}
			else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Вы не можете пройти тест!", "Время прохождения теста еще не пришло!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return false;
			}
		}
		if ((currentDate.compareTo(activeTest.getTestSettings().getStartDate()) >= 0) && 
			 (currentDate.compareTo(activeTest.getTestSettings().getFinishDate()) <= 1))
				return true;
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Вы не можете пройти тест", "Время прохождения теста еще не пришло или уже истекло");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return false;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getWhoCreate() {
		return whoCreate;
	}

	public void setWhoCreate(User whoCreate) {
		this.whoCreate = whoCreate;
	}

	public boolean isCheckPassTest() {
		return checkPassTest;
	}

	public void setCheckPassTest(boolean checkPassTest) {
		this.checkPassTest = checkPassTest;
	}

	public Test getActiveTest() {
		return activeTest;
	}

	public void setActiveTest(Test activeTest) {
		this.activeTest = activeTest;
	}

	public Result getCurrentUsersResult() {
		return currentUsersResult;
	}

	public void setCurrentUsersResult(Result currentUsersResult) {
		this.currentUsersResult = currentUsersResult;
	}
	
}
