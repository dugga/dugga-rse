/*******************************************************************************
 * Copyright (c) 2005-2009 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *******************************************************************************/
package com.softlanding.rse.extensions.messages;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ibm.as400.access.QueuedMessage;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class MessageQueueMailMessenger {
	private String[] recipients;
	private boolean debug = false;
	private Properties properties = new Properties();
	private String mailFrom;

	public MessageQueueMailMessenger() {
		super();
	}
	
	public void sendMail(String subject, String message) throws Exception {
		if ((recipients == null) || (recipients.length == 0)) return;
//		Session session = Session.getDefaultInstance(properties, null);
		Session session = Session.getInstance(properties);
		session.setDebug(debug);
		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(recipients[0], mailFrom);
		msg.setFrom(addressFrom);
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++)
			addressTo[i] = new InternetAddress(recipients[i]);
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		msg.setSubject(subject);
		msg.setContent(message, "text/plain"); //$NON-NLS-1$
		Transport.send(msg);
	}
	
	public void sendMail(QueuedMessage message) throws Exception {
		if ((recipients == null) || (recipients.length == 0)) return;
		//Session session = Session.getDefaultInstance(properties, null);
		Session session = Session.getInstance(properties);
		session.setDebug(debug);
		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(recipients[0], mailFrom);
		msg.setFrom(addressFrom);
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++)
			addressTo[i] = new InternetAddress(recipients[i]);
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		msg.setSubject(message.getText());
		msg.setContent(getMessageBody(message), "text/plain"); //$NON-NLS-1$
		Transport.send(msg);
	}
	
	private String getMessageBody(QueuedMessage message) {
		StringBuffer body = new StringBuffer();
		if (message.getUser() != null) body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.2") + message.getUser() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		  if (message.getID() != null) body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.4") + message.getID() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		                               body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.6") + message.getSeverity() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
	               	                   body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.8")); //$NON-NLS-1$
		switch (message.getType()) {
			case QueuedMessage.COMPLETION :
				body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.9")); //$NON-NLS-1$
				break;
			case QueuedMessage.DIAGNOSTIC :
				body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.10")); //$NON-NLS-1$
				break;
			case QueuedMessage.INFORMATIONAL :
				body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.11")); //$NON-NLS-1$
				break;
			case QueuedMessage.INQUIRY :
				body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.12")); //$NON-NLS-1$
				break;
			case QueuedMessage.SENDERS_COPY :
				body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.13")); //$NON-NLS-1$
				break;
			case QueuedMessage.REQUEST :
				body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.14")); //$NON-NLS-1$
				break;
			case QueuedMessage.REQUEST_WITH_PROMPTING :
				body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.15")); //$NON-NLS-1$
				break;
			case QueuedMessage.NOTIFY :
				body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.16")); //$NON-NLS-1$
				break;
			default :
				break;
		}
		body.append("\n"); //$NON-NLS-1$
		if (message.getDate() != null) body.append("\n" + ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.19") + message.getDate().getTime() + "\n");			                                     //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (message.getFromJobName() != null)   body.append("\n" + ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.22") + message.getFromJobName() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (message.getFromJobNumber() != null) body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.24") + message.getFromJobNumber() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		if (message.getFromProgram() != null)   body.append(ExtensionsPlugin.getResourceString("MessageQueueMailMessenger.26") + message.getFromProgram() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		body.append("\n"); //$NON-NLS-1$
		body.append(message.getText());
		return body.toString();
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
		if (debug)
			properties.setProperty("mail.debug", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		else properties.setProperty("mail.debug", "false"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void setHost(String host) {
		properties.setProperty("mail.smtp.host", host); //$NON-NLS-1$
	}

	public void setPort(String port) {
		properties.setProperty("mail.smtp.port", port); //$NON-NLS-1$
	}

	public void setRecipients(String[] strings) {
		recipients = strings;
	}

	public void setSendPartial(boolean sendPartial) {
		if (sendPartial)
			properties.setProperty("mail.smtp.sendpartial", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		else properties.setProperty("mail.smtp.sendpartial", "false"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void setMailFrom(String string) {
		mailFrom = string;
	}

}
