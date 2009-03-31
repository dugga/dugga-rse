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
package com.softlanding.rse.extensions.spooledfiles;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFileMessageDialog extends Dialog {
	private SpooledFile spooledFile;
	private AS400Message message;
	private Text replyText;

	public SpooledFileMessageDialog(Shell shell, SpooledFile spooledFile) {
		super(shell);
		this.spooledFile = spooledFile;
	}
	
	public Control createDialogArea(Composite parent) {
		Composite rtnGroup = (Composite)super.createDialogArea(parent);
		parent.getShell().setText(ExtensionsPlugin.getResourceString("Spooled_File_Message_1")); //$NON-NLS-1$
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		rtnGroup.setLayout(layout);
		rtnGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		Group headerGroup = new Group(rtnGroup, SWT.NONE);
		GridLayout headerLayout = new GridLayout();
		headerLayout.numColumns = 2;
		headerGroup.setLayout(headerLayout);
		headerGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		try {	
			message = spooledFile.getMessage();
			Label msgidLabel = new Label(headerGroup, SWT.NONE);
			msgidLabel.setText(ExtensionsPlugin.getResourceString("Message_ID__2")); //$NON-NLS-1$
			Text msgidText = new Text(headerGroup, SWT.BORDER);
			msgidText.setEnabled(false);
			msgidText.setText(message.getID());
			Label sevLabel = new Label(headerGroup, SWT.NONE);
			sevLabel.setText(ExtensionsPlugin.getResourceString("Severity__3")); //$NON-NLS-1$
			Text sevText = new Text(headerGroup, SWT.BORDER);
			sevText.setEnabled(false);
			sevText.setText(new Integer(message.getSeverity()).toString());
			Label typeLabel = new Label(headerGroup, SWT.NONE);
			typeLabel.setText(ExtensionsPlugin.getResourceString("Message_type__4")); //$NON-NLS-1$
			Text typeText = new Text(headerGroup, SWT.BORDER);
			typeText.setEnabled(false);
			switch (message.getType()) {
				case AS400Message.COMPLETION: 		typeText.setText(ExtensionsPlugin.getResourceString("Completion_5")); break; //$NON-NLS-1$
				case AS400Message.DIAGNOSTIC:		typeText.setText(ExtensionsPlugin.getResourceString("Diagnostic_6")); break; //$NON-NLS-1$
				case AS400Message.ESCAPE:			typeText.setText(ExtensionsPlugin.getResourceString("Escape_7")); break; //$NON-NLS-1$
				case AS400Message.INFORMATIONAL:	typeText.setText(ExtensionsPlugin.getResourceString("Informational_8")); break; //$NON-NLS-1$
				case AS400Message.INQUIRY:			typeText.setText(ExtensionsPlugin.getResourceString("Inquiry_9")); break; //$NON-NLS-1$
				case AS400Message.NOTIFY:			typeText.setText(ExtensionsPlugin.getResourceString("Notify_10")); break; //$NON-NLS-1$
				case AS400Message.REQUEST:			typeText.setText(ExtensionsPlugin.getResourceString("Request_11")); break; //$NON-NLS-1$
				default:
			}
			Label sentLabel = new Label(headerGroup, SWT.NONE);
			sentLabel.setText(ExtensionsPlugin.getResourceString("Sent__12")); //$NON-NLS-1$
			Text sentText = new Text(headerGroup, SWT.BORDER);
			sentText.setEnabled(false);
			sentText.setText(message.getDate().getTime().toString());
			
			Text msgText = new Text(rtnGroup, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
			msgText.setEditable(false);
			GridData gd = new GridData();
			gd.widthHint = 400;
			gd.heightHint = 80;
			msgText.setLayoutData(gd);
			msgText.setText(message.getText());
			
			Text helpText = new Text(rtnGroup, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
			helpText.setEditable(false);
			gd = new GridData();
			gd.widthHint = 400;
			gd.heightHint = 160;
			helpText.setLayoutData(gd);
			if (message.getHelp() != null) helpText.setText(format(message.getHelp()));
			
			Composite replyGroup = new Composite(rtnGroup, SWT.NONE);
			GridLayout replyLayout = new GridLayout();
			replyLayout.numColumns = 2;
			replyGroup.setLayout(replyLayout);
			replyGroup.setLayoutData(
			new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
			
			Label replyLabel = new Label(replyGroup, SWT.NONE);
			replyLabel.setText(ExtensionsPlugin.getResourceString("Reply__13")); //$NON-NLS-1$
			replyText = new Text(replyGroup, SWT.BORDER);
			gd = new GridData();
			gd.widthHint = 300;
			replyText.setLayoutData(gd);
			if (message.getDefaultReply() != null) {
				replyText.setText(message.getDefaultReply());
				replyText.selectAll();
			}	
			replyText.setFocus();		
			
		} catch (Exception e) {handleError(e);}
		
		return rtnGroup;
	}
	
	public void okPressed() {
		try {
			spooledFile.answerMessage(replyText.getText());
		} catch (Exception e) {
			handleError(e);
			return;
		}
		super.okPressed();
	}
	
	private String format(String text) {
		for (int i = text.indexOf("&N"); i != -1;) { //$NON-NLS-1$
			text = text.substring(0, i) + System.getProperty("line.separator") + "  " + text.substring(i + 2); //$NON-NLS-1$ //$NON-NLS-2$
			i = text.indexOf("&N"); //$NON-NLS-1$
		}
		for (int i = text.indexOf("&B"); i != -1;) { //$NON-NLS-1$
			text = text.substring(0, i) + System.getProperty("line.separator") + "    " + text.substring(i + 2); //$NON-NLS-1$ //$NON-NLS-2$
			i = text.indexOf("&B"); //$NON-NLS-1$
		}
		for (int i = text.indexOf("&P"); i != -1;) { //$NON-NLS-1$
			text = text.substring(0, i) + System.getProperty("line.separator") + "      " + text.substring(i + 2); //$NON-NLS-1$ //$NON-NLS-2$
			i = text.indexOf("&P"); //$NON-NLS-1$
		}
		return text;
	}
	
	private void handleError(Exception e) {
		MessageDialog.openError(getShell(), ExtensionsPlugin.getResourceString("Spooled_File_Message_Error_26"), e.getMessage()); //$NON-NLS-1$
	}

}