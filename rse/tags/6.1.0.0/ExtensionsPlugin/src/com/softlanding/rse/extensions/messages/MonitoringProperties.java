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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import com.softlanding.rse.extensions.ExtensionsPlugin;

public class MonitoringProperties extends PropertyPage {
	private QueuedMessageSubSystem queuedMessageSubSystem;
	private Button monitorButton;
	private Button removeButton;
	private Label removeLabel;
	private Composite prefGroup;
	private Combo inqCombo;
	private Combo infCombo;
	private Group eMailGroup;
	private Text eMailText;
	private Text fromText;
	private Text portText;
	private Text hostText;
	private Button testButton;
	public final static String VENDOR_ID = "com.softlanding"; //$NON-NLS-1$
	public final static String MONITOR = "com.softlanding.rse.extensions.messages.monitor"; //$NON-NLS-1$
	public final static String REMOVE = "com.softlanding.rse.extensions.messages.remove"; //$NON-NLS-1$
	public final static String INQUIRY_NOTIFICATION = "com.softlanding.rse.extensions.messages.inquiry"; //$NON-NLS-1$
	public final static String INFORMATIONAL_NOTIFICATION = "com.softlanding.rse.extensions.messages.informational"; //$NON-NLS-1$
	public final static String EMAIL_ADDRESS = "com.softlanding.rse.extensions.messages.email"; //$NON-NLS-1$
	public final static String EMAIL_FROM = "com.softlanding.rse.extensions.messages.from"; //$NON-NLS-1$
	public final static String EMAIL_PORT = "com.softlanding.rse.extensions.messages.port"; //$NON-NLS-1$
	public final static String EMAIL_HOST = "com.softlanding.rse.extensions.messages.host"; //$NON-NLS-1$

	public MonitoringProperties() {
		super();
	}

	protected Control createContents(Composite parent) {
		queuedMessageSubSystem = (QueuedMessageSubSystem)getElement();
		Composite propsGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		propsGroup.setLayout(layout);
		propsGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		Composite monGroup = new Composite(propsGroup, SWT.NONE);
		GridLayout monLayout = new GridLayout();
		monLayout.numColumns = 2;
		monGroup.setLayout(monLayout);
		monGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		monitorButton = new Button(monGroup, SWT.CHECK);
		Label monLabel = new Label(monGroup, SWT.NONE);
		monLabel.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.9")); //$NON-NLS-1$
		removeButton = new Button(monGroup, SWT.CHECK);
		removeLabel = new Label(monGroup, SWT.NONE);
		removeLabel.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.10")); //$NON-NLS-1$
		prefGroup = new Composite(propsGroup, SWT.NONE);
		GridLayout prefLayout = new GridLayout();
		prefLayout.numColumns = 2;
		prefGroup.setLayout(prefLayout);
		prefGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		Label inqLabel = new Label(prefGroup, SWT.NONE);
		inqLabel.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.11")); //$NON-NLS-1$
		inqCombo = new Combo(prefGroup, SWT.READ_ONLY);
		inqCombo.add(ExtensionsPlugin.getResourceString("MonitoringProperties.12")); //$NON-NLS-1$
		inqCombo.add(ExtensionsPlugin.getResourceString("MonitoringProperties.13")); //$NON-NLS-1$
		inqCombo.add(ExtensionsPlugin.getResourceString("MonitoringProperties.14")); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.widthHint = 100;
		inqCombo.setLayoutData(gd);
		Label infLabel = new Label(prefGroup, SWT.NONE);
		infLabel.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.15")); //$NON-NLS-1$
		infCombo = new Combo(prefGroup, SWT.READ_ONLY);
		infCombo.add("Dialog"); //$NON-NLS-1$
		infCombo.add("eMail"); //$NON-NLS-1$
		infCombo.add("Beep"); //$NON-NLS-1$
		gd = new GridData();
		gd.widthHint = 100;
		infCombo.setLayoutData(gd);
		eMailGroup = new Group(propsGroup, SWT.NONE);
		GridLayout eMailLayout = new GridLayout();
		eMailLayout.numColumns = 2;
		eMailGroup.setLayout(eMailLayout);
		eMailGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		Label eMailLabel = new Label(eMailGroup, SWT.NONE);
		eMailLabel.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.19")); //$NON-NLS-1$
		eMailText = new Text(eMailGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 250;
		eMailText.setLayoutData(gd);
		Label fromLabel = new Label(eMailGroup, SWT.NONE);
		fromLabel.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.20")); //$NON-NLS-1$
		fromText = new Text(eMailGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 250;
		fromText.setLayoutData(gd);
		Label hostLabel = new Label(eMailGroup, SWT.NONE);
		hostLabel.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.21")); //$NON-NLS-1$
		hostText = new Text(eMailGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 250;
		hostText.setLayoutData(gd);
		Label portLabel = new Label(eMailGroup, SWT.NONE);
		portLabel.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.22")); //$NON-NLS-1$
		portText = new Text(eMailGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 50;
		portText.setLayoutData(gd);
		portText.setTextLimit(4);
		String monitorString = queuedMessageSubSystem.getVendorAttribute(VENDOR_ID, MONITOR);
		String removeString = queuedMessageSubSystem.getVendorAttribute(VENDOR_ID, REMOVE);
		String eMailString = queuedMessageSubSystem.getVendorAttribute(VENDOR_ID, EMAIL_ADDRESS);
		String inquiryString = queuedMessageSubSystem.getVendorAttribute(VENDOR_ID, INQUIRY_NOTIFICATION);
		String infoString = queuedMessageSubSystem.getVendorAttribute(VENDOR_ID, INFORMATIONAL_NOTIFICATION);
		String fromString = queuedMessageSubSystem.getVendorAttribute(VENDOR_ID, EMAIL_FROM);
		String portString = queuedMessageSubSystem.getVendorAttribute(VENDOR_ID, EMAIL_PORT);
		String hostString = queuedMessageSubSystem.getVendorAttribute(VENDOR_ID, EMAIL_HOST);
		if (monitorString == null) monitorButton.setSelection(false);
		else monitorButton.setSelection(monitorString.equals("true")); //$NON-NLS-1$
		if (removeString == null) removeButton.setSelection(false);
		else removeButton.setSelection(removeString.equals("true")); //$NON-NLS-1$
		if (inquiryString == null) inqCombo.select(inqCombo.indexOf("Dialog")); //$NON-NLS-1$
		else inqCombo.select(inqCombo.indexOf(inquiryString));
		if (infoString == null) infCombo.select(infCombo.indexOf("eMail")); //$NON-NLS-1$
		else infCombo.select(infCombo.indexOf(infoString));
		if (eMailString != null) eMailText.setText(eMailString);
		else eMailText.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.27")); //$NON-NLS-1$
		if (fromString != null) fromText.setText(fromString);
		else fromText.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.28")); //$NON-NLS-1$
		if (hostString != null) hostText.setText(hostString);
		else hostText.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.29")); //$NON-NLS-1$
		if (portString != null) portText.setText(portString);
		else portText.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.30")); //$NON-NLS-1$
		testButton = new Button(eMailGroup, SWT.PUSH);
		testButton.setText(ExtensionsPlugin.getResourceString("MonitoringProperties.31")); //$NON-NLS-1$
		testButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {
				testEmail();
			}
		});
		if ((eMailText.getText() == null) || (eMailText.getText().trim().length() == 0) || (fromText.getText() == null) || (fromText.getText().trim().length() == 0) || (portText.getText() == null) || (portText.getText().trim().length() == 0) || (hostText.getText() == null) || (hostText.getText().trim().length() == 0))
			testButton.setEnabled(false);
		SelectionListener listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {
				setVisible();
			}
		};
		ModifyListener modListener = new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if ((eMailText.getText() == null) || (eMailText.getText().trim().length() == 0) || (fromText.getText() == null) || (fromText.getText().trim().length() == 0) || (portText.getText() == null) || (portText.getText().trim().length() == 0) || (hostText.getText() == null) || (hostText.getText().trim().length() == 0))
					testButton.setEnabled(false);
				else
					testButton.setEnabled(true);
			}
		};
		eMailText.addModifyListener(modListener);
		fromText.addModifyListener(modListener);
		hostText.addModifyListener(modListener);
		portText.addModifyListener(modListener);
		if (!monitorButton.getSelection()) {
			removeButton.setVisible(false);
			removeLabel.setVisible(false);
			prefGroup.setVisible(false);
			eMailGroup.setVisible(false);
		} else {
			if (infCombo.getSelectionIndex() == infCombo.indexOf("Beep")) //$NON-NLS-1$
				removeButton.setEnabled(false);
			if ((inqCombo.getSelectionIndex() != inqCombo.indexOf("eMail")) && (infCombo.getSelectionIndex() != infCombo.indexOf("eMail"))) //$NON-NLS-1$ //$NON-NLS-2$
				eMailGroup.setVisible(false);
		}
		monitorButton.addSelectionListener(listener);
		inqCombo.addSelectionListener(listener);
		infCombo.addSelectionListener(listener);
		
		return propsGroup;
	}
	
	public boolean performOk() {
		if (monitorButton.getSelection())
			queuedMessageSubSystem.setVendorAttribute(VENDOR_ID, MONITOR, "true"); //$NON-NLS-1$
		else
			queuedMessageSubSystem.setVendorAttribute(VENDOR_ID, MONITOR, "false"); //$NON-NLS-1$
		if (removeButton.getSelection())
			queuedMessageSubSystem.setVendorAttribute(VENDOR_ID, REMOVE, "true"); //$NON-NLS-1$
		else
			queuedMessageSubSystem.setVendorAttribute(VENDOR_ID, REMOVE, "false");			 //$NON-NLS-1$
		queuedMessageSubSystem.setVendorAttribute(VENDOR_ID, INQUIRY_NOTIFICATION, inqCombo.getText());
		queuedMessageSubSystem.setVendorAttribute(VENDOR_ID, INFORMATIONAL_NOTIFICATION, infCombo.getText());
		queuedMessageSubSystem.setVendorAttribute(VENDOR_ID, EMAIL_ADDRESS, eMailText.getText());
		queuedMessageSubSystem.setVendorAttribute(VENDOR_ID, EMAIL_FROM, fromText.getText());
		queuedMessageSubSystem.setVendorAttribute(VENDOR_ID, EMAIL_PORT, portText.getText());
		queuedMessageSubSystem.setVendorAttribute(VENDOR_ID, EMAIL_HOST, hostText.getText());
		return super.performOk();
	}
	
	private void setVisible() {
		if (monitorButton.getSelection()) {
			removeButton.setVisible(true);
			removeLabel.setVisible(true);
			if (infCombo.getSelectionIndex() == infCombo.indexOf("Beep")) { //$NON-NLS-1$
				removeButton.setSelection(false);
				removeButton.setEnabled(false);
			} else
				removeButton.setEnabled(true);
			prefGroup.setVisible(true);
			if ((inqCombo.getSelectionIndex() == inqCombo.indexOf("eMail")) || (infCombo.getSelectionIndex() == infCombo.indexOf("eMail"))) { //$NON-NLS-1$ //$NON-NLS-2$
				eMailGroup.setVisible(true);
			} else
				eMailGroup.setVisible(false);
		} else {
			removeButton.setVisible(false);
			removeLabel.setVisible(false);
			prefGroup.setVisible(false);
			eMailGroup.setVisible(false);
		}
	}
	
	private void testEmail() {
		MessageQueueMailMessenger messenger = new MessageQueueMailMessenger();
		String[] recipients = {eMailText.getText()};
		messenger.setRecipients(recipients);
		messenger.setMailFrom(fromText.getText());
		messenger.setPort(portText.getText());
		messenger.setHost(hostText.getText());
		try {
			messenger.sendMail(ExtensionsPlugin.getResourceString("MonitoringProperties.42"), ExtensionsPlugin.getResourceString("MonitoringProperties.43")); //$NON-NLS-1$ //$NON-NLS-2$
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("MonitoringProperties.44"), ExtensionsPlugin.getResourceString("MonitoringProperties.45") + eMailText.getText()); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			if (errorMessage == null) errorMessage = e.toString();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("MonitoringProperties.46"), errorMessage); //$NON-NLS-1$
		}
	}

}
