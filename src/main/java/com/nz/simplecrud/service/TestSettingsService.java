package com.nz.simplecrud.service;

import javax.ejb.Stateless;

import com.nz.simplecrud.entity.TestSettings;

@Stateless
public class TestSettingsService extends DataAccessService<TestSettings>{

	public TestSettingsService() {
		super(TestSettings.class);
	}
}
