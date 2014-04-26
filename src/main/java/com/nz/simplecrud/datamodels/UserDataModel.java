package com.nz.simplecrud.datamodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.nz.simplecrud.entity.User;

public class UserDataModel extends ListDataModel<User> implements SelectableDataModel<User>, Serializable{
	private static final long serialVersionUID = 1L;

	private List<User> data = new ArrayList<User>();
	
	public UserDataModel() {
		
	}
	
	public List<User> getData() {
		return data;
	}



	public void setData(List<User> data) {
		this.data = data;
	}



	public UserDataModel(List<User> data) {
		super(data);
	}

	@SuppressWarnings("unchecked")
	@Override
	public User getRowData(String rowKey) {
		
		List<User> users =  (List<User>) getWrappedData();  
        
        for(User user : users) {  
            if(user.getUsername().equals(rowKey))  
                return user;  
        }  
          
        return null; 
	}

	@Override
	public Object getRowKey(User user) {
		return user.getUsername();
	}

}
