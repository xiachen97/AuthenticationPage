package com.uniquedeveloper.registration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Retrieve the 'username' and 'password' parameters from the login.java
		String uemail=request.getParameter("username");
		String upwd=request.getParameter("password");
		// Get the current HTTP session, store user-specific data across multiple requests
		HttpSession session=request.getSession();
		// Initialize a RequestDispatcher,forward the request to JSP page.
		RequestDispatcher dispatcher=null;
		
		try {
			// Load the MySQL JDBC driver class
			Class.forName("com.mysql.jdbc.Driver");
			//connect to the MySQL database 'company' with the provided credentials (root and password).
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/company?useSSL=false&serverTimezone=UTC","root","xiachen123");
			//SQL query to select a user with the given uemail and password upwd
			PreparedStatement pst=con.prepareStatement("select * from users where uemail=? and upwd=?");
			//Set values for the query parameters using the input from the form.
			pst.setString(1, uemail);
			pst.setString(2, upwd);
			
			//Execute the query and store the result in a ResultSet.
			ResultSet rs=pst.executeQuery();
			//Check if a matching user exists in the ResultSet.
			if(rs.next()) {
				// If a user is found, set their name in the session to keep them logged in.
				session.setAttribute("name", rs.getString("uname"));
				dispatcher=request.getRequestDispatcher("index.jsp");
			}else {
				// If the login failed, set an attribute 'status' to 'failed'.
				request.setAttribute("status", "failed");
				dispatcher=request.getRequestDispatcher("login.jsp");
			}
			dispatcher.forward(request, response);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
