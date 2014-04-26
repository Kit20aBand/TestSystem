package com.nz.simplecrud.validators;

import javax.faces.event.ComponentSystemEvent;

public abstract class Validator {
	public abstract void validate(ComponentSystemEvent event);
}
