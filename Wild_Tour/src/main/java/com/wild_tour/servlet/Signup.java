package com.wild_tour.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.wild_tour.dao.UserDAO;
import com.wild_tour.dao.UserDAOImpl;
import com.wild_tour.dto.User;

@WebServlet("/signup")
public class Signup extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("signup.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User u = new User();
		UserDAO udao = new UserDAOImpl();
		
		u.setUser_name(req.getParameter("name"));
		u.setEmail(req.getParameter("email"));
		try {
			u.setPhone(Long.parseLong(req.getParameter("phone")));
		} catch (NumberFormatException e) {
			req.setAttribute("error", "Invalid phone number format");
			RequestDispatcher rd = req.getRequestDispatcher("signup.jsp");
			rd.forward(req, resp);
			return;
		}
		
		if(req.getParameter("pass").equals(req.getParameter("cpass"))) {
			u.setPassword(req.getParameter("pass"));
			u.setAddress(req.getParameter("address"));
			
			try {
				if(udao.insertUser(u)) {
					req.setAttribute("success", "Data saved successfully");
					RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
					rd.forward(req, resp);	
				} else {
					req.setAttribute("error", "Failed to save data. Database connection might be offline.");
					RequestDispatcher rd = req.getRequestDispatcher("signup.jsp");
					rd.forward(req, resp);
				}
			} catch (Exception e) {
				req.setAttribute("error", "An internal error occurred: " + e.getMessage());
				RequestDispatcher rd = req.getRequestDispatcher("signup.jsp");
				rd.forward(req, resp);
			}
		} else {
			req.setAttribute("error", "Passwords do not match");
			RequestDispatcher rd = req.getRequestDispatcher("signup.jsp");
			rd.forward(req, resp);
		}
	}
}
