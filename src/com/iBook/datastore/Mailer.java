package com.iBook.datastore;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.*;

public class Mailer
{
	// mailer code from: http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
	public Mailer(String toAddr, String newPwd)
	{
		// outgoing smtp admin user credentials
		final String adminUserName = "electraink.webmaster@gmail.com";
		final String adminPassword = "flap_d00dle";

		// SMTP properties: port 587 (TLS) on Google's mail server 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(adminUserName, adminPassword);
			}
		  });

		try 
		{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(adminUserName));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(toAddr));
			message.setSubject("ElectraInk Log-in");
			message.setText("As you requested, your iBook password at ElectraInk.com " +
					"has been reset. \n\nYour new password is: " + 
					newPwd + 
					"\n\n-- The ElectraInk webmaster at www.electraink.com");

			Transport.send(message);
		}
		catch (MessagingException e) 
		{
			throw new RuntimeException(e);
		}
	}	
}
