package com.nz.simplecrud.util.tests;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;

import com.nz.simplecrud.entity.Question;

@SessionScoped
public class UserTestSettings implements Serializable {
	private static final long serialVersionUID = 1L;

	List<Question> questions;
	
	public List<Question> getQuestions() {
		return questions;
	}
	
	public void setQuestions(List<Question> questions) {
		if (this.questions == null)
			this.questions = questions;
	}
}
