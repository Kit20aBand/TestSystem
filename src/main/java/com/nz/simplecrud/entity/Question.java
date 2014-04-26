package com.nz.simplecrud.entity;

import java.io.Serializable; 
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({
	@NamedQuery(name = Question.FIND_BY_TEST_ID, query = "SELECT q FROM Question q where q.test = :test"),
	@NamedQuery(name = Question.TOTAL, query = "SELECT COUNT(q) FROM Question q"),
	@NamedQuery(name = Question.ALL, query = "SELECT q FROM Question q")})
public class Question extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ALL = "Question.selectTest";
	
	public static final String FIND_BY_TEST_ID = "Question.findByTestId";
	
	public static final String TOTAL = "Question.total";
	
	@Column(length=1000)
	private String question;
	
	private String typeOfQuestion;
	
	private String url;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="question")
	Set<Variant> variants;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="question")
	Set<Answer> answers;
	
	@ManyToOne
	private Test test;
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Set<Variant> getVariants() {
		return variants;
	}

	public void setVariants(Set<Variant> variants) {
		this.variants = variants;
	}

	public Set<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<Answer> answers) {
		this.answers = answers;
	}

	public String getTypeOfQuestion() {
		return typeOfQuestion;
	}

	public void setTypeOfQuestion(String typeOfQuestion) {
		this.typeOfQuestion = typeOfQuestion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}
	
}
