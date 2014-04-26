package com.nz.simplecrud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.nz.simplecrud.entity.TestSettings;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.TestService;
import com.nz.simplecrud.service.TestSettingsService;
import com.nz.simplecrud.util.tests.QuestionReport;
import com.nz.simplecrud.util.tests.TestUtil;

@Named
@RequestScoped
public class ShowResultToUserController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private SubmitController submitController;

	@Inject
	private SaveResultController saveResultController;

	@Inject
	private TestUtil testUtil;
	
	@Inject
	private CheckConditionsForTestController checkConditionsForTestController;

	private List<QuestionReport> questionsReport;

	private TestSettings testSettings;

	@PostConstruct
	private void init() {
		questionsReport = submitController.getQuestionsReport();

		User user = testUtil.findUser();
		User whoCreate = testUtil.findWhoCreatedUser(user);
		testSettings = testUtil.findActiveTest(whoCreate).getTestSettings();

		if (testSettings.isTestMode()) {
			saveResultController.saveResult();
		}
	}

	
	
	public List<QuestionReport> getQuestionsReport() {
		if (testSettings.isTestMode()) {
			return null;
		}
		return questionsReport;
	}

	public TestSettings getTestSettings() {
		return testSettings;
	}

}
