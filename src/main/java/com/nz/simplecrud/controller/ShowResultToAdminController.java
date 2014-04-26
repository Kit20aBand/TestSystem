package com.nz.simplecrud.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.nz.simplecrud.datamodels.ResultDataModel;
import com.nz.simplecrud.entity.Result;
import com.nz.simplecrud.entity.User;
import com.nz.simplecrud.service.ResultService;
import com.nz.simplecrud.util.tests.TestUtil;

@Named
@RequestScoped
public class ShowResultToAdminController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	ResultService resultDao;
	
	@Inject
	TestUtil testUtil;
	
	private ResultDataModel resultDataModel;
	
	private List<Result> results;
	
	private List<User> filteredResults;

	private List<User> selectedResults;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		User user = testUtil.findUser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("whoCreated", user.getUsername());
		results = resultDao.findWithNamedQuery(Result.FIND_BY_WHO_CREATED, params);
		resultDataModel = new ResultDataModel(results);
	}
	
	public void postProcessXLS(Object document) {  
	    HSSFWorkbook wb = (HSSFWorkbook) document;  
	    
	    HSSFSheet sheet = wb.getSheetAt(0);  
	    HSSFRow header = sheet.getRow(0);  
	      
	    HSSFCellStyle cellStyle = wb.createCellStyle();    
	    cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);  
	    cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	      
	    for(int i=0; i < header.getPhysicalNumberOfCells();i++) {  
	        HSSFCell cell = header.getCell(i);  
	        cell.setCellStyle(cellStyle);  
	    }  
	}  
	
	public String getActiveTestName() {
		return testUtil.findActiveTest(testUtil.findUser()).getTopic();
	}

	public ResultDataModel getResultDataModel() {
		return resultDataModel;
	}

	public void setResultDataModel(ResultDataModel resultDataModel) {
		this.resultDataModel = resultDataModel;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public List<User> getFilteredResults() {
		return filteredResults;
	}

	public void setFilteredResults(List<User> filteredResults) {
		this.filteredResults = filteredResults;
	}

	public List<User> getSelectedResults() {
		return selectedResults;
	}

	public void setSelectedResults(List<User> selectedResults) {
		this.selectedResults = selectedResults;
	}	
}
