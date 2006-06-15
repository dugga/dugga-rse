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

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SaveToFileDialog extends Dialog {
	private SpooledFile spooledFile;
	private Combo fileTypeCombo;
	private Text fileText;
	private Button browseButton;
	private File file;
	private SaveToFileDelegate[] saveToFileDelegates;
	
	private static String[] formats = { SaveToFile.PLAIN_TEXT, SaveToFile.TIFF_G4};
	private static String[] extensions = { "txt", "tif" }; //$NON-NLS-1$ //$NON-NLS-2$

	public SaveToFileDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public SaveToFileDialog(Shell parentShell, SpooledFile spooledFile) {
		this(parentShell);
		this.spooledFile = spooledFile;
	}
	
	public Control createDialogArea(Composite parent) {
		Composite rtnGroup = (Composite)super.createDialogArea(parent);
		parent.getShell().setText(ExtensionsPlugin.getResourceString("Save_Spooled_File_to_File_3")); //$NON-NLS-1$
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		rtnGroup.setLayout(layout);
		rtnGroup.setLayoutData(
		new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		Label nameLabel = new Label(rtnGroup, SWT.NONE);
		nameLabel.setText(ExtensionsPlugin.getResourceString("Spooled_file__4")); //$NON-NLS-1$
		Text nameText = new Text(rtnGroup, SWT.BORDER);
		nameText.setText(spooledFile.getName());
		nameText.setEnabled(false);
		Label jobLabel = new Label(rtnGroup, SWT.NONE);
		jobLabel.setText(ExtensionsPlugin.getResourceString("Job_name__5")); //$NON-NLS-1$
		Text jobText = new Text(rtnGroup, SWT.BORDER);
		jobText.setText(spooledFile.getJobName());
		jobText.setEnabled(false);
		Label userLabel = new Label(rtnGroup, SWT.NONE);
		userLabel.setText(ExtensionsPlugin.getResourceString("Job_user__6")); //$NON-NLS-1$
		Text userText = new Text(rtnGroup, SWT.BORDER);
		userText.setText(spooledFile.getJobUser());
		userText.setEnabled(false);
		Label jobNbrLabel = new Label(rtnGroup, SWT.NONE);
		jobNbrLabel.setText(ExtensionsPlugin.getResourceString("Job_number__7")); //$NON-NLS-1$
		Text jobNbrText = new Text(rtnGroup, SWT.BORDER);
		jobNbrText.setText(spooledFile.getJobNumber());
		jobNbrText.setEnabled(false);
		Label nbrLabel = new Label(rtnGroup, SWT.NONE);
		nbrLabel.setText(ExtensionsPlugin.getResourceString("File_number__8")); //$NON-NLS-1$
		Text nbrText = new Text(rtnGroup, SWT.BORDER);
		nbrText.setText(new Integer(spooledFile.getNumber()).toString());
		nbrText.setEnabled(false);

		Label fileTypeLabel = new Label(rtnGroup, SWT.NONE);
		fileTypeLabel.setText(ExtensionsPlugin.getResourceString("File_format__9")); //$NON-NLS-1$
		fileTypeCombo = new Combo(rtnGroup, SWT.READ_ONLY);

		try {
			saveToFileDelegates = ExtensionsPlugin.getSaveToFileDelegates(spooledFile.getStringAttribute(SpooledFile.ATTR_PRTDEVTYPE));
			for (int i = 0; i < saveToFileDelegates.length; i++) fileTypeCombo.add(saveToFileDelegates[i].getName());
		} catch (Exception e) {
		}
		
		Label fileLabel = new Label(rtnGroup, SWT.NONE);
		fileLabel.setText(ExtensionsPlugin.getResourceString("Save_to_file__12")); //$NON-NLS-1$
		Composite fileGroup = new Composite(rtnGroup, SWT.NONE);
		GridLayout fileGroupLayout = new GridLayout();
		fileGroupLayout.numColumns = 2;
		fileGroup.setLayout(fileGroupLayout);
		GridData gd = new GridData();
		gd.horizontalIndent = 0;
		fileGroup.setLayoutData(gd);
		fileText = new Text(fileGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 300;
		fileText.setLayoutData(gd);
		browseButton = new Button(fileGroup, SWT.PUSH);
		browseButton.setText(ExtensionsPlugin.getResourceString("Browse..._13")); //$NON-NLS-1$
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {
				selectLink();
			}
		});
		if ((saveToFileDelegates == null) || (saveToFileDelegates.length == 0)) {
			fileTypeCombo.add(ExtensionsPlugin.getResourceString("No_conversion_available_for_selected_spooled_file_1")); //$NON-NLS-1$
			browseButton.setEnabled(false);
			fileText.setEnabled(false);
		}	
		fileTypeCombo.select(0);			
			
		return rtnGroup;
	}
	
	public void okPressed() {
		Runnable runnable = new Runnable() {
			public void run() {
				if ((fileText.getText() != null) && (fileText.getText().trim().length() > 0)) {
					try {
						file = new File(fileText.getText());
						FileOutputStream out = new FileOutputStream(file);
						ISaveToFile saveToFile = saveToFileDelegates[fileTypeCombo.getSelectionIndex()].getSaveToFile();
						if (saveToFile != null)				
							saveToFile.saveAs(spooledFile, out);
						try {
							out.close();
						} catch (Exception exc) {}
					} catch (Exception e) {
						MessageDialog.openError(getShell(), ExtensionsPlugin.getResourceString("Save_Spooled_File_Error_14"), e.getMessage()); //$NON-NLS-1$
						return;
					}
				}
			}
		};
		if ((saveToFileDelegates != null) && (saveToFileDelegates.length != 0))
			BusyIndicator.showWhile(Display.getCurrent(), runnable);
		super.okPressed();
	}
	
	public File getFile() {
		return file;
	}
	
	private void selectLink() {
		String extension = saveToFileDelegates[fileTypeCombo.getSelectionIndex()].getFileExtension();
		FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
		dialog.setFileName(spooledFile.getName() + "_" + spooledFile.getJobNumber() + "_" + spooledFile.getJobUser() + "_" + spooledFile.getJobName() + "_" + spooledFile.getNumber() + "." + extension); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		String fileName = dialog.open();
		if (fileName != null) fileText.setText(fileName);
	}

}
