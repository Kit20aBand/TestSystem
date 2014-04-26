package com.nz.simplecrud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import com.nz.simplecrud.datamodels.UserDataModel;
import com.nz.simplecrud.entity.Role;
import com.nz.simplecrud.entity.StudyPlace;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.UserService;
import com.nz.simplecrud.util.tests.TestUtil;
 
@Named
@SessionScoped
public class AdminController implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject
	UserService userDao;
	
	@Inject
	UserService roleDao;
	
	@Inject
	TestUtil testUtil;
	
	private UserDataModel userDataModel;
	
	private User newUser = new User();
	
	private StudyPlace studyPlace = new StudyPlace();
	
	private List<User> users;
	
	private List<User> filteredUsers;

	private List<User> selectedUsers;
	
	@PostConstruct
	private void init() {
		users = userDao.findWithNamedQuery(User.FIND_TEACHERS);
		userDataModel = new UserDataModel(users);
		newUser.setStudyPlace(studyPlace);
	}
	
	public void doCreateUser() {
		newUser.setWhoCreated(testUtil.findUser().getUsername());
		User userToCreate = new User(newUser);
		setRole(userToCreate);
		userDao.create(userToCreate);
		users.add(userToCreate);
		userDataModel.setData(users);
	}
	
	private void setRole(User userToCreate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("roledesc", "Administrator");
		Role role = (Role) roleDao.getOneResult(Role.SELECT_BY_ROLEDESC, parameters);
		
		List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		userToCreate.setRoles(roles);
	}
	
	@SuppressWarnings("unchecked")
	public void doDeleteUsers() {
		if (selectedUsers.size() == 0) {
			FacesMessage msg = new FacesMessage("Выберите хотя бы одного преподавателя", "Вы выбрали " + selectedUsers.size() + " преподавателя");    
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        return;
		}
		List<User> relatedUsers = new ArrayList<User>();
		Map<String, Object> userParam = new HashMap<String, Object>();

		for (int i = 0; i < selectedUsers.size(); i++) {
			userParam.put("whoCreated", selectedUsers.get(i).getUsername());
			relatedUsers = userDao.findWithNamedQuery(User.FIND_BY_WHO_CREATED, userParam);
			userDao.deleteItems(relatedUsers);
			userParam.clear();
		}
		userDao.deleteItems(selectedUsers);
		users.removeAll(selectedUsers);
		userDataModel.setData(users);
	}

	public void onEdit(RowEditEvent event) {
		User updatedUser = (User) event.getObject();
		try {
			userDao.update(updatedUser);
		} catch (Exception e) {
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
	
	public UserDataModel getUserDataModel() {
		return userDataModel;
	}

	public void setUserDataModel(UserDataModel userDataModel) {
		this.userDataModel = userDataModel;
	}

	public List<User> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(List<User> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	public List<User> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

}
