package com.nz.simplecrud.validators;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

import com.nz.simplecrud.annotations.Test;

@Named
@Test
public class TestValidator extends Validator implements Serializable{
	private static final long serialVersionUID = 1L;

	@Override
	public void validate(ComponentSystemEvent event) {
	
	}

	private void addMessage(String mainInfo, String supportInfo) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				mainInfo, supportInfo);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
