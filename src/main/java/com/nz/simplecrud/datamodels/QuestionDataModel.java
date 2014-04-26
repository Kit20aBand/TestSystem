package com.nz.simplecrud.datamodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.nz.simplecrud.entity.Question;
import com.nz.simplecrud.entity.User;

public class QuestionDataModel extends ListDataModel<Question> implements SelectableDataModel<Question>, Serializable{
	private static final long serialVersionUID = 1L;

	private List<Question> data = new ArrayList<Question>();
	
	public QuestionDataModel() {
		
	}

	public List<Question> getData() {
		return data;
	}

	public void setData(List<Question> data) {
		this.data = data;
	}

	public QuestionDataModel(List<Question> data) {
		super(data);
	}

	@Override
	public Question getRowData(String rowKey) {
		
		@SuppressWarnings("unchecked")
		List<Question> questions =  (List<Question>) getWrappedData();  
        
        for(Question question : questions) {  
            if(question.getId().equals(rowKey))  
                return question;  
        }  
          
        return null; 
	}

	@Override
	public Object getRowKey(Question arg0) {
		return arg0.getId();
	}

}
