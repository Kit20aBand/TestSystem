package com.nz.simplecrud.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class UserResultController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SubmitController submitController;
	
	private double score;
	
	private int scoreInPercent;
	
	public int getScore() {
		return submitController.getScore();
	}
	
	public double getScoreInPercent() {
		return submitController.getScoreInPercent();
	}
}
