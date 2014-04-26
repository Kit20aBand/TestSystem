package com.nz.simplecrud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Variant;

import com.nz.simplecrud.entity.Answer;
import com.nz.simplecrud.entity.Question;
import com.nz.simplecrud.entity.Result;
import com.nz.simplecrud.util.tests.QuestionReport;

@Named
@RequestScoped
public class SubmitController implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String USER_RESULTS = "/user/Results.xhtml";

	@Inject
	private RunTestController runTestController;

	private List<String> userAnswersToCurrentQuestion;

	private int countOfQuestions;

	private int score;

	private double scoreInPercent;

	private int countOfEmptyResponces = 0;

	private Map<Integer, List<String>> userAnswersToEachTest = new HashMap<Integer, List<String>>();

	private Map<Integer, List<String>> correctAnswersToEachTest = new HashMap<Integer, List<String>>();

	private List<QuestionReport> questionsReport = new ArrayList<QuestionReport>();


	
	public void setUserAnswersToCurrentQuestion(
			List<String> userAnswersToCurrentQuestion) {
		this.userAnswersToCurrentQuestion = userAnswersToCurrentQuestion;
		setAnswersToMaps(userAnswersToCurrentQuestion);
		System.out.println(userAnswersToCurrentQuestion + " userAnswers");
		if (userAnswersToCurrentQuestion.size() == 0) {
			countOfEmptyResponces++;
		}
	}

	private void setAnswersToMaps(List<String> userAnswersToCurrentQuestion) {
		int id = runTestController.getCurrentIndex();

		List<Answer> list = runTestController
				.getCorrectAnswersToCurrentQuestion();

		correctAnswersToEachTest.put(id, getCorrectAnswersInString(list));
		userAnswersToEachTest.put(id, userAnswersToCurrentQuestion);

		setReport(runTestController.getQuestion());
	}

	private void setReport(Question question) {
		QuestionReport questionReport = new QuestionReport();
		questionReport.setQuestion(question.getQuestion());
		questionReport.setIdQuestion(question.getId());
		questionReport.setVariants(question.getVariants());
		questionReport.setCorrectAnswers(getCorrectAnswersInString(question
				.getAnswers()));
		questionReport.setCorrect(false);
		questionsReport.add(questionReport);
		System.out.println(questionReport);
	}

	private Set<String> getCorrectAnswersInString(Set<Answer> set) {

		Set<String> stringAnswersSet = new HashSet<String>();
		Iterator<Answer> iterator = set.iterator();
		while (iterator.hasNext()) {
			stringAnswersSet.add(iterator.next().getAnswer());
		}
		return stringAnswersSet;
	}

	private List<String> getCorrectAnswersInString(List<Answer> list) {
		List<String> stringAnswersList = new ArrayList<String>();
		Iterator<Answer> iterator = list.iterator();
		while (iterator.hasNext()) {
			stringAnswersList.add(iterator.next().getAnswer());
		}
		return stringAnswersList;
	}

	public String checkUserAnswers() {
		System.out.println(userAnswersToEachTest);
		System.out.println(correctAnswersToEachTest);
		if (runTestController.getActiveTest().getTestSettings()
				.isCheckAllAnswers()) {
			if (countOfEmptyResponces > 0) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Вы ответили не на все тесты. ",
								"Пожалуйста, проверьте свои ответы!"));
				countOfEmptyResponces = 0;
				return null;
			}
		}
		boolean isCorrect = false;
		List<String> list = new ArrayList<String>();
		list.add("Вы не ответили");
		for (int i = 0; i < userAnswersToEachTest.size(); i++) {
			if (userAnswersToEachTest.get(i) == null) {
				userAnswersToEachTest.put(i, list);
			}
			if (userAnswersToEachTest.get(i).equals(
					correctAnswersToEachTest.get(i)) && (userAnswersToEachTest.size() == correctAnswersToEachTest.size())) {
				isCorrect = true;
				score++;
			}
			QuestionReport resultAnswer = questionsReport.get(i);
			System.out.println(isCorrect);
			resultAnswer.setCorrect(isCorrect);
			resultAnswer.setUserAnswers(userAnswersToEachTest.get(i));
			// resultAnswer.setCorrectAnswer(answersToEachTest.get(i));
			questionsReport.set(i, resultAnswer);
			isCorrect = false;
		}
		scoreInPercent = score
				/ (double) runTestController.getCountOfQuestions() * 100;
		return USER_RESULTS;
	}

	public void reset() {
		score = 0;
		userAnswersToCurrentQuestion.clear();
		correctAnswersToEachTest.clear();
		questionsReport.clear();
	}

	/**
	 * GETTERS/SETTERS
	 * 
	 */
	public int getScore() {
		return score;
	}

	public double getScoreInPercent() {
		return scoreInPercent;
	}

	public Map<Integer, List<String>> getUserAnswersToEachTest() {
		return userAnswersToEachTest;
	}

	public Map<Integer, List<String>> getAnswersToEachTest() {
		return correctAnswersToEachTest;
	}

	public List<String> getUserAnswersToCurrentQuestion() {
		return userAnswersToCurrentQuestion;
	}

	public List<QuestionReport> getQuestionsReport() {
		return questionsReport;
	}

	public void setQuestionsReport(List<QuestionReport> questionsReport) {
		this.questionsReport = questionsReport;
	}

	public int getCountOfQuestions() {
		return runTestController.getCountOfQuestions();
	}
}
