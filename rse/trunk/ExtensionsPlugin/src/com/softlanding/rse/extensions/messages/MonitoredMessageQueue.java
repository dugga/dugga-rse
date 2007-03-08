/*******************************************************************************
 * Copyright (c) 2005 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *******************************************************************************/
package com.softlanding.rse.extensions.messages;

import java.util.ArrayList;
import java.util.Enumeration;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.MessageQueue;
import com.ibm.as400.access.QueuedMessage;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class MonitoredMessageQueue extends MessageQueue {
	private QueuedMessageFilter filter;
	private IMessageHandler messageHandler;
	private boolean monitoring;
	private String messageAction;
	private String messageType;
	private String errorMessage;
	private final static String END_MONITORING = "*END_MONITORING"; //$NON-NLS-1$

	public MonitoredMessageQueue(AS400 system, QueuedMessageFilter filter, IMessageHandler messageHandler) {
		super(system);
		this.filter = filter;
		this.messageHandler = messageHandler;
	}

	public MonitoredMessageQueue(AS400 system, String path, QueuedMessageFilter filter, IMessageHandler messageHandler) {
		super(system, path);
		this.filter = filter;
		this.messageHandler = messageHandler;
	}
	
	public QueuedMessage[] getFilteredMessages() throws Exception {
		Enumeration enumer = getMessages();
		ArrayList messages = new ArrayList();
		while (enumer.hasMoreElements()) {
			QueuedMessage message = (QueuedMessage)enumer.nextElement();
			if (includeMessage(message))
				messages.add(message);
		}
		QueuedMessage[] messageArray = new QueuedMessage[messages.size()];
		messages.toArray(messageArray);
		return messageArray;
	}
	
	public boolean startMonitoring(String action, String type) {
		messageAction = action;
		messageType = type;
		Thread monitoringThread = new Thread(new Runnable() {
			
			public void run() {
				try {
					while (monitoring) {
//						QueuedMessage message = receive(null, -1, messageAction, messageType);
						QueuedMessage message = receive(null, 30, messageAction, messageType);
						if (monitoring && (message != null)) {
							if (message.getText().equals(END_MONITORING)) {
								if (!messageAction.equals(REMOVE)) remove(message.getKey());
								monitoring = false; 
							} else {
								if (includeMessage(message))
									if (messageHandler != null) messageHandler.handleMessage(message, MonitoredMessageQueue.this);
							}
						} 
					}
				} catch (Exception e) {
					monitoring = false;
					if (e.getMessage() == null) errorMessage = e.toString();
					else errorMessage = e.getMessage();
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MessageDialog.openError(Display.getDefault().getActiveShell(), ExtensionsPlugin.getResourceString("MonitoredMessageQueue.1"), errorMessage); //$NON-NLS-1$
						}
					});
				}
			}			
		});
		monitoring = true;
		monitoringThread.setDaemon(true);
		monitoringThread.start();
		return monitoring;
	}
	
	public void stopMonitoring() {
		try {
//			sendInformational(END_MONITORING);
			monitoring = false;
		} catch (Exception e) {
			String errorMessage = null;
			if (e.getMessage() == null) errorMessage = e.toString();
			else errorMessage = e.getMessage();
			MessageDialog.openError(Display.getDefault().getActiveShell(), ExtensionsPlugin.getResourceString("MonitoredMessageQueue.1"), errorMessage); //$NON-NLS-1$
		}
	}

	public QueuedMessageFilter getFilter() {
		return filter;
	}

	public void setFilter(QueuedMessageFilter filter) {
		this.filter = filter;
	}

	public IMessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(IMessageHandler handler) {
		messageHandler = handler;
	}
	
	public boolean includeMessage(QueuedMessage message) {
		if (filter == null) return true;
		if (filter.getUser() != null) {
			if ((message.getUser() == null) || !message.getUser().equals(filter.getUser())) return false;
		}
		if (filter.getId() != null) {
			if ((message.getID() == null) || !message.getID().equals(filter.getId())) return false;
		}
		if (filter.getFromJobName() != null) {
			if ((message.getFromJobName() == null) || !message.getFromJobName().equals(filter.getFromJobName())) return false;
		}
		if (filter.getFromJobNumber() != null) {
			if ((message.getFromJobNumber() == null) || !message.getFromJobNumber().equals(filter.getFromJobNumber())) return false;
		}
		if (filter.getFromProgram() != null) {
			if ((message.getFromProgram() == null) || !message.getFromProgram().equals(filter.getFromProgram())) return false;
		}
		if (filter.getText() != null) {
			if ((message.getText() == null) || (message.getText().indexOf(filter.getText()) < 0)) return false;
		}
		if (filter.getSeverity() != -1) {
			if (message.getSeverity() < filter.getSeverity()) return false;
		}
		if (filter.getMessageType() != -1) {
			if (message.getType() != filter.getMessageType()) return false;
		}
		if (filter.getDate() != null) {
			if (filter.getDate().after(message.getDate().getTime())) return false;
		}
		return true;
	}

}
