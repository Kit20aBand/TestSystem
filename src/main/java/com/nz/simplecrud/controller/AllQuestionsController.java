package com.nz.simplecrud.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.RowEditEvent;

import com.nz.simplecrud.datamodels.QuestionDataModel;
import com.nz.simplecrud.entity.Answer;
import com.nz.simplecrud.entity.Question;
import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.entity.Variant;
import com.nz.simplecrud.service.AnswerService;
import com.nz.simplecrud.service.QuestionService;
import com.nz.simplecrud.service.TestService;
import com.nz.simplecrud.service.VariantService;
import com.nz.simplecrud.util.tests.TestUtil;

@Named
@SessionScoped
public class AllQuestionsController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final String THIS_PAGE = "Questions.xhtml";

	private static final String NEW_QUESTION_PAGE = "AddQuestion.xhtml?faces-redirect=true";

	private static final String EDIT_QUESTION_PAGE = "EditQuestion.xhtml?faces-redirect=true";
	
	@Inject
	private TestUtil testUtil;
	
	@Inject
	private TestService testDao;

	@Inject
	private QuestionService questionDao;

	@Inject
	private VariantService variantDao;

	@Inject
	private AnswerService answerDao;

	private QuestionDataModel questionDataModel;

	private List<Question> questions;

	private List<Variant> variants;

	private List<Answer> answers;

	private Test activeTest;

	private List<Question> filteredQuestions;

	private Question[] selectedQuestions;
	
	private List<Integer> questionsCountList = new ArrayList<Integer>();
	
	private boolean activeTestSelected = false;

	@PostConstruct
	void init() {
		try {
			activeTest = getActiveTest();
			activeTestSelected = true;
			findQuestions();
			formQuestionsCountList();
			questionDataModel = new QuestionDataModel(questions);
			
		} catch (EJBException e) {
			//questions = new ArrayList<Question>();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Активный тест не выбран", "Пожалуйста, выберите активный тест в разделе \"Тесты\"");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	private Test getActiveTest() {
		User user = testUtil.findUser();
		Test test = testUtil.findActiveTest(user);
		return test;
	}

	@SuppressWarnings("unchecked")
	private void findQuestions() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("test", activeTest);

		questions = questionDao.findWithNamedQuery(Question.FIND_BY_TEST_ID,
				param);

	}
	
	void formQuestionsCountList() {
		if (activeTest.getTestSettings().getCountOfQuestionsInTest() == 0) {
			activeTest.getTestSettings().setCountOfQuestionsInTest(questions.size());
			testDao.update(activeTest);
		}
		questionsCountList.clear();
		for (int i = 0; i < questions.size(); i++) {
			questionsCountList.add(i+1);
		}
		
	}

	public void deleteQuestions() {
		if (selectedQuestions.length == 0) {
			FacesMessage msg = new FacesMessage("Выберите хотя бы один вопрос!", "Вы выбрали " + selectedQuestions.length + " вопроса");    
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        return;
		}
		questionDao.deleteItems(getSelectedQuestions());
		questions.removeAll(Arrays.asList(selectedQuestions));
		questionDataModel.setData(questions);
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(THIS_PAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		formQuestionsCountList();
		activeTest.getTestSettings().setCountOfQuestionsInTest(questions.size());
		testDao.update(activeTest);
	}

	public String doNewQuestion() {
		return AllQuestionsController.NEW_QUESTION_PAGE;
	}
	
	public String editQuestion() {
		if (selectedQuestions.length != 1) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Выберите только один вопрос!", "Вы выбрали " + selectedQuestions.length + " вопроса");    
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        return null;
		}
		return EDIT_QUESTION_PAGE;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Variant> getVariants() {
		return variants;
	}

	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public List<Question> getFilteredQuestions() {
		return filteredQuestions;
	}

	public void setFilteredQuestions(List<Question> filteredQuestions) {
		this.filteredQuestions = filteredQuestions;
	}

	public Question[] getSelectedQuestions() {
		return selectedQuestions;
	}

	public void setSelectedQuestions(Question[] selectedQuestions) {
		this.selectedQuestions = selectedQuestions;
	}

	public QuestionDataModel getQuestionDataModel() {
		return questionDataModel;
	}

	public boolean isActiveTestSelected() {
		return activeTestSelected;
	}

	public void setActiveTestSelected(boolean activeTestSelected) {
		this.activeTestSelected = activeTestSelected;
	}

	public List<Integer> getQuestionsCountList() {
		return questionsCountList;
	}

}
