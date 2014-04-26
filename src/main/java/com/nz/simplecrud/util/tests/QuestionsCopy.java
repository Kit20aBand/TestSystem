package com.nz.simplecrud.util.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nz.simplecrud.entity.Question;

public class QuestionsCopy {
	private List<Question> questions = new ArrayList<Question>();
	
	private int position;
	
	public QuestionsCopy(List<Question> questions) {
		for (Question question: questions) {
			this.questions.add(question);
		}
	}
	
	public Question getQuestion() {
			return questions.get(position++);
	}
	
	public int getPosition() {
		return position;
	}
}
