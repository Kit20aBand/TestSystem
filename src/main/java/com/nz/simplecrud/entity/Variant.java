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
    @NamedQuery(name = Variant.ALL, query = "SELECT v FROM Variant v where v.question = :question")})
public class Variant extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ALL = "Variant.getAllVariantsToThisQuestion";
	
	@Column(length=1000)
	private String variant;
	
	@ManyToOne
	private Question question;
	
	public Variant() {
		
	}
	
	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@Override
	public String toString() {
		return variant;
	}

	
}
