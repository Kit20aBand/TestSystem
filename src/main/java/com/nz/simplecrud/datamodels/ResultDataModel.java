package com.nz.simplecrud.datamodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.nz.simplecrud.entity.Result;

public class ResultDataModel extends ListDataModel<Result> implements SelectableDataModel<Result>, Serializable{
	private static final long serialVersionUID = 1L;

	private List<Result> data = new ArrayList<Result>();
	
	public ResultDataModel() {
		
	}
	
	public List<Result> getData() {
		return data;
	}



	public void setData(List<Result> data) {
		this.data = data;
	}



	public ResultDataModel(List<Result> data) {
		super(data);
	}

	@Override
	public Result getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<Result> results =  (List<Result>) getWrappedData();  
        
        for (Result result : results) {  
            if(result.getUser().getFirstname().equals(rowKey))  
                return result;  
        }  
          
        return null; 
	}

	@Override
	public Object getRowKey(Result result) {
		return result.getUser().getFirstname();
	}

}
