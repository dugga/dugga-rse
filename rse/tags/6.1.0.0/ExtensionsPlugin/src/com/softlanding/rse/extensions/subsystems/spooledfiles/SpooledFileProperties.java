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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileModel;

public class SpooledFileProperties extends PropertyPage {

	public SpooledFileProperties() {
		super();
		noDefaultAndApplyButton();
	}

	protected Control createContents(Composite parent) {
		parent.setLayout(new GridLayout());
		parent.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		Composite propGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		propGroup.setLayout(layout);
		propGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		SpooledFileResource spooledFileResource = (SpooledFileResource)getElement();
		SpooledFile spooledFile = spooledFileResource.getSpooledFile();
		Label file = new Label(propGroup, SWT.NONE);
		file.setText(ExtensionsPlugin.getResourceString("File_10") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label fileValue = new Label(propGroup, SWT.NONE);
		fileValue.setText(spooledFile.getName());
		Label outq = new Label(propGroup, SWT.NONE);
		outq.setText(ExtensionsPlugin.getResourceString("Output_queue_13") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label outqValue = new Label(propGroup, SWT.NONE);
		SpooledFileModel model = new SpooledFileModel(spooledFile);
		outqValue.setText(model.getOutputQueue());
		Label dta = new Label(propGroup, SWT.NONE);
		dta.setText(ExtensionsPlugin.getResourceString("User_data_16") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label dtaValue = new Label(propGroup, SWT.NONE);
		try {
			dtaValue.setText(spooledFile.getStringAttribute(PrintObject.ATTR_USERDATA));
		} catch (Exception e) {}
		Label sts = new Label(propGroup, SWT.NONE);
		sts.setText(ExtensionsPlugin.getResourceString("Status_19") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label stsValue = new Label(propGroup, SWT.NONE);
		try {
			stsValue.setText(spooledFile.getStringAttribute(PrintObject.ATTR_SPLFSTATUS));
		} catch (Exception e) {}
		Label pages = new Label(propGroup, SWT.NONE);
		pages.setText(ExtensionsPlugin.getResourceString("Total_pages_22") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label pagesValue = new Label(propGroup, SWT.NONE);
		try {
			pagesValue.setText(spooledFile.getIntegerAttribute(PrintObject.ATTR_PAGES).toString());
		} catch (Exception e) {}
		Label page = new Label(propGroup, SWT.NONE);
		page.setText(ExtensionsPlugin.getResourceString("Current_page_25") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label pageValue = new Label(propGroup, SWT.NONE);
		try {
			pageValue.setText(spooledFile.getIntegerAttribute(PrintObject.ATTR_CURPAGE).toString());
		} catch (Exception e) {}
		Label user = new Label(propGroup, SWT.NONE);
		user.setText(ExtensionsPlugin.getResourceString("Job_user_28") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label userValue = new Label(propGroup, SWT.NONE);
		userValue.setText(spooledFile.getJobUser());
		Label job = new Label(propGroup, SWT.NONE);
		job.setText(ExtensionsPlugin.getResourceString("Job_name_31") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label jobValue = new Label(propGroup, SWT.NONE);
		jobValue.setText(spooledFile.getJobName());
		Label jobnbr = new Label(propGroup, SWT.NONE);
		jobnbr.setText(ExtensionsPlugin.getResourceString("Job_number_34") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label jobnbrValue = new Label(propGroup, SWT.NONE);
		jobnbrValue.setText(spooledFile.getJobNumber());
		Label nbr = new Label(propGroup, SWT.NONE);
		nbr.setText(ExtensionsPlugin.getResourceString("File_number_37") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label nbrValue = new Label(propGroup, SWT.NONE);
		nbrValue.setText(new Integer(spooledFile.getNumber()).toString());
		Label pty = new Label(propGroup, SWT.NONE);
		pty.setText(ExtensionsPlugin.getResourceString("Priority_40") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label ptyValue = new Label(propGroup, SWT.NONE);
		try {
			ptyValue.setText(spooledFile.getStringAttribute(PrintObject.ATTR_OUTPTY));
		} catch (Exception e) {}
		Label cpy = new Label(propGroup, SWT.NONE);
		cpy.setText(ExtensionsPlugin.getResourceString("Copies_43") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label cpyValue = new Label(propGroup, SWT.NONE);
		try {
			cpyValue.setText(spooledFile.getIntegerAttribute(PrintObject.ATTR_COPIES).toString());
		} catch (Exception e) {}
		Label type = new Label(propGroup, SWT.NONE);
		type.setText(ExtensionsPlugin.getResourceString("Form_type_46") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label typeValue = new Label(propGroup, SWT.NONE);
		try {
			typeValue.setText(spooledFile.getStringAttribute(PrintObject.ATTR_FORMTYPE));
		} catch (Exception e) {}
		Label date = new Label(propGroup, SWT.NONE);
		date.setText(ExtensionsPlugin.getResourceString("Creation_date_49") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label dateValue = new Label(propGroup, SWT.NONE);
		dateValue.setText(model.getCreateDate());
		Label time = new Label(propGroup, SWT.NONE);
		time.setText(ExtensionsPlugin.getResourceString("Creation_time_52") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		Label timeValue = new Label(propGroup, SWT.NONE);
		timeValue.setText(model.getCreateTime());
		return propGroup;
	}

}
