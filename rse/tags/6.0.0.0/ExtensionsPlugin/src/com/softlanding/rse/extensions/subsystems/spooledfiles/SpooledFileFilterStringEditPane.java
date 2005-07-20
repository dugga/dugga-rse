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
package com.softlanding.rse.extensions.subsystems.spooledfiles;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.etools.systems.core.messages.SystemMessage;
import com.ibm.etools.systems.core.ui.SystemWidgetHelpers;
import com.ibm.etools.systems.filters.ui.SystemFilterStringEditPane;
import com.softlanding.rse.extensions.ExtensionsPlugin;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileFilter;

public class SpooledFileFilterStringEditPane
	extends SystemFilterStringEditPane {
		
	private Text userText;
	private Text outqText;
	private Text outqLibText;
	private Text userDataText;
	private Text formTypeText;
	private Text startDateText;
	private Text startTimeText;
	private Text endDateText;
	private Text endTimeText;

	public SpooledFileFilterStringEditPane(Shell shell) {
		super(shell);
	}
	
	public Control createContents(Composite parent) {
		int nbrColumns = 2;
		Composite composite_prompts = SystemWidgetHelpers.createComposite(parent, nbrColumns);	
		((GridLayout)composite_prompts.getLayout()).marginWidth = 0;
		
		Label userLabel = new Label(composite_prompts, SWT.NONE);
		userLabel.setText(ExtensionsPlugin.getResourceString("User__1")); //$NON-NLS-1$
		userText = new Text(composite_prompts, SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = 75;
		userText.setLayoutData(gd);
		userText.setTextLimit(10);
		
		Label outqLabel = new Label(composite_prompts, SWT.NONE);
		outqLabel.setText(ExtensionsPlugin.getResourceString("Output_queue__2")); //$NON-NLS-1$
		outqText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		outqText.setLayoutData(gd);
		outqText.setTextLimit(10);
		
		Label outqLibLabel = new Label(composite_prompts, SWT.NONE);
		outqLibLabel.setText(ExtensionsPlugin.getResourceString("___Library__3")); //$NON-NLS-1$
		outqLibText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		outqLibText.setLayoutData(gd);
		outqLibText.setTextLimit(10);
		
		Label dtaLabel = new Label(composite_prompts, SWT.NONE);
		dtaLabel.setText(ExtensionsPlugin.getResourceString("User_data__4")); //$NON-NLS-1$
		userDataText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		userDataText.setLayoutData(gd);
		userDataText.setTextLimit(10);
		
		Label typeLabel = new Label(composite_prompts, SWT.NONE);
		typeLabel.setText(ExtensionsPlugin.getResourceString("Form_type__5")); //$NON-NLS-1$
		formTypeText = new Text(composite_prompts, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		formTypeText.setLayoutData(gd);
		formTypeText.setTextLimit(10);
				
		resetFields();
		doInitializeFields();
		
		ModifyListener keyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
					validateStringInput();
			}
		};
		
		userText.addModifyListener(keyListener);
		outqText.addModifyListener(keyListener);
		outqLibText.addModifyListener(keyListener);
		userDataText.addModifyListener(keyListener);
		formTypeText.addModifyListener(keyListener);
		
		return composite_prompts;
	}
	
	public Control getInitialFocusControl() {
		return userText;
	}	
	
	protected void doInitializeFields() {
		if (userText == null)
		  return;
		 if (inputFilterString != null) {
			SpooledFileFilter filter = new SpooledFileFilter(inputFilterString);
			if (filter.getUserFilter() != null) userText.setText(filter.getUserFilter());
			else userText.setText("*"); //$NON-NLS-1$
			if (filter.getQueueFilter() != null) outqText.setText(filter.getQueueFilter());
			else outqText.setText("*"); //$NON-NLS-1$
			if (filter.getQueueLibraryFilter() != null) outqLibText.setText(filter.getQueueLibraryFilter());
			else outqLibText.setText("*"); //$NON-NLS-1$
			if (filter.getUserDataFilter() != null) userDataText.setText(filter.getUserDataFilter());
			else userDataText.setText("*"); //$NON-NLS-1$
			if (filter.getFormTypeFilter() != null) formTypeText.setText(filter.getFormTypeFilter());
			else formTypeText.setText("*"); //$NON-NLS-1$
		 }
	}
	
	protected void resetFields() {
	    userText.setText("*");	 //$NON-NLS-1$
	    outqText.setText("*");	 //$NON-NLS-1$
	    outqLibText.setText("*"); //$NON-NLS-1$
	    userDataText.setText("*"); //$NON-NLS-1$
	    formTypeText.setText("*"); //$NON-NLS-1$
	}
	
	protected boolean areFieldsComplete() {
		return true;
	}
	
	public String getFilterString() {
		SpooledFileFilter filter = new SpooledFileFilter();
		if ((userText.getText() != null) && (userText.getText().length() > 0) && (!userText.getText().equals("*"))) //$NON-NLS-1$
			filter.setUserFilter(userText.getText().toUpperCase());
		if ((outqText.getText() != null) && (outqText.getText().length() > 0) && (!outqText.getText().equals("*"))) //$NON-NLS-1$
			filter.setQueueFilter(outqText.getText().toUpperCase());
		if ((outqLibText.getText() != null) && (outqLibText.getText().length() > 0) && (!outqLibText.getText().equals("*"))) //$NON-NLS-1$
			filter.setQueueLibraryFilter(outqLibText.getText().toUpperCase());
		if ((userDataText.getText() != null) && (userDataText.getText().length() > 0) && (!userDataText.getText().equals("*"))) //$NON-NLS-1$
			filter.setUserDataFilter(userDataText.getText().toUpperCase());
		if ((formTypeText.getText() != null) && (formTypeText.getText().length() > 0) && (!formTypeText.getText().equals("*"))) //$NON-NLS-1$
			filter.setFormTypeFilter(formTypeText.getText().toUpperCase());
		return filter.getFilterString();
	}	
	
	public SystemMessage verify() {
		return null;
	}

}
