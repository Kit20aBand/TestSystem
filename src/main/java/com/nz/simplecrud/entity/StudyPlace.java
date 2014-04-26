package com.nz.simplecrud.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Embeddable
public class StudyPlace implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(length = 50)
	private String educationInstitution;

	@Column(length = 50)
	private String course;

	@Column(length = 50)
	private String studyGroup;
	
	@ManyToOne
	private User user;

	public StudyPlace() {
	}

	public String getEducationInstitution() {
		return educationInstitution;
	}

	public void setEducationInstitution(String educationInstitution) {
		if (educationInstitution != null) {
			this.educationInstitution = educationInstitution;
		}
		else {
			this.educationInstitution = "";
		}
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		if (course != null) {
			this.course = course;
		}
		else {
			this.course = "";
		}
	}

	public String getStudyGroup() {
		return studyGroup;
	}

	public void setStudyGroup(String studyGroup) {
		if (course != null) {
			this.studyGroup = studyGroup;
		}
		else {
			this.studyGroup = "";
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}