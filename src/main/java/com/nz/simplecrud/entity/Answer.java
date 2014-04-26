package com.nz.simplecrud.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = Answer.ALL, query = "SELECT a FROM Answer a"),
    @NamedQuery(name = Answer.SELECT_BY_ID, query = "SELECT a FROM Answer a where a.question = :question"),
    @NamedQuery(name = Answer.TOTAL, query = "SELECT COUNT(a) FROM Answer a where a.question = :question")})
public class Answer extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String SELECT_BY_ID = "Answer.getAnswersByQuestionId";
	
	public static final String ALL = "Answer.getAllAnswersToThisTest";
	
	public static final String TOTAL = "Answer.getCountOfAnswersToThisTest";
	
	@Column(length=1000)
	private String answer;
	
	@ManyToOne
	private Question question;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		return answer;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
	
}
