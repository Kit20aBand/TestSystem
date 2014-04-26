package com.nz.simplecrud.controller;

import java.io.Serializable;
import java.util.Formatter;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.util.tests.TestUtil;

@Named
@RequestScoped
public class SendMailController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private UserController userController;

	@Inject
	private TestUtil testUtil;

	@Resource(mappedName = "java:/mail/Gmail")
	private Session mailSession;

	private String username;

	private String password;

	private String emailAddress;

	private Test test;

	private User teacher;

	public void send() {
		try {
			teacher = testUtil.findUser();

			if (!findActiveTest()) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Активный тест не выбран",
						"Пожалуйста, выберите активный тест в разделе 'Тесты'");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}

			User[] selectedUsers = userController.getSelectedUsers();
			if (selectedUsers.length == 0) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Вы не выбрали ни одного студента",
						"Пожалуйста, выберите студентов, которым следует отправить информацию");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}
			for (int i = 0; i < selectedUsers.length; i++) {
				Message message = new MimeMessage(mailSession);
				message.setFrom(new InternetAddress(
						"voltage.test.system@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(selectedUsers[i].getEmail()));
				message.setSubject("Forgotten data to enter Test System");

				Formatter formattedInfo = new Formatter();
				formattedInfo
						.format(" Приветствуем Вас, %s %s. \n "
								+ " \n Ваши данные для входа в систему: \n Ник: %s \n Пароль: %s. \n"
								+ " Дисциплина: %s. \n Тема: %s. \n Преподаватель: %s %s. \n Удачи Вам!",
								selectedUsers[i].getFirstname(),
								selectedUsers[i].getLastname(),
								selectedUsers[i].getUsername(),
								selectedUsers[i].getPassword(),
								test.getSubject(), test.getTopic(),
								teacher.getFirstname(), teacher.getUsername());
				message.setText(formattedInfo.toString());

				Transport.send(message);
			}
			FacesMessage goodMessage = new FacesMessage(
					"Отправка почты завершилась успешно",
					"Теперь студенты будут знать о предстоящем тесте");
			FacesContext.getCurrentInstance().addMessage(null, goodMessage);
		} catch (MessagingException e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Ошибка отправки почты",
					"Попробуйте повторить отправку или отправить письмо самостоятельно");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}

	}

	public void sendForgottenData(User user) {
		try {
		Message message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress("voltage.test.system@gmail.com"));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(user.getEmail()));
		message.setSubject("Testing Subject");

		Formatter formattedInfo = new Formatter();
		formattedInfo
				.format(" Приветствуем Вас, %s %s. \n "
						+ " \n Ваши данные для входа в систему: \n Ник: %s \n Пароль: %s. \n",

				user.getFirstname(), user.getLastname(), user.getUsername(),
						user.getPassword());
		message.setText(formattedInfo.toString());

		Transport.send(message);
		FacesMessage goodMessage = new FacesMessage(
				"Отправка почты завершилась успешно!",
				"Проверьте свою почту!");
		FacesContext.getCurrentInstance().addMessage(null, goodMessage);
		} catch (MessagingException e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Ошибка отправки почты",
					"Попробуйте повторить отправку или отправить письмо самостоятельно");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
	}

	private boolean findActiveTest() {
		try {
			test = testUtil.findActiveTest(teacher);
			return true;
		} catch (EJBException e) {

		}
		return false;
	}
}
