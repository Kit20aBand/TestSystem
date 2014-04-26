package com.nz.simplecrud.controller;

import com.nz.simplecrud.datamodels.UserDataModel;
import com.nz.simplecrud.entity.Result;
import com.nz.simplecrud.entity.Role;
import com.nz.simplecrud.entity.StudyPlace;
import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.RoleService;
import com.nz.simplecrud.service.UserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UpdateModelException;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;

import org.primefaces.event.RowEditEvent;

/**
 * User Controller class allows users to do CRUD operations
 */

@Named
@SessionScoped
public class UserController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private transient Logger logger;

	@Inject
	private UserService userDao;

	@Inject
	private RoleService roleDao;

	@Inject
	private LoginController loginController;

	@Inject
	private SendMailController sendMailController;

	private List<User> users;

	private List<User> filteredUsers;

	private User[] selectedUsers;

	private UserDataModel userDataModel;

	private User newUser = new User();

	private StudyPlace studyPlace = new StudyPlace();

	private Result result = new Result();

	private FacesContext facesContext;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		logger.log(Level.INFO, "UserController running");
		Map<String, Object> userParam = new HashMap<String, Object>();
		userParam.put("whoCreated", loginController.getUsername());
		users = userDao.findWithNamedQuery(User.FIND_BY_WHO_CREATED, userParam);

		userDataModel = new UserDataModel(users);
		initUser();
		facesContext = FacesContext.getCurrentInstance();
	}

	public void initUser() {
		newUser.setStudyPlace(studyPlace);
		
		/*
		 * Role role = new Role(); role.setRoledesc("User");
		 * role.setRolename("Users"); roles.add(role); newUser.setRoles(roles);
		 */

	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User[] getSelectedUsers() {
		System.out.println(selectedUsers);
		return selectedUsers;
	}

	public void setSelectedUsers(User[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public List<User> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(List<User> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	public UserDataModel getUserDataModel() {
		return userDataModel;
	}

	public void setUserDataModel(UserDataModel userDataModel) {
		this.userDataModel = userDataModel;
	}

	/**
	 * Create User
	 */
	public void doCreateUser() {
		newUser.setWhoCreated(loginController.getUsername());
		User userToCreate = new User(newUser);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("roledesc", "user");
		Role role = (Role) roleDao.getOneResult(Role.SELECT_BY_ROLEDESC,
				parameters);

		List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		userToCreate.setRoles(roles);

		try {
			userDao.create(userToCreate);
			users.add(userToCreate);
			userDataModel.setData(users);
		} catch (EJBException e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Такой ник уже существует",
					"Введите другой ник");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	public StudyPlace getStudyPlace() {
		return studyPlace;
	}

	public void setStudyPlace(StudyPlace studyPlace) {
		this.studyPlace = studyPlace;
	}

	/**
	 * Delete User
	 */
	public void doDeleteUsers(ActionEvent actionEvent) {	
		if (selectedUsers.length == 0) {
			FacesMessage msg = new FacesMessage("Выберите хотя бы одного студента!", "Вы выбрали " + selectedUsers.length + " студента");    
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        return;
		}
		userDao.deleteItems(selectedUsers);
		users.removeAll(new ArrayList<User>(Arrays.asList(selectedUsers)));
		userDataModel.setData(users);
	}

	public void sendEmail() {
		sendMailController.send();
	}

	public void onEdit(RowEditEvent event) {
		User updatedUser = (User) event.getObject();

		try {
			userDao.update(updatedUser);
		} catch (Exception e) {
			// User oldUser = (User) event.
			updatedUser.setUsername(userDao.find(updatedUser.getId())
					.getUsername());

			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Такой ник уже существует",
					"Введите другой ник");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void onCancel() {

	}

	public SendMailController getSendMailController() {
		return sendMailController;
	}

	public void setSendMailController(SendMailController sendMailController) {
		this.sendMailController = sendMailController;
	}

}
