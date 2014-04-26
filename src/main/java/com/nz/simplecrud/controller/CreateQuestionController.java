package com.nz.simplecrud.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

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
import com.nz.simplecrud.validators.Validator;

@Named
@RequestScoped
public class CreateQuestionController implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int MAX_COUNT_OF_TEXTAREAS = 6;

	private static final String THIS_PAGE = "Questions.xhtml";

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
	
	@Inject
	private AllQuestionsController allQuestionsController;
	
	@Inject
	@com.nz.simplecrud.annotations.Question
	private Validator validator;

	private Question question;

	private String questionText;

	private List<String> variants = new ArrayList<String>();

	private List<String> answers = new ArrayList<String>();

	public CreateQuestionController() {
		for (int i = 0; i < MAX_COUNT_OF_TEXTAREAS; i++) {
			variants.add("");
			answers.add("false");
		}
	}

	public void addQuestion() {

		// validate();
		// if (questionValidator.isAllCorrect()) {
		Test activeTest = getActiveTest();
		question = new Question();
		question.setTest(activeTest);
		question.setQuestion(questionText);
		questionDao.create(question);
		saveVariants(question);
		saveAnswers(question);
		questionDao.update(question);

		activeTest.getQuestions().add(question);
		testDao.update(activeTest);

		
		allQuestionsController.getQuestions().add(question);
		allQuestionsController.getQuestionDataModel().setData(allQuestionsController.getQuestions());
		allQuestionsController.formQuestionsCountList();
		activeTest.getTestSettings().setCountOfQuestionsInTest(allQuestionsController.getQuestions().size());
		testDao.update(activeTest);

		redirect();
		// }

	}

	private Test getActiveTest() {
		User user = testUtil.findUser();
		Test test = testUtil.findActiveTest(user);
		return test;
	}

	private void redirect() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(THIS_PAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void validateEmptyAnswers(ComponentSystemEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();

		UIComponent components = event.getComponent();

		UIInput uiInputQuestionText = (UIInput) components
				.findComponent("editor");
		if (uiInputQuestionText.equals("")) {
			addMessage("Вы не ввели вопрос!", "Пожалуйста, введите вопрос!");
			fc.renderResponse();
		}
		
		List<String> variants = new ArrayList<String>();
		for (int i = 0; i < MAX_COUNT_OF_TEXTAREAS; i++) {
			UIInput uiInputVariant = (UIInput) components
					.findComponent("variant" + i);
			String var = uiInputVariant.getLocalValue() == null ? ""
					: uiInputVariant.getLocalValue().toString();
			variants.add(var);
		}
	
		if (variants.get(0).equals("")) {
			addMessage("Вы не ввели первый вариант!", "Пожалуйста, введите первый вариант!");
			fc.renderResponse();
		}
		if (variants.get(1).equals("")) {
			addMessage("Вы не ввели второй вариант!", "Пожалуйста, введите второй вариант!");
			fc.renderResponse();
		}
		List<Boolean> answers = new ArrayList<Boolean>();
		for (int i = 0; i < MAX_COUNT_OF_TEXTAREAS; i++) {
			UIInput uiInputАnswer = (UIInput) components
					.findComponent("answer" + i);
			answers.add((Boolean.parseBoolean(uiInputАnswer.getLocalValue().toString())));
			System.out.println(Boolean.parseBoolean(uiInputАnswer.getLocalValue().toString()));
		}
		for (int i = 0; i < MAX_COUNT_OF_TEXTAREAS; i++) {
			if ((answers.get(i) == true) && variants.get(i) == "") {
				//checkEmtyVariantsWithCorrectQuestions(components, answers);
				//return;
				addMessage("Ошибка ввода!", "Вы поставили правильный ответ на несуществующий(пустой) вариант!");
				fc.renderResponse();
			}
		}
		int countOfCorrect = 0;
		for (int i = 0; i < answers.size(); i++) {
			if (answers.get(i)) {
				countOfCorrect++;
			}
		}
		if (countOfCorrect == 0) {
			addMessage("Вы не ввели ни одного правильного ответа!", "Пожалуйста, введите хотя бы один правильный ответ!");
			fc.renderResponse();
		}
	}
	
	private void saveVariants(Question question) {
		Variant variant;
		for (String variantStr : variants) {
			if (variantStr.length() > 0) {
				variant = new Variant();
				variant.setQuestion(question);
				variant.setVariant(variantStr);
				variantDao.create(variant);
				question.getVariants().add(variant);
			}
		}
	}

	private void saveAnswers(Question question) {
		Answer answer;
		for (int i = 0; i < answers.size(); i++) {
			if ((answers.get(i).equals("true"))
					&& (variants.get(i).length() > 0)) {
				answer = new Answer();
				answer.setQuestion(question);
				answer.setAnswer(variants.get(i));
				answerDao.create(answer);
				question.getAnswers().add(answer);
			}
		}
	}

	private void addMessage(String mainInfo, String supportInfo) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				mainInfo, supportInfo);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public List<String> getVariants() {
		return variants;
	}

	public void setVariants(List<String> variants) {
		this.variants = variants;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}


	
}
