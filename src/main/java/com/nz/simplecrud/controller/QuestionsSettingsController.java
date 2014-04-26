package com.nz.simplecrud.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.TestService;
import com.nz.simplecrud.util.tests.TestUtil;

@Named
@RequestScoped
public class QuestionsSettingsController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private TestUtil testUtil;

	@Inject
	private TestService testDao;

	private Test activeTest;

	@PostConstruct
	void init() {
		try {
			activeTest = findActiveTest();
		} catch (EJBException e) {
			// questions = new ArrayList<Question>();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Активный тест не выбран",
					"Пожалуйста, выберите активный тест в разделе \"Тесты\"");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void saveTestSettings() {
		testDao.update(activeTest);
	}

	private Test findActiveTest() {
		User user = testUtil.findUser();
		Test test = testUtil.findActiveTest(user);
		return test;
	}

	public Test getActiveTest() {
		return activeTest;
	}

	public void setActiveTest(Test activeTest) {
		this.activeTest = activeTest;
	}

}
