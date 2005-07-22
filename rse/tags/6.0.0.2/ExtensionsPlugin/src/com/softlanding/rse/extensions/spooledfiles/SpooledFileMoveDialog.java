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
package com.softlanding.rse.extensions.spooledfiles;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFileMoveDialog extends Dialog {
	private SpooledFile spooledFile;
	private Combo outputQueueCombo;
	private Combo libraryCombo;
	private Button sameButton;
	private boolean moveAllToSame;
	private String outputQueuePath;

	public SpooledFileMoveDialog(Shell shell, SpooledFile spooledFile) {
		super(shell);
		this.spooledFile = spooledFile;
	}
	
	public Control createDialogArea(Composite parent) {
		Composite rtnGroup = (Composite)super.createDialogArea(parent);
		parent.getShell().setText(ExtensionsPlugin.getResourceString("Move_Spooled_File_1")); //$NON-NLS-1$
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		rtnGroup.setLayout(layout);
		rtnGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		Label fileLabel = new Label(rtnGroup, SWT.NONE); 
		fileLabel.setText(ExtensionsPlugin.getResourceString("Spooled_file__2")); //$NON-NLS-1$
		Text fileText = new Text(rtnGroup, SWT.BORDER);
		fileText.setText(spooledFile.getName());
		GridData gd = new GridData();
		gd.widthHint = 75;
		fileText.setLayoutData(gd);
		fileText.setEnabled(false);
		Label jobNameLabel = new Label(rtnGroup, SWT.NONE); 
		jobNameLabel.setText(ExtensionsPlugin.getResourceString("Job_name__3")); //$NON-NLS-1$
		Text jobNameText = new Text(rtnGroup, SWT.BORDER);
		jobNameText.setText(spooledFile.getJobName());
		gd = new GridData();
		gd.widthHint = 75;
		jobNameText.setLayoutData(gd);
		jobNameText.setEnabled(false);
		Label jobUserLabel = new Label(rtnGroup, SWT.NONE); 
		jobUserLabel.setText(ExtensionsPlugin.getResourceString("___Job_user__4")); //$NON-NLS-1$
		Text jobUserText = new Text(rtnGroup, SWT.BORDER);
		jobUserText.setText(spooledFile.getJobUser());
		gd = new GridData();
		gd.widthHint = 75;
		jobUserText.setLayoutData(gd);
		jobUserText.setEnabled(false);
		Label jobNbrLabel = new Label(rtnGroup, SWT.NONE); 
		jobNbrLabel.setText(ExtensionsPlugin.getResourceString("___Job_number__5")); //$NON-NLS-1$
		Text jobNbrText = new Text(rtnGroup, SWT.BORDER);
		jobNbrText.setText(spooledFile.getJobNumber());
		gd = new GridData();
		gd.widthHint = 75;
		jobNbrText.setLayoutData(gd);
		jobNbrText.setEnabled(false);
		Label splNbrLabel = new Label(rtnGroup, SWT.NONE); 
		splNbrLabel.setText(ExtensionsPlugin.getResourceString("Spooled_file_number__6")); //$NON-NLS-1$
		Text splNbrText = new Text(rtnGroup, SWT.BORDER);
		splNbrText.setText(new Integer(spooledFile.getNumber()).toString());
		gd = new GridData();
		gd.widthHint = 75;
		splNbrText.setLayoutData(gd);
		splNbrText.setEnabled(false);
		Label outqLabel = new Label(rtnGroup, SWT.NONE); 
		outqLabel.setText(ExtensionsPlugin.getResourceString("Move_to_output_queue__7")); //$NON-NLS-1$
		outputQueueCombo = new Combo(rtnGroup, SWT.NONE);	
		gd = new GridData();
		gd.widthHint = 75;
		outputQueueCombo.setLayoutData(gd);
		outputQueueCombo.setTextLimit(10);
		outputQueueCombo.add("*SAME"); //$NON-NLS-1$
		outputQueueCombo.add("*DEV"); //$NON-NLS-1$
		outputQueueCombo.select(0);
		Label libLabel = new Label(rtnGroup, SWT.NONE); 
		libLabel.setText(ExtensionsPlugin.getResourceString("___Library__10")); //$NON-NLS-1$
		libraryCombo = new Combo(rtnGroup, SWT.NONE);	
		gd = new GridData();
		gd.widthHint = 75;
		libraryCombo.setLayoutData(gd);
		libraryCombo.setTextLimit(10);
		libraryCombo.add("*LIBL"); //$NON-NLS-1$
		libraryCombo.add("*CURLIB"); //$NON-NLS-1$
		libraryCombo.select(0);
		Composite sameGroup = new Composite(rtnGroup, SWT.NONE);
		GridLayout sameLayout = new GridLayout();
		sameLayout.numColumns = 2;
		sameGroup.setLayout(sameLayout);
		sameGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		gd = new GridData();
		gd.horizontalSpan = 2;
		sameGroup.setLayoutData(gd);
		sameButton = new Button(sameGroup, SWT.CHECK);
		sameButton.setSelection(true);
		Label sameLabel = new Label(sameGroup, SWT.NONE);
		sameLabel.setText(ExtensionsPlugin.getResourceString("Move_all_selections_to_same_output_queue_13")); //$NON-NLS-1$
		
		return rtnGroup;
	}
	
	public void okPressed() {
		String outq = outputQueueCombo.getText().toUpperCase();
		if (outq.startsWith("*")) outq = "%" + outq.substring(1) + "%"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String lib = libraryCombo.getText().toUpperCase();
		if (lib.startsWith("*")) lib = "%" + lib.substring(1) + "%"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		outputQueuePath = "/QSYS.LIB/" + lib + ".LIB/" + outq + ".OUTQ"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		OutputQueue outputQueue = new OutputQueue(spooledFile.getSystem(), outputQueuePath);
		try {
			spooledFile.move(outputQueue);
			moveAllToSame = sameButton.getSelection();
		} catch (Exception e) {
			MessageDialog.openError(getShell(), ExtensionsPlugin.getResourceString("Move_Spooled_File_Error_23"), e.getMessage()); //$NON-NLS-1$
			return;
		}
		super.okPressed();
	}

	public boolean moveAllToSame() {
		return moveAllToSame;
	}

	public String getOutputQueuePath() {
		return outputQueuePath;
	}

}
