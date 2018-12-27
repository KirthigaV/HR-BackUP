package com.homedepot.hr.hr.retailstaffing.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingRequisitionDAO;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingRequisitionManager;
import com.homedepot.ta.aa.util.TAAAResourceManager;

public class EmailSender
{

	private static final Logger logger = Logger.getLogger(RetailStaffingRequisitionManager.class);

	public static void sendEmail(String toAddress, String fromAddress, String subject, String textBody, String LCP)
	{
		try
		{
			//String mailhost = TAAAResourceManager.getProperty("mail.host");
			String mailhost = RetailStaffingRequisitionDAO.readHrOrgParmMailHost(LCP);
			Properties props = System.getProperties();

			if(mailhost != null)
			{
				logger.info("***Mail Host = " + mailhost);
				props.put("mail.host", mailhost);
			} else {
				throw new Exception("Mail Host Was Null....");
			}
			Session session = Session.getDefaultInstance(props, null);
			MimeMessage msg = new MimeMessage(session);
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
			msg.setFrom(new InternetAddress(fromAddress));
			msg.setSubject(subject);
			msg.setText(textBody);
			Transport.send(msg);
			logger.info("***Email Sent to Queweb....");
		}
		catch (MessagingException me)
		{
			// log this exception and handle it correctly depending on the
			// application code
			logger.info("*****************************");
			logger.info("Email Failed to SEND");
			logger.info("*****************************");
			me.printStackTrace();
		} catch (Exception e) {
			logger.info("*****************************");
			logger.info("Issue with sendMail");
			logger.info("*****************************");
			e.printStackTrace();
		}
	}
}
