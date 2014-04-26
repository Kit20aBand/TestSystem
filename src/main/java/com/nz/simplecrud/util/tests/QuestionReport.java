package com.nz.simplecrud.util.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nz.simplecrud.entity.Variant;

public class QuestionReport {
	private int idQuestion;

	private String question;
	
	private Set<Variant> variants;

	private boolean isCorrect;

	private Set<String> correctAnswers;

	private List<String> userAnswers;

	public int getIdQuestion() {
		return idQuestion;
	}

	public Set<String> getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(Set<String> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}


	public List<String> getUserAnswers() {
		return userAnswers;
	}

	public void setUserAnswers(List<String> userAnswer) {
		this.userAnswers = userAnswer;
	}

	public void setIdQuestion(int idQuestion) {
		this.idQuestion = idQuestion;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public Set<Variant> getVariants() {
		return variants;
	}

	public void setVariants(Set<Variant> variants) {
		this.variants = variants;
	}

	@Override
	public String toString() {
		return "ResultAnswer [idQuestion=" + idQuestion + ", question="
				+ question + ", isCorrect=" + isCorrect + "]";
	}

}
