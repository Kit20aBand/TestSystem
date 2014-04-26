package com.nz.simplecrud.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.nz.simplecrud.entity.Answer;
import com.nz.simplecrud.entity.Question;
import com.nz.simplecrud.entity.Variant;
import com.nz.simplecrud.service.AnswerService;
import com.nz.simplecrud.service.QuestionService;
import com.nz.simplecrud.service.VariantService;
import com.nz.simplecrud.util.tests.TestUtil;
import com.nz.simplecrud.validators.Validator;

@Named
@RequestScoped
public class EditQuestionController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String THIS_PAGE = "Questions.xhtml";

	private static final int MAX_COUNT_OF_TEXTAREAS = 6;
	
	@Inject
	private AllQuestionsController allQuestionsController;
	
	@Inject
	private QuestionService questionDao;
	
	@Inject
	private VariantService variantDao;
	
	@Inject 
	private AnswerService answerDao;
	
	@Inject
	private TestUtil testUtil;
	
	private Question editableQuestion;
	
	@Inject
	@com.nz.simplecrud.annotations.Question
	private Validator validator;

	private List<String> variants = new ArrayList<String>();

	private List<Answer> answers = new ArrayList<Answer>();
	
	private List<String> correctVariants = new ArrayList<String>();
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	void init() {
		editableQuestion = allQuestionsController.getSelectedQuestions()[0];
		
		initVariants();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("question", editableQuestion);
		answers = answerDao.findWithNamedQuery(Answer.SELECT_BY_ID, param);
		
		for (int i = 0; i < MAX_COUNT_OF_TEXTAREAS; i++) {
			correctVariants.add("false");
		}
		
		for (int i = 0; i < variants.size(); i++) {
			String variant = variants.get(i);
			for (int j = 0; j < answers.size(); j++) {
				if (variant.equals(answers.get(j).getAnswer())) {
					correctVariants.set(i, "true");
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void initVariants() {
		//Variant variant = new Variant();
		//variant.setVariant("");
		for (int i = 0; i <MAX_COUNT_OF_TEXTAREAS; i++) {		
			variants.add("");
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("question", editableQuestion);
		List<Variant> vars = new ArrayList<Variant>();
		vars = variantDao.findWithNamedQuery(Variant.ALL, param);
		for (int i = 0; i < vars.size(); i++) {
			variants.set(i, vars.get(i).getVariant());
		}
	}

	public void changeQuestion() {
		String questionText = editableQuestion.getQuestion();
		int index = allQuestionsController.getQuestions().indexOf(editableQuestion);
		int questionId = allQuestionsController.getQuestions().get(index).getId();
		//allQuestionsController.getQuestions().remove(editableQuestion);
		questionDao.delete(editableQuestion.getId());
		
		Question newQuestion;
		newQuestion = new Question();
		newQuestion.setTest(testUtil.findActiveTest(testUtil.findUser()));
		newQuestion.setQuestion(questionText);
		questionDao.create(newQuestion);
		saveVariants(newQuestion);
		saveAnswers(newQuestion);
		questionDao.update(newQuestion);
		
		allQuestionsController.getQuestions().set(index,newQuestion);
		//allQuestionsController.getQuestions().add(newQuestion);
		allQuestionsController.getQuestionDataModel().setData(allQuestionsController.getQuestions());
		//allQuestionsController.init();
		redirect();
	}
	
	private void saveVariants(Question question) {
		Variant newVariant;
		for (String var : variants) {
			if (var.length() > 0) {
				newVariant = new Variant();
				newVariant.setQuestion(question);
				newVariant.setVariant(var);
				variantDao.create(newVariant);
				question.getVariants().add(newVariant);
			}
		}
	}
	
	private void saveAnswers(Question question) {
		Answer answer;
		for (int i = 0 ; i < correctVariants.size(); i++) {
			if ((correctVariants.get(i).equals("true"))
					&& (variants.get(i).length() > 0)) {
				answer = new Answer();
				answer.setQuestion(question);
				answer.setAnswer(variants.get(i));
				answerDao.create(answer);
				question.getAnswers().add(answer);
			}
		}
	}

	private void redirect() {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(THIS_PAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getCorrectVariants() {
		return correctVariants;
	}

	public void setCorrectVariants(List<String> correctVariants) {
		this.correctVariants = correctVariants;
	}

	public Question getEditableQuestion() {
		return editableQuestion;
	}

	public void setEditableQuestion(Question editableQuestion) {
		this.editableQuestion = editableQuestion;
	}

	public List<String> getVariants() {
		return variants;
	}

	public void setVariants(List<String> variants) {
		this.variants = variants;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
}
