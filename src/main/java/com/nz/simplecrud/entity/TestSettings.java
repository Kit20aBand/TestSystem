package com.nz.simplecrud.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Embeddable
public class TestSettings implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String EMPTY_OR_NOT_VALID_MESSAGE = "не ограничено";

	private String countOfMinutes;

	private boolean testMode;

	private String countOfTries;

	private Date startDate;

	private Date finishDate;
	
	private boolean shuffleQuestions;
	
	private boolean showCountOfCorrectAnswers;
	
	private boolean checkAllAnswers;
	
	private int countOfQuestionsInTest;

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getCountOfMinutes() {
		return countOfMinutes;
	}

	public void setCountOfMinutes(String countOfMinutes) {
		if (countOfMinutes == "") {
			this.countOfMinutes = EMPTY_OR_NOT_VALID_MESSAGE;
			return;
		}
		try {
			Integer.parseInt(countOfMinutes);
		} catch (NumberFormatException e) {
			this.countOfMinutes = EMPTY_OR_NOT_VALID_MESSAGE;
			return;
		}
		this.countOfMinutes = countOfMinutes;
	}

	public String getCountOfTries() {
		return countOfTries;
	}

	public void setCountOfTries(String countOfTries) {
		if (countOfTries == "") {
			this.countOfTries = EMPTY_OR_NOT_VALID_MESSAGE;
			return;
		}
		try {
			Integer.parseInt(countOfTries);
		} catch (NumberFormatException e) {
			this.countOfTries = EMPTY_OR_NOT_VALID_MESSAGE;
			return;
		}
		this.countOfTries = countOfTries;
	}

	public boolean isShuffleQuestions() {
		return shuffleQuestions;
	}

	public void setShuffleQuestions(boolean shuffleQuestions) {
		this.shuffleQuestions = shuffleQuestions;
	}

	public boolean isShowCountOfCorrectAnswers() {
		return showCountOfCorrectAnswers;
	}

	public void setShowCountOfCorrectAnswers(boolean showCountOfCorrectAnswers) {
		this.showCountOfCorrectAnswers = showCountOfCorrectAnswers;
	}

	public boolean isCheckAllAnswers() {
		return checkAllAnswers;
	}

	public void setCheckAllAnswers(boolean checkAllAnswers) {
		this.checkAllAnswers = checkAllAnswers;
	}

	public int getCountOfQuestionsInTest() {
		return countOfQuestionsInTest;
	}

	public void setCountOfQuestionsInTest(int countOfQuestionsInTest) {
		this.countOfQuestionsInTest = countOfQuestionsInTest;
	}
	
	
}
