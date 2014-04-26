package com.nz.simplecrud.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({
	@NamedQuery(name = Result.FIND_BY_WHO_CREATED, query = "SELECT r FROM Result r where r.whoCreated = :whoCreated"),
	@NamedQuery(name = Result.FIND_BY_USER_ID_AND_TEST_ID, query = "SELECT r FROM Result r where r.user = :user AND r.test = :test"),
	@NamedQuery(name = Result.FIND_BY_USER_ID, query = "SELECT r FROM Result r where r.user = :user"),
	@NamedQuery(name = Result.FIND_BY_TEST_ID, query = "SELECT r FROM Result r where r.test = :test")})
public class Result extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_WHO_CREATED = "Result.findByWhoCreated";
	
	public static final String FIND_BY_USER_ID_AND_TEST_ID = "Result.findByUserIdAndTestId";
	
	public static final String FIND_BY_USER_ID = "Result.findByUserId";
	
	public static final String FIND_BY_TEST_ID = "Result.findByTestId";
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Test test;
	
	@Column(nullable=true)
	private int bestResult;
	
	@Column(nullable=true)
	private int countOfAttempts;
	
    private String whoCreated;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getBestResult() {
		return bestResult;
	}

	public void setBestResult(int bestResult) {
		this.bestResult = bestResult;
	}

	public int getCountOfAttempts() {
		return countOfAttempts;
	}

	public void setCountOfAttempts(int countOfAttempts) {
		this.countOfAttempts = countOfAttempts;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public String getWhoCreated() {
		return whoCreated;
	}

	public void setWhoCreated(String whoCreated) {
		this.whoCreated = whoCreated;
	}
	
}
