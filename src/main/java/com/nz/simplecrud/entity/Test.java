package com.nz.simplecrud.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@NamedQueries({
		@NamedQuery(name = Test.FIND_BY_ID, query = "SELECT t FROM Test t where t.user = :user"),
		@NamedQuery(name = Test.FIND_ACTIVE_TEST, query = "SELECT t FROM Test t where t.user = :user AND t.isActive = :isActive"),
		@NamedQuery(name = Test.ALL, query = "SELECT t FROM Test t ") })
public class Test extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ALL = "Test.getAllRecords";
	
	public static final String FIND_BY_ID = "Test.findByUserId";
	
	public static final String FIND_ACTIVE_TEST = "Test.findActiveTest";
	
	private String subject;

	private String teacherName;

	private String topic;
	
	private boolean isActive;

	@ManyToOne
	private User user;
	
	@Embedded
	private TestSettings testSettings;
	
	public Test() {
	}
	
	public Test(Test test) {
		subject = test.subject;
		teacherName = test.teacherName;
		topic = test.topic;
		user = test.user;
		testSettings = test.testSettings;
	}

	@OneToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER, mappedBy = "test")
	private Set<Question> questions;
	
	@OneToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER, mappedBy = "test")
	private Set<Result> results;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public TestSettings getTestSettings() {
		return testSettings;
	}

	public void setTestSettings(TestSettings testSettings) {
		this.testSettings = testSettings;
	}
	
}
