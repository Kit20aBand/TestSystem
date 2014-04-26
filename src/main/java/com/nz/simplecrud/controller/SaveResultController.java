package com.nz.simplecrud.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.nz.simplecrud.entity.Result;
import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.TestSettings;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.ResultService;
import com.nz.simplecrud.service.UserService;
import com.nz.simplecrud.util.tests.TestUtil;

@Named
@RequestScoped
public class SaveResultController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private SubmitController submitController;

	@Inject
	private TestSettings testSettings;

	@Inject
	private ResultService resultDao;

	@Inject
	private UserService userDao;

	@Inject
	private TestUtil testUtil;

	private Test activeTest;

	List<Result> results;

	private double scoreInPercent;

	private User user;

	private Result currentResult;

	@PostConstruct
	private void init() {
		scoreInPercent = submitController.getScoreInPercent();
		user = testUtil.findUser();
		activeTest = testUtil.findActiveTest(testUtil.findWhoCreatedUser(user));
		/*
		 * System.out.println(user.getResult()); if (user.getResult() == null) {
		 * 
		 * //resultDao.create(result); user.setResult(new Result());
		 * userDao.update(user); }
		 */
	}

	@SuppressWarnings("unchecked")
	public void saveResult() {
		User whoCreate = testUtil.findWhoCreatedUser(user);
		/*Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		results = resultDao.findWithNamedQuery(Result.FIND_BY_USER_ID, params);*/
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		params.put("test", activeTest);
		currentResult = (Result) resultDao.getOneResult(Result.FIND_BY_USER_ID_AND_TEST_ID, params);
		currentResult.setWhoCreated(whoCreate.getUsername());
		
		updateAttempts();
		updateBestResult();
		resultDao.update(currentResult);
			
		/*
		 * result.setSubject(testUtil.findActiveTest(whoCreate).getSubject());
		 * result.setTopic(testUtil.findActiveTest(whoCreate).getTopic());
		 * updateAttempts(); updateBestResult(); user.setResult(result);
		 * userDao.update(user);
		 */
	}

	private void updateAttempts() {
		int countOfAttempts = currentResult.getCountOfAttempts();
		currentResult.setCountOfAttempts(++countOfAttempts);
	}

	private void updateBestResult() {
		int bestResult = currentResult.getBestResult();
		if (submitController.getScoreInPercent() > bestResult) {
			currentResult.setBestResult((int) submitController
					.getScoreInPercent());
		}
	}

}
