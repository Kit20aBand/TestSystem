package com.nz.simplecrud.datamodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.nz.simplecrud.entity.Test;
import com.nz.simplecrud.entity.User;

public class TestDataModel extends ListDataModel<Test> implements SelectableDataModel<Test>, Serializable{
	private static final long serialVersionUID = 1L;

	private List<Test> data = new ArrayList<Test>();
	
	public TestDataModel() {
		
	}
	
	public List<Test> getData() {
		return data;
	}

	public void setData(List<Test> data) {
		this.data = data;
	}



	public TestDataModel(List<Test> data) {
		super(data);
	}

	@Override
	public Test getRowData(String rowKey) {
		System.out.println("rowKey-------------------- " + rowKey);
		
		@SuppressWarnings("unchecked")
		List<Test> tests =  (List<Test>) getWrappedData();  
        
        for(Test test : tests) {  
            if(test.getSubject().equals(rowKey))  
                return test;  
        }  
          
        return null; 
	}

	@Override
	public Object getRowKey(Test test) {
		return test.getSubject();
	}

}
