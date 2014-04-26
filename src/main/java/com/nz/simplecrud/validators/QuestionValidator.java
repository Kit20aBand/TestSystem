package com.nz.simplecrud.validators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

import com.nz.simplecrud.annotations.Question;

@Named
@Question
public class QuestionValidator extends Validator implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int MAX_COUNT_OF_TEXTAREAS = 6;

	public void validate(ComponentSystemEvent event) {
		validateQuestionText(event);
		List<String> variants = validateVariants(event);
		validateEqualsVariants(event, variants);
		validateAnswers(event, variants);
	}

	private void validateQuestionText(ComponentSystemEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();
		UIComponent components = event.getComponent();
		UIInput uiInputQuestionText = (UIInput) components
				.findComponent("editor");
		if (uiInputQuestionText.equals("")) {
			addMessage("Вы не ввели вопрос!", "Пожалуйста, введите вопрос!");
			fc.renderResponse();
		}
	}

	private List<String> validateVariants(ComponentSystemEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();
		UIComponent components = event.getComponent();
		List<String> variants = new ArrayList<String>();
		for (int i = 0; i < MAX_COUNT_OF_TEXTAREAS; i++) {
			UIInput uiInputVariant = (UIInput) components
					.findComponent("variant" + i);
			String var = uiInputVariant.getLocalValue() == null ? ""
					: uiInputVariant.getLocalValue().toString();
			variants.add(var);
		}
		if (variants.get(0).equals("")) {
			addMessage("Вы не ввели первый вариант!",
					"Пожалуйста, введите первый вариант! Вариантов должно быть минимум 2!");
			fc.renderResponse();
		}
		if (variants.get(1).equals("")) {
			addMessage("Вы не ввели второй вариант!",
					"Пожалуйста, введите второй вариант! Вариантов должно быть минимум 2!");
			fc.renderResponse();
		}
		return variants;
	}

	private void validateEqualsVariants(ComponentSystemEvent event,
			List<String> variants) {
		FacesContext fc = FacesContext.getCurrentInstance();
		int countOfNonEmptyVariants = 0;
		for (String variant : variants) {
			if (!variant.equals("")) {
				countOfNonEmptyVariants++;
			}
		}
		int countOfEqualsVariants = 0;
		for (int i = 0; i < variants.size(); i++) {
			for (String variant : variants) {
				if (variants.get(i).equals(variant) && (!variants.get(i).equals(""))) {
					countOfEqualsVariants++;
				}
			}
		}
		if (countOfEqualsVariants > countOfNonEmptyVariants) {
			addMessage("Ошибка ввода!",
					"Вы ввели несколько одинаковых вариантов!");
			fc.renderResponse();
		}
	}

	private void validateAnswers(ComponentSystemEvent event,
			List<String> variants) {
		FacesContext fc = FacesContext.getCurrentInstance();
		UIComponent components = event.getComponent();
		List<Boolean> answers = new ArrayList<Boolean>();
		for (int i = 0; i < MAX_COUNT_OF_TEXTAREAS; i++) {
			UIInput uiInputАnswer = (UIInput) components.findComponent("answer"
					+ i);
			answers.add((Boolean.parseBoolean(uiInputАnswer.getLocalValue()
					.toString())));
			System.out.println(Boolean.parseBoolean(uiInputАnswer
					.getLocalValue().toString()));
		}
		for (int i = 0; i < MAX_COUNT_OF_TEXTAREAS; i++) {
			if ((answers.get(i) == true) && variants.get(i) == "") {
				addMessage("Ошибка ввода!",
						"Вы поставили правильный ответ на несуществующий(пустой) вариант!");
				fc.renderResponse();
			}
		}
		int countOfCorrect = 0;
		for (int i = 0; i < answers.size(); i++) {
			if (answers.get(i)) {
				countOfCorrect++;
			}
		}
		if (countOfCorrect == 0) {
			addMessage("Вы не ввели ни одного правильного ответа!",
					"Пожалуйста, введите хотя бы один правильный ответ!");
			fc.renderResponse();
		}
	}

	private void addMessage(String mainInfo, String supportInfo) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				mainInfo, supportInfo);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
