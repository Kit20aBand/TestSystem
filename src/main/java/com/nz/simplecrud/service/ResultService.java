package com.nz.simplecrud.service;

import javax.ejb.Stateless;

import com.nz.simplecrud.entity.Question;
import com.nz.simplecrud.entity.Result;
import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.Variant;

@Stateless
public class ResultService extends DataAccessService<Result>{

	public ResultService() {
		super(Result.class);
	}
}
 