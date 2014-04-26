package com.nz.simplecrud.service;

import java.util.List;

import javax.ejb.Stateless;

import com.nz.simplecrud.entity.Answer;

@Stateless
public class AnswerService extends DataAccessService<Answer>{

	public AnswerService() {
		super(Answer.class);
	}
	
}
 