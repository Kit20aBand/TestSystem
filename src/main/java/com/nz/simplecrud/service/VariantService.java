package com.nz.simplecrud.service;

import javax.ejb.Stateless;

import com.nz.simplecrud.entity.Question;
import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.Variant;

@Stateless
public class VariantService extends DataAccessService<Variant>{

	public VariantService() {
		super(Variant.class);
	}
}
 