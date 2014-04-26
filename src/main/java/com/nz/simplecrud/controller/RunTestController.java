package com.nz.simplecrud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.nz.simplecrud.entity.Answer;
import com.nz.simplecrud.entity.Question;
import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.TestSettings;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.AnswerService;
import com.nz.simplecrud.service.QuestionService;
import com.nz.simplecrud.service.TestService;
import com.nz.simplecrud.service.TestSettingsService;
import com.nz.simplecrud.service.UserService;
import com.nz.simplecrud.util.tests.QuestionsCopyContainer;
import com.nz.simplecrud.util.tests.TestUtil;
import com.nz.simplecrud.util.tests.UserTestSettings;

import org.apache.commons.lang3.StringEscapeUtils;

@Named
@RequestScoped
public class RunTestController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private QuestionService question_das;
	
	@Inject
	private AnswerService answer_das;
	
	@Inject
	private UserTestSettings userTestSettings;
	
	@Inject
	private TestUtil testUtil;
	
	private Test activeTest;
	
	private List<Question> questions;
	
	private int countOfQuestions;
	
	private QuestionsCopyContainer questionsCopyContainer = new QuestionsCopyContainer();
	

	@PostConstruct
	private void init() {
		activeTest = findActiveTest();
		
		Map<String, Object> params = new HashMap<String, Object>();
		User user = testUtil.findUser();
		User whoCreate = testUtil.findWhoCreatedUser(user);
		params.put("test", testUtil.findActiveTest(whoCreate));
		questions = question_das.findWithNamedQuery(Question.FIND_BY_TEST_ID, params);
		
		checkTestSettings();
		userTestSettings.setQuestions(questions);
		questions = userTestSettings.getQuestions();
		
		countOfQuestions = questions.size();
		questionsCopyContainer.addCopy("common", questions);
		questionsCopyContainer.addCopy("forCount", questions);
		questionsCopyContainer.addCopy("forOneOrMany", questions);
		questionsCopyContainer.addCopy("forShowResult", questions);
		questionsCopyContainer.addCopy("forGetVariants", questions);

	}
	
	private void  checkTestSettings() {
		boolean isShuffle = false;
		if (activeTest.getTestSettings().isShuffleQuestions()) {
			Collections.shuffle(questions);
			isShuffle = true;
		}
		if (activeTest.getTestSettings().getCountOfQuestionsInTest() != questions.size()) {
			if (!isShuffle)
				Collections.shuffle(questions);
			List<Question> settingCollection = new ArrayList<Question>();
			for (int i = 0; i < activeTest.getTestSettings().getCountOfQuestionsInTest(); i++) {
				settingCollection.add(questions.get(i));
			}
			questions = settingCollection;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Question> getQuestions() {
		return questions;
	}
	
	public List<Answer> getCorrectAnswersToCurrentQuestion() {
		Map<String, Object> questionParam = new HashMap<String, Object>();
		questionParam.put("question", questionsCopyContainer.getCopy("common"));
		return answer_das.findWithNamedQuery(Answer.SELECT_BY_ID, questionParam);
	}	
	
	public int getCountOfAnswers() {
		Map<String, Object> questionParam = new HashMap<String, Object>();
		questionParam.put("question", questionsCopyContainer.getCopy("forCount"));
		int result = answer_das.countTotalRecord(Answer.TOTAL, questionParam);
		return result;
	}
	

	
	private Test findActiveTest() {
		User user = testUtil.findUser();
		User whoCreate = testUtil.findWhoCreatedUser(user);
		Test test = testUtil.findActiveTest(whoCreate);
		return test;
	}
	
	public int getCurrentIndex() {
		return questionsCopyContainer.getIndex("common");
	}
	
	public Question getQuestion() {
		return questionsCopyContainer.getCopy("forShowResult");
	}
	
	public int getCountOfQuestions() {
		return countOfQuestions;
	}

	public void setCountOfQuestions(int countOfQuestions) {
		this.countOfQuestions = countOfQuestions;
	}

	public Test getActiveTest() {
		return activeTest;
	}


}
