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

import org.eclipse.rse.services.clientserver.messages.SystemMessage;
import org.eclipse.rse.ui.SystemWidgetHelpers;
import org.eclipse.rse.ui.filters.*;
import org.eclipse.rse.ui.messages.SystemMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.as400.access.QueuedMessage;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class QueuedMessageFilterStringEditPane
	extends SystemFilterStringEditPane {
		
	private Text messageQueueText;
	private Text libraryText;
	private Text userText;
	private Text idText;
	private Text severityText;
	private Text fromJobText;
	private Text fromJobNumberText;
	private Text fromProgramText;
	private Text textText;
	private Combo messageTypeCombo;

	public QueuedMessageFilterStringEditPane(Shell shell) {
		super(shell);
	}
	
	public Control createContents(Composite parent) {
		int nbrColumns = 2;
		Composite composite_prompts = SystemWidgetHelpers.createComposite(parent, nbrColumns);	
		((GridLayout)composite_prompts.getLayout()).marginWidth = 0;
		
		Label messageQueueLabel = new Label(composite_prompts, SWT.NONE);
		messageQueueLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.0")); //$NON-NLS-1$
		messageQueueText = new Text(composite_prompts, SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = 75;
		messageQueueText.setLayoutData(gd);
		messageQueueText.setTextLimit(10);
		
		Label libraryLabel = new Label(composite_prompts, SWT.NONE);
		libraryLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.1")); //$NON-NLS-1$
		libraryText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		libraryText.setLayoutData(gd);
		libraryText.setTextLimit(10);
		
		Label fromLabel = new Label(composite_prompts, SWT.NONE);
		fromLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.2")); //$NON-NLS-1$
		userText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		userText.setLayoutData(gd);
		userText.setTextLimit(10);
		
		Label idLabel = new Label(composite_prompts, SWT.NONE);
		idLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.3")); //$NON-NLS-1$
		idText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		idText.setLayoutData(gd);
		idText.setTextLimit(7);
		
		Label severityLabel = new Label(composite_prompts, SWT.NONE);
		severityLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.4")); //$NON-NLS-1$
		severityText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 50;
		severityText.setLayoutData(gd);
		severityText.setTextLimit(2);
		
		Label fromJobLabel = new Label(composite_prompts, SWT.NONE);
		fromJobLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.5")); //$NON-NLS-1$
		fromJobText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		fromJobText.setLayoutData(gd);
		fromJobText.setTextLimit(10);
		
		Label fromJobNumberLabel = new Label(composite_prompts, SWT.NONE);
		fromJobNumberLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.6")); //$NON-NLS-1$
		fromJobNumberText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		fromJobNumberText.setLayoutData(gd);
		fromJobNumberText.setTextLimit(6);
		
		Label fromProgramLabel = new Label(composite_prompts, SWT.NONE);
		fromProgramLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.7")); //$NON-NLS-1$
		fromProgramText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		fromProgramText.setLayoutData(gd);
		fromProgramText.setTextLimit(10);
		
		Label textLabel = new Label(composite_prompts, SWT.NONE);
		textLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.8")); //$NON-NLS-1$
		textText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 150;
		textText.setLayoutData(gd);
		textText.setTextLimit(255);
		
		Label typeLabel = new Label(composite_prompts, SWT.NONE);
		typeLabel.setText(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.9")); //$NON-NLS-1$
		messageTypeCombo = new Combo(composite_prompts, SWT.BORDER);
		messageTypeCombo.add("Any"); //$NON-NLS-1$
		messageTypeCombo.add("Completion"); //$NON-NLS-1$
		messageTypeCombo.add("Diagnostic"); //$NON-NLS-1$
		messageTypeCombo.add("Informational"); //$NON-NLS-1$
		messageTypeCombo.add("Inquiry"); //$NON-NLS-1$
		messageTypeCombo.add("Senders copy"); //$NON-NLS-1$
		messageTypeCombo.add("Request"); //$NON-NLS-1$
		messageTypeCombo.add("Request with prompting"); //$NON-NLS-1$
		messageTypeCombo.add("Notify"); //$NON-NLS-1$
		
		resetFields();
		doInitializeFields();
		
		ModifyListener keyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
					validateStringInput();
			}
		};
		
		messageQueueText.addModifyListener(keyListener);
		libraryText.addModifyListener(keyListener);
		userText.addModifyListener(keyListener);
		idText.addModifyListener(keyListener);
		severityText.addModifyListener(keyListener);
		fromJobText.addModifyListener(keyListener);
		fromJobNumberText.addModifyListener(keyListener);
		fromProgramText.addModifyListener(keyListener);
		textText.addModifyListener(keyListener);
		messageTypeCombo.addModifyListener(keyListener);

		return composite_prompts;
	}
	
	public Control getInitialFocusControl() {
		return messageQueueText;
	}	
	
	protected void doInitializeFields() {
		if (messageQueueText == null)
		  return;
		 if (inputFilterString != null) {
			QueuedMessageFilter filter = new QueuedMessageFilter(inputFilterString);
			if (filter.getMessageQueue() != null) messageQueueText.setText(filter.getMessageQueue());
			if (filter.getLibrary() != null) libraryText.setText(filter.getLibrary());
			else libraryText.setText("QUSRSYS"); //$NON-NLS-1$
			if (filter.getUser() != null) userText.setText(filter.getUser());
			else userText.setText("*"); //$NON-NLS-1$
			if (filter.getId() != null) idText.setText(filter.getId());
			else idText.setText("*"); //$NON-NLS-1$
			if (filter.getSeverity() == -1) severityText.setText("*"); //$NON-NLS-1$
			else severityText.setText(new Integer(filter.getSeverity()).toString());
			if (filter.getFromJobName() != null) fromJobText.setText(filter.getFromJobName());
			else fromJobText.setText("*"); //$NON-NLS-1$
			if (filter.getFromJobNumber() != null) fromJobNumberText.setText(filter.getFromJobNumber());
			else fromJobNumberText.setText("*"); //$NON-NLS-1$
			if (filter.getFromProgram() != null) fromProgramText.setText(filter.getFromProgram());
			else fromProgramText.setText("*"); //$NON-NLS-1$
			if (filter.getText() != null) textText.setText(filter.getText());
			else textText.setText("*"); //$NON-NLS-1$
			if (filter.getMessageType() == -1) messageTypeCombo.select(messageTypeCombo.indexOf("Any"));  //$NON-NLS-1$
			else {
				switch (filter.getMessageType()) {
					case QueuedMessage.COMPLETION :
						messageTypeCombo.select(messageTypeCombo.indexOf("Completion")); //$NON-NLS-1$
						break;
					case QueuedMessage.DIAGNOSTIC :
						messageTypeCombo.select(messageTypeCombo.indexOf("Diagnostic")); //$NON-NLS-1$
						break;
					case QueuedMessage.INFORMATIONAL :
						messageTypeCombo.select(messageTypeCombo.indexOf("Informational")); //$NON-NLS-1$
						break;
					case QueuedMessage.INQUIRY :
						messageTypeCombo.select(messageTypeCombo.indexOf("Inquiry")); //$NON-NLS-1$
						break;
					case QueuedMessage.SENDERS_COPY :
						messageTypeCombo.select(messageTypeCombo.indexOf("Senders copy")); //$NON-NLS-1$
						break;
					case QueuedMessage.REQUEST :
						messageTypeCombo.select(messageTypeCombo.indexOf("Request")); //$NON-NLS-1$
						break;
					case QueuedMessage.REQUEST_WITH_PROMPTING :
						messageTypeCombo.select(messageTypeCombo.indexOf("Request with prompting")); //$NON-NLS-1$
						break;
					case QueuedMessage.NOTIFY :
						messageTypeCombo.select(messageTypeCombo.indexOf("Notify")); //$NON-NLS-1$
						break;
					default :
						messageTypeCombo.select(messageTypeCombo.indexOf("Any")); //$NON-NLS-1$
				}
			}
		 }
	}
	
	protected void resetFields() {
		messageQueueText.setText(""); //$NON-NLS-1$
		libraryText.setText("QUSRSYS"); //$NON-NLS-1$
		userText.setText("*"); //$NON-NLS-1$
		idText.setText("*"); //$NON-NLS-1$
		severityText.setText("*"); //$NON-NLS-1$
		fromJobText.setText("*"); //$NON-NLS-1$
		fromJobNumberText.setText("*"); //$NON-NLS-1$
		fromProgramText.setText("*"); //$NON-NLS-1$
		textText.setText("*"); //$NON-NLS-1$
		messageTypeCombo.select(messageTypeCombo.indexOf("Any")); //$NON-NLS-1$
	}
	
	protected boolean areFieldsComplete() {
		return ((messageQueueText.getText() != null) && (messageQueueText.getText().trim().length() > 0) && (libraryText.getText() != null) && (libraryText.getText().trim().length() > 0) && (!messageQueueText.getText().equals("*")) && (!libraryText.getText().equals("*"))); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public String getFilterString() {
		QueuedMessageFilter filter = new QueuedMessageFilter();
		if ((messageQueueText.getText() != null) && (messageQueueText.getText().length() > 0) && (!messageQueueText.getText().equals("*"))) //$NON-NLS-1$
			filter.setMessageQueue(messageQueueText.getText().toUpperCase());
		if ((libraryText.getText() != null) && (libraryText.getText().length() > 0) && (!libraryText.getText().equals("*"))) //$NON-NLS-1$
			filter.setLibrary(libraryText.getText().toUpperCase());
		if ((userText.getText() != null) && (userText.getText().length() > 0) && (!userText.getText().equals("*"))) //$NON-NLS-1$
			filter.setUser(userText.getText().toUpperCase());
		if ((idText.getText() != null) && (idText.getText().length() > 0) && (!idText.getText().equals("*"))) //$NON-NLS-1$
			filter.setId(idText.getText().toUpperCase());
		if ((severityText.getText() != null) && (severityText.getText().length() > 0) && (!severityText.getText().equals("*"))) { //$NON-NLS-1$
			int severity = -1;
			try {
				severity = new Integer(severityText.getText()).intValue();
			} catch (Exception e) {
			}
			filter.setSeverity(severity);
		}
		if ((fromJobText.getText() != null) && (fromJobText.getText().length() > 0) && (!fromJobText.getText().equals("*"))) //$NON-NLS-1$
			filter.setFromJobName(fromJobText.getText().toUpperCase());
		if ((fromJobNumberText.getText() != null) && (fromJobNumberText.getText().length() > 0) && (!fromJobNumberText.getText().equals("*"))) //$NON-NLS-1$
			filter.setFromJobNumber(fromJobNumberText.getText());
		if ((fromProgramText.getText() != null) && (fromProgramText.getText().length() > 0) && (!fromProgramText.getText().equals("*"))) //$NON-NLS-1$
			filter.setFromProgram(fromProgramText.getText().toUpperCase());
		if ((textText.getText() != null) && (textText.getText().length() > 0) && (!textText.getText().equals("*"))) //$NON-NLS-1$
			filter.setText(textText.getText());
		int messageType = -1;
		if (messageTypeCombo.getText().equals("Completion")) messageType = QueuedMessage.COMPLETION; //$NON-NLS-1$
		else if (messageTypeCombo.getText().equals("Diagnostic")) messageType = QueuedMessage.DIAGNOSTIC; //$NON-NLS-1$
		else if (messageTypeCombo.getText().equals("Informational")) messageType = QueuedMessage.INFORMATIONAL; //$NON-NLS-1$
		else if (messageTypeCombo.getText().equals("Inquiry")) messageType = QueuedMessage.INQUIRY; //$NON-NLS-1$
		else if (messageTypeCombo.getText().equals("Senders copy")) messageType = QueuedMessage.SENDERS_COPY; //$NON-NLS-1$
		else if (messageTypeCombo.getText().equals("Request")) messageType = QueuedMessage.REQUEST; //$NON-NLS-1$
		else if (messageTypeCombo.getText().equals("Request with prompting")) messageType = QueuedMessage.REQUEST_WITH_PROMPTING; //$NON-NLS-1$
		else if (messageTypeCombo.getText().equals("Notify")) messageType = QueuedMessage.NOTIFY; //$NON-NLS-1$
		filter.setMessageType(messageType);
		return filter.getFilterString();
	}	
	
	public SystemMessage verify() {
		if (!areFieldsComplete()) return SystemMessageDialog.getExceptionMessage(Display.getCurrent().getActiveShell(), new Exception(ExtensionsPlugin.getResourceString("QueuedMessageFilterStringEditPane.66"))); //$NON-NLS-1$
		return null;
	}

}