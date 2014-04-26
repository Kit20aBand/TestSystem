package com.nz.simplecrud.service;

import javax.ejb.Stateless;

import com.nz.simplecrud.entity.Test;

@Stateless
public class TestService extends DataAccessService<Test>{

	public TestService() {
		super(Test.class);
	}
}
