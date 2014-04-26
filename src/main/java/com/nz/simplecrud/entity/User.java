package com.nz.simplecrud.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.Email;


@Entity
@NamedQueries({
    @NamedQuery(name = User.ALL, query = "SELECT u FROM User u "),
    @NamedQuery(name = User.FIND_BY_USERNAME, query = "SELECT u FROM User u where u.username = :username"),
    @NamedQuery(name = User.FIND_BY_WHO_CREATED, query = "SELECT u FROM User u where u.whoCreated = :whoCreated"),
    @NamedQuery(name = User.FIND_BY_EMAIL, query = "SELECT u FROM User u where u.email = :email"),
    @NamedQuery(name = User.FIND_TEACHERS, query = "SELECT u FROM User u where u.whoCreated = 'manager'"),
    @NamedQuery(name = User.TOTAL_TEACHERS, query = "SELECT COUNT(u) FROM User u where u.whoCreated = 'manager'"),
    @NamedQuery(name = User.TOTAL_STUDENTS, query = "SELECT COUNT(u) FROM User u where u.whoCreated <> 'manager'"),
    @NamedQuery(name = User.TOTAL, query = "SELECT COUNT(u) FROM User u")})
public class User extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final static String ALL = "User.populateUsers";
    public final static String TOTAL = "User.countUsersTotal";
    public final static String TOTAL_STUDENTS = "User.countStudents";
    public final static String TOTAL_TEACHERS = "User.countTeachers";
    public final static String FIND_BY_USERNAME = "User.findByUsername";
    public final static String FIND_BY_EMAIL = "User.findByEmail";
    public final static String FIND_BY_WHO_CREATED = "User.findByWhoCreated";
    public final static String FIND_TEACHERS = "User.findTeachers";
    
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(length = 50)
    private String firstname;
    
    @Column(length = 50)
    private String lastname;
    
    @Embedded
    private StudyPlace studyPlace;
    
    @Column(length = 50)
    @Email(message = "Введите корректный email!")  
    private String email;
    
    @Column(length = 64)
    private String password;
      
    @Column(length = 50)
    private String whoCreated;
    
    @Column
    private boolean rememberCheck;
    
    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = {
        @JoinColumn(name = "User_userid")}, inverseJoinColumns = {
        @JoinColumn(name = "Role_roleid")})
    private List<Role> roles;

    @OneToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER, mappedBy = "user")
    Set<Test> tests;
    
    @OneToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER, mappedBy = "user")
    Set<Result> results;
      
    public User() {
        roles = new ArrayList<Role>();
    }

    public User(User user) {
    	this.username = user.username;
    	this.firstname = user.firstname;
    	this.lastname = user.lastname;
    	this.email = user.email;
    	this.password = user.password;
    	this.studyPlace = user.studyPlace;
    	this.whoCreated = user.whoCreated;
    	//this.results = user.getResults();
    }
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

	public String getWhoCreated() {
		return whoCreated;
	}

	public void setWhoCreated(String whoCreated) {
		this.whoCreated = whoCreated;
	}

	public StudyPlace getStudyPlace() {
		return studyPlace;
	}

	public void setStudyPlace(StudyPlace studyPlace) {
		this.studyPlace = studyPlace;
	}

	public boolean isRememberCheck() {
		return rememberCheck;
	}

	public void setRememberCheck(boolean rememberCheck) {
		this.rememberCheck = rememberCheck;
	}

}