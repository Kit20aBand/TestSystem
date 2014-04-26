package com.nz.simplecrud.service;

import javax.ejb.Stateless;

import com.nz.simplecrud.entity.Question;
import com.nz.simplecrud.entity.User;

@Stateless
public class QuestionService extends DataAccessService<Question>{

	public QuestionService() {
		super(Question.class);
	}
	
	public int getCountOfQuestions() {
    	int countOfQuestions = countTotalRecord(Question.TOTAL);
		return countOfQuestions;
    }
}
