package com.nz.simplecrud.service;

import javax.ejb.Stateless;

import com.nz.simplecrud.entity.Role;
import com.nz.simplecrud.entity.Test;

@Stateless
public class RoleService extends DataAccessService<Role>{

	public RoleService() {
		super(Role.class);
	}
}
