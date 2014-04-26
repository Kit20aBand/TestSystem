package com.nz.simplecrud.filter;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *  To prevent user from going back to Login page if the user already logged in
 * @author Emre Simtay <emre@simtay.com>
 */
public class LoginPageFilter implements Filter{
   @Override
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,   FilterChain filterChain) throws IOException, ServletException{
       HttpServletRequest request = (HttpServletRequest) servletRequest;
       HttpServletResponse response = (HttpServletResponse) servletResponse;
       System.out.println(request.getUserPrincipal());
       
       if(request.getUserPrincipal() != null){ //If user is already authenticated   
                String navigateString = "";
                if(request.isUserInRole("Administrator")){
                        navigateString = "/admin/Admin_home.xhtml";
                }else if(request.isUserInRole("Manager")){
                        navigateString = "/manager/Manager_home.xhtml";
                }else if(request.isUserInRole("User")){
                        navigateString = "/user/User_home.xhtml";
                }
                response.setContentType("text/xml");
                response.getWriter()
                    .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                    .printf("<partial-response><redirect url=\"%s\"></redirect></partial-response>", request.getContextPath()+navigateString);
       } else{
           filterChain.doFilter(servletRequest, servletResponse);
       }
   }

   @Override
   public void destroy(){
   }
   
   @Override
   public void init(FilterConfig filterConfig) throws ServletException {
   }
}