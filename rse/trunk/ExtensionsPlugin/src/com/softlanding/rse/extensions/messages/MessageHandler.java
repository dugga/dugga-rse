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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.ibm.as400.access.QueuedMessage;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class MessageHandler implements IMessageHandler {
	private QueuedMessageSubSystem queuedMessageSubSystem;
	private QueuedMessage msg;
	private boolean remove;

	public MessageHandler(QueuedMessageSubSystem queuedMessageSubSystem, boolean remove) {
		super();
		this.queuedMessageSubSystem = queuedMessageSubSystem;
		this.remove = remove;
	}

	public void handleMessage(
		QueuedMessage message,
		MonitoredMessageQueue messageQueue) {
			String monString = queuedMessageSubSystem.getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.MONITOR);
			if ((monString == null) || (monString.equals("false"))) return; //$NON-NLS-1$
			msg = message;
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					String handling;
					if (msg.getType() == QueuedMessage.INQUIRY) {
						handling = queuedMessageSubSystem.getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.INQUIRY_NOTIFICATION);
						if (handling == null) handling = "Dialog"; //$NON-NLS-1$
					} else {
						handling = queuedMessageSubSystem.getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.INFORMATIONAL_NOTIFICATION);
						if (handling == null) handling = "eMail"; //$NON-NLS-1$
					}
					if (handling.equals("Beep")) Display.getDefault().beep(); //$NON-NLS-1$
					if (handling.equals("eMail")) { //$NON-NLS-1$
						String eMail = queuedMessageSubSystem.getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.EMAIL_ADDRESS);
						String from = queuedMessageSubSystem.getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.EMAIL_FROM);
						String port = queuedMessageSubSystem.getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.EMAIL_PORT);
						String host = queuedMessageSubSystem.getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.EMAIL_HOST);
						if (from == null) from = ExtensionsPlugin.getResourceString("MessageHandler.5") + queuedMessageSubSystem.getHost().getHostName(); //$NON-NLS-1$
						if (port == null) port = "25"; //$NON-NLS-1$
						if ((eMail == null) || (eMail.trim().length() == 0) || (host == null) || (host.trim().length() == 0)) {
							if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), ExtensionsPlugin.getResourceString("MessageHandler.7"), ExtensionsPlugin.getResourceString("MessageHandler.8"))) { //$NON-NLS-1$ //$NON-NLS-2$
								QueuedMessageDialog dialog = new QueuedMessageDialog(Display.getDefault().getActiveShell(), msg);
								Display.getDefault().beep();
								dialog.open();
								return;
							} 
						}
						MessageQueueMailMessenger messenger = new MessageQueueMailMessenger();
						String[] recipients = {eMail};
						messenger.setRecipients(recipients);
						messenger.setMailFrom(from);
						messenger.setPort(port);
						messenger.setHost(host);
						try {
							messenger.sendMail(msg);
						} catch (Exception e) {
							String errorMessage = e.getMessage();
							if (errorMessage == null) errorMessage = e.toString();
							errorMessage = errorMessage + ExtensionsPlugin.getResourceString("MessageHandler.9"); //$NON-NLS-1$
							Display.getDefault().beep();
							if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), ExtensionsPlugin.getResourceString("MessageHandler.10"), errorMessage)) handling = "Dialog"; //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
					if (handling.equals("Dialog")) { //$NON-NLS-1$
						Display.getDefault().beep();
						QueuedMessageDialog dialog = new QueuedMessageDialog(Display.getDefault().getActiveShell(), msg);
						dialog.open();
					}
					if (remove && (msg.getType() != QueuedMessage.INQUIRY)) {
						try {
							msg.getQueue().remove(msg.getKey());
						} catch (Exception e) {}
					}
				}
			});
	}

}
