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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.as400.access.MessageQueue;
import com.ibm.as400.access.QueuedMessage;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class QueuedMessageDialog extends Dialog {
	private QueuedMessage queuedMessage;
	private Text responseText;

	public QueuedMessageDialog(Shell shell, QueuedMessage queuedMessage) {
		super(shell);
		this.queuedMessage = queuedMessage;
//		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	public Control createDialogArea(Composite parent) {
		Composite promptGroup = (Composite)super.createDialogArea(parent);
		parent.getShell().setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.0")); //$NON-NLS-1$
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		promptGroup.setLayout(layout);
		promptGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		Composite headerGroup = new Composite(promptGroup, SWT.NONE);
		GridLayout headerLayout = new GridLayout();
		headerLayout.numColumns = 4;
		headerGroup.setLayout(headerLayout);
		headerGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		Label idLabel = new Label(headerGroup, SWT.NONE);
		idLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.1")); //$NON-NLS-1$
		Text idText = new Text(headerGroup, SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = 75;
		idText.setLayoutData(gd);
		idText.setEnabled(false);
		if (queuedMessage.getID() != null) idText.setText(queuedMessage.getID());
		Label sevLabel = new Label(headerGroup, SWT.NONE);
		sevLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.2")); //$NON-NLS-1$
		Text sevText = new Text(headerGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 50;
		sevText.setLayoutData(gd);
		sevText.setEnabled(false);
		sevText.setText(new Integer(queuedMessage.getSeverity()).toString());		
		Label typeLabel = new Label(headerGroup, SWT.NONE);
		typeLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.3")); //$NON-NLS-1$
		Text typeText = new Text(headerGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 200;
		gd.horizontalSpan = 3;
		typeText.setLayoutData(gd);
		typeText.setEnabled(false);
		switch (queuedMessage.getType()) {
			case QueuedMessage.COMPLETION :
				typeText.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.4")); //$NON-NLS-1$
				break;
			case QueuedMessage.DIAGNOSTIC :
				typeText.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.5")); //$NON-NLS-1$
				break;
			case QueuedMessage.INFORMATIONAL :
				typeText.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.6")); //$NON-NLS-1$
				break;
			case QueuedMessage.INQUIRY :
				typeText.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.7")); //$NON-NLS-1$
				break;
			case QueuedMessage.SENDERS_COPY :
				typeText.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.8")); //$NON-NLS-1$
				break;
			case QueuedMessage.REQUEST :
				typeText.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.9")); //$NON-NLS-1$
				break;
			case QueuedMessage.REQUEST_WITH_PROMPTING :
				typeText.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.10")); //$NON-NLS-1$
				break;
			case QueuedMessage.NOTIFY :
				typeText.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.11")); //$NON-NLS-1$
				break;
			default :
		}
		Label dateLabel = new Label(headerGroup, SWT.NONE);
		dateLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.12")); //$NON-NLS-1$
		Text dateText = new Text(headerGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 200;
		gd.horizontalSpan = 3;
		dateText.setLayoutData(gd);
		dateText.setEnabled(false);
		dateText.setText(queuedMessage.getDate().getTime().toString());
		Label userLabel = new Label(headerGroup, SWT.NONE);
		userLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.13")); //$NON-NLS-1$
		Text userText = new Text(headerGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 200;
		gd.horizontalSpan = 3;
		userText.setLayoutData(gd);
		userText.setEnabled(false);
		if (queuedMessage.getUser() != null) userText.setText(queuedMessage.getUser());
		Label msgLabel = new Label(promptGroup, SWT.NONE); 
		msgLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.14")); //$NON-NLS-1$
		Text msgText = new Text(promptGroup, SWT.WRAP | SWT.V_SCROLL);
		if ((queuedMessage.getHelp() == null) || (queuedMessage.getHelp().equals(queuedMessage.getText())))
			msgText.setText(queuedMessage.getText());
		else
		msgText.setText(queuedMessage.getText() + "\n" + queuedMessage.getHelp()); //$NON-NLS-1$
		gd = new GridData();
		gd.widthHint = 300;
		gd.heightHint = 150;
		msgText.setLayoutData(gd);
		msgText.setEditable(false);
		msgText.setRedraw(true);
		if (queuedMessage.getType() == QueuedMessage.INQUIRY) {
			Composite responseGroup = new Composite(promptGroup, SWT.NONE);
			GridLayout responseLayout = new GridLayout();
			responseLayout.numColumns = 2;
			responseGroup.setLayout(responseLayout);
			responseGroup.setLayoutData(
			new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
			Label replyLabel = new Label(responseGroup, SWT.NONE);
			replyLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageDialog.16")); //$NON-NLS-1$
			responseText = new Text(responseGroup, SWT.BORDER);
			gd = new GridData();
			gd.widthHint = 200;
			responseText.setTextLimit(132);
			responseText.setLayoutData(gd);
			if (queuedMessage.getDefaultReply() != null)
				responseText.setText(queuedMessage.getDefaultReply());
			responseText.setFocus();
		}
		return promptGroup;
	}
	
	public int open() {

		if (getShell() == null) {
			// create the window
			create();
		}
		
		getShell().forceActive();

		return super.open();
	}
	
	public void okPressed() {
		if (queuedMessage.getType() == QueuedMessage.INQUIRY) {
			if ((responseText.getText() != null) && (responseText.getText().trim().length() > 0)) {
				MessageQueue messageQueue = queuedMessage.getQueue();
				try {
					messageQueue.reply(queuedMessage.getKey(), responseText.getText());
				} catch (Exception e) {
					String errorMessage = e.getMessage();
					if (errorMessage == null) errorMessage = e.toString();
					MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("QueuedMessageDialog.17"), errorMessage); //$NON-NLS-1$
					return;
				}
			}	
		}
		super.okPressed();
	}

}
