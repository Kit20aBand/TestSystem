package com.nz.simplecrud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.RowEditEvent;

import com.nz.simplecrud.datamodels.TestDataModel;
import com.nz.simplecrud.datamodels.UserDataModel;
import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.TestSettings;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.QuestionService;
import com.nz.simplecrud.service.TestService;
import com.nz.simplecrud.service.UserService;
import com.nz.simplecrud.util.tests.TestUtil;
import com.nz.simplecrud.validators.Validator;

@Named
@SessionScoped
public class AllTestsController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private transient Logger logger;
	
	@Inject
	private AllQuestionsController allQuestionsController;

	@Inject
	private TestService testDao;
	
	@Inject
	private QuestionService questionDao;

	@Inject
	private UserService userDao;
	
	@Inject
	private TestUtil testUtil;
	
	@Inject
	@com.nz.simplecrud.annotations.Test
	private Validator validator;

	private TestDataModel testDataModel;

	private Test newTest = new Test();

	private Test selectedMainTest;

	private List<Test> tests;

	private List<Test> filteredTests;

	private Test[] selectedTests;
	
	private TestSettings testSettings = new TestSettings();
	
	private HtmlInputText inputComponent = new HtmlInputText();

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", testUtil.findUser());
		tests = testDao.findWithNamedQuery(Test.FIND_BY_ID, params);
		testDataModel = new TestDataModel(tests);

	}

	public void chooseTest() {
		if (selectedTests.length != 1) {
			FacesMessage msg = new FacesMessage("Выберите только один тест", "Вы выбрали " + selectedTests.length + " теста");    
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        return;
		}
		Test choosenTest = testDao.find(selectedTests[0].getId());
		
		resetActiveTest();
		
		choosenTest.setActive(true);
		testDao.update(choosenTest);
		allQuestionsController.init();
	}
	
	public void resetActiveTest() {
		List<Test> allTests = testDao.findWithNamedQuery(Test.ALL);
		User whoCreateTest = testUtil.findUser();
		System.out.println(whoCreateTest);
		for (Test test: allTests) {
			System.out.println(test);
			if ((test.isActive()) && (test.getUser().equals(whoCreateTest))) {
				test.setActive(false);
				testDao.update(test);
			}
		}
	}
	
	public String findActiveTest() {
		List<Test> allTests = testDao.findWithNamedQuery(Test.ALL);
		User whoCreateTest = testUtil.findUser();
		for (Test test: allTests) {
			if ((test.isActive()) && (test.getUser().equals(whoCreateTest))) {
				return test.getSubject() + ", " + test.getTopic();
			}
		}
		return "не выбран";
	}
	
	public void addTest() {

		newTest.setTestSettings(testSettings);
		
		newTest.setTeacherName(testUtil.findUser().getLastname());
		
		Test testToAdd = new Test(newTest);
		
		testToAdd.setUser(testUtil.findUser());
		
		testToAdd.setTestSettings(testSettings);
		
		testDao.create(testToAdd);
		tests.add(testToAdd);
		testDataModel.setData(tests);
	}

	public void deleteTests() {
		testDao.deleteItems(selectedTests);
		tests.removeAll(new ArrayList<Test>(Arrays.asList(selectedTests)));
		testDataModel.setData(tests);
	}
 
	public void onEdit(RowEditEvent event) {
		Test updatedTest = (Test) event.getObject();
        testDao.update(updatedTest);
	}

	public void onCancel(RowEditEvent event) {
		/*FacesMessage msg = new FacesMessage("Редактирование отменено", "");  
        FacesContext.getCurrentInstance().addMessage(null, msg);*/
	} 
	
	/*public void resetValues() {
		RequestContext.getCurrentInstance().reset("newTestForm:displayNewTest");
	}*/
	/**
	 * GETTERS/SETTERS 
	 */
	
	public TestDataModel getTestDataModel() {
		return testDataModel;
	}
 
	public List<Test> getTests() {
		return tests;
	}

	public List<Test> getFilteredTests() {
		return filteredTests;
	}

	public void setFilteredTests(List<Test> filteredTests) {
		this.filteredTests = filteredTests;
	}

	public Test[] getSelectedTests() {
		return selectedTests;
	}

	public void setSelectedTests(Test[] selectedTests) {
		this.selectedTests = selectedTests;
	}

	public Test getNewTest() {
		return newTest;
	}

	public void setSelectedMainTest(Test selectedMainTest) {
		this.selectedMainTest = selectedMainTest;
	}

	public Test getSelectedMainTest() {
		return selectedMainTest;
	}

	public TestSettings getTestSettings() {
		return testSettings;
	}

	public void setTestSettings(TestSettings testSettings) {
		this.testSettings = testSettings;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public HtmlInputText getInputComponent() {
		return inputComponent;
	}

	public void setInputComponent(HtmlInputText inputComponent) {
		this.inputComponent = inputComponent;
	}
	
}
