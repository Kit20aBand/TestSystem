package com.nz.simplecrud.util.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.Position;

import com.nz.simplecrud.entity.Question;

public class QuestionsCopyContainer {
	private Map<String, QuestionsCopy> map = new HashMap<String, QuestionsCopy>();
	
	private QuestionsCopy questionsCopy;
	
	public void addCopy(String key, List<Question> value) {
		QuestionsCopy questionsCopy = new QuestionsCopy(value);
		map.put(key, questionsCopy);
	}
	
	public Question getCopy(String key) {
		QuestionsCopy questionsCopy = map.get(key);
		return questionsCopy.getQuestion();
	}
	
	public int getIndex(String key) {
		QuestionsCopy questionsCopy = map.get(key);
		return questionsCopy.getPosition();
	}
}
