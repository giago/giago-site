package com.giago.www.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.giago.www.shared.ContactRequest;

public class ContactRequestReceiver extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ContactRequestReceiver.class.getName());
	
	private static final long serialVersionUID = 1L;
	
	private ContactRequestJdo jdo = new ContactRequestJdo();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		getAndPost(req, resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		getAndPost(req, resp);
	}

	private void getAndPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = null;
		String firstName = null;
		String lastName = null;
		String company = null;
		String phone = null;
		try {
			String title = (String)req.getParameter("title");
			req.removeAttribute("title");
			firstName = (String)req.getParameter("firstName");
			req.removeAttribute("firstName");
			lastName = (String)req.getParameter("lastName");
			req.removeAttribute("lastName");
			company = (String)req.getParameter("company");
			req.removeAttribute("company");
			email = (String)req.getParameter("email");
			req.removeAttribute("email");
			String reason = (String)req.getParameter("reason");
			req.removeAttribute("reason");
			phone = (String)req.getParameter("phone");
			req.removeAttribute("phone");
			
			ContactRequest r = new ContactRequest();
			r.setTitle(title);
			r.setEmail(email);
			r.setFirstName(firstName);
			r.setLastName(lastName);
			r.setCreated(System.currentTimeMillis());
			r.setModified(System.currentTimeMillis());
			r.setReason(reason);
			r.setCompany(company);
			r.setPhone(phone);
			
			jdo.save(r);
			
			sendEmail(r);
			
		} catch (Throwable t) {
			logger.severe("Error in the registration : " + email 
					+ "," + firstName + "," + lastName + "," 
					+ company + "," + phone);
		}
		resp.sendRedirect("/registration-end.jsp");
	}

	private void sendEmail(ContactRequest r) {		
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("luigi.agosti@gmail.com", "Contact request"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress("luigi.agosti@gmail.com", "Luigi"));
            String title = "New request came through to the Giago website\n";
            msg.setSubject(title);
            String info = "Title : " + r.getTitle() + "\n" +
            "FirstName : " + r.getFirstName() + "\n" +
            "LastName : " + r.getLastName() + "\n" +
            "Company : " + r.getCompany() + "\n" +
            "Email : " + r.getEmail() + "\n" +
            "Phone : " + r.getPhone() + "\n" +
            "Reason : " + r.getReason() + "\n" +
            "Created : " + r.getCreated() + "\n" +
            "Id : " + r.getId() + "\n";
            msg.setText(title + info);
            Transport.send(msg);
        } catch (AddressException e) {
            logger.log(Level.SEVERE, "AddressException", e);
            throw new RuntimeException(e.getMessage());
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "MessagingException", e);
            throw new RuntimeException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.SEVERE, "UnsupportedEncodingException", e);
            throw new RuntimeException(e.getMessage());
        }
	}

}
