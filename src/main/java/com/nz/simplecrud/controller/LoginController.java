package com.nz.simplecrud.controller;

import com.nz.simplecrud.datamodels.UserDataModel;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.UserService;
import com.nz.simplecrud.util.DateUtility;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.PrePassivate;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.print.attribute.standard.Severity;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Application;

import org.hibernate.validator.constraints.Email;

/**
 * Login Controller class allows only authenticated users to log in to the web
 * application.
 * 
 */
@Named
@SessionScoped
public class LoginController implements Serializable {

	@Inject
	private transient Logger logger;

	@Inject
	private UserService userDao;
	
	@Inject SendMailController mailController;

	private String username;

	private String password;
	
	@Email(message="Введите корректный email!")
	private String email;
	
	// Getters and Setters
	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Listen for button clicks on the #{loginController.login} action,
	 * validates the username and password entered by the user and navigates to
	 * the appropriate page.
	 * 
	 * @param actionEvent
	 */
	public void login(ActionEvent actionEvent) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		try {
			System.out.println("user name " + username);
			
			if (request.getUserPrincipal() != null) { // If user is already
				if (request.getUserPrincipal().getName().equals(username)) {
					context.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка входа!",
									"Пользователь с таким ником уже авторизирован."));
					return;
				}
			}
			
			String navigateString = "";
			// Checks if username and password are valid if not throws a
			// ServletException
			request.login(username, password);
			
			// gets the user principle and navigates to the appropriate page
			Principal principal = request.getUserPrincipal();


			if (request.isUserInRole("Administrator")) {
				navigateString = "/admin/Admin_home.xhtml";
			} else if (request.isUserInRole("Manager")) {
				navigateString = "/manager/Manager_home.xhtml";
			} else if (request.isUserInRole("User")) {
				navigateString = "/user/User_home.xhtml";
			}
			try {
				logger.log(
						Level.INFO,
						"User ({0}) loging in #"
								+ DateUtility.getCurrentDateTime(), request
								.getUserPrincipal().getName());
				context.getExternalContext().redirect(
						request.getContextPath() + navigateString);
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "IOException, Login Controller"
						+ "Username : " + principal.getName(), ex);
				context.addMessage(null, new FacesMessage("Error!",
						"Exception occured"));
			}

		} catch (ServletException e) {
			logger.log(Level.SEVERE, e.toString());
			context.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка входа!",
							"Проверьте правильность вода ника и пароля."));
		}
	}

	/**
	 * Listen for logout button clicks on the #{loginController.logout} action
	 * and navigates to login screen.
	 */
	public void logout() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		logger.log(Level.INFO,
				"User ({0}) loging out #" + DateUtility.getCurrentDateTime(),
				request.getUserPrincipal().getName());
		if (session != null) {
			session.invalidate();
		}
		FacesContext
				.getCurrentInstance()
				.getApplication()
				.getNavigationHandler()
				.handleNavigation(FacesContext.getCurrentInstance(), null,
						"/Login.xhtml?faces-redirect=true");
	}

	public void sendForgottenData() {
		mailController.sendForgottenData(userDao.findByEmail(email));
	}
	
	private HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}


}
