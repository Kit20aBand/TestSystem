package com.nz.simplecrud.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

@Named
@SessionScoped
public class DashboardController implements Serializable{
	private static final long serialVersionUID = 1L;

	private DashboardModel model;
	
	public DashboardController() {
		model = new DefaultDashboardModel();
		DashboardColumn column1 = new DefaultDashboardColumn();  
        DashboardColumn column2 = new DefaultDashboardColumn();  
        DashboardColumn column3 = new DefaultDashboardColumn();
        
        column1.addWidget("Students");
        column1.addWidget("Results");
        column2.addWidget("Tests");
        column3.addWidget("Questions");
        
        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);
	}

	public DashboardModel getModel() {
		return model;
	}
	
	
}
