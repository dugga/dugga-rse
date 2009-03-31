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
package com.softlanding.rse.extensions.subsystems.spooledfiles;

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;

import com.ibm.as400.access.SpooledFile;
import com.ibm.etools.iseries.rse.ui.actions.popupmenu.ISeriesAbstractQSYSPopupMenuAction;
import com.softlanding.rse.extensions.ExtensionsPlugin;
import com.softlanding.rse.extensions.spooledfiles.SaveToFile;

public class EditPopupMenuExtensionAction extends ISeriesAbstractQSYSPopupMenuAction {

	public EditPopupMenuExtensionAction() {
		super();
	}
	
	public void run() {
		Object[] selection = getSelectedRemoteObjects();
		for (int i = 0; i < selection.length; i++) {
			if (selection[i] instanceof SpooledFileResource) {
				SpooledFileResource spooledFileResource = (SpooledFileResource)selection[i];
				SpooledFile splf = spooledFileResource.getSpooledFile();
				try {
					File tempDir = new File(ExtensionsPlugin.getWorkspace().getRoot().getLocation().toString() + File.separator + ExtensionsPlugin.getTempProject());
					if (!tempDir.exists())
						tempDir.mkdirs();
					File tempFile = new File(tempDir + File.separator + splf.getName() + "_" + splf.getJobNumber() + "_" + splf.getJobUser() + "_" + splf.getJobName() + "_" + splf.getNumber() + ".txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
					FileOutputStream out = new FileOutputStream(tempFile);
					SaveToFile.saveAsText(splf, out);
					IProject project = ExtensionsPlugin.getWorkspace().getRoot().getProject(ExtensionsPlugin.getTempProject());
					if (!project.exists()) {
						IProjectDescription description = ExtensionsPlugin.getWorkspace().newProjectDescription(project.getName());
						project.create(description, null);
						project.open(null);
					}
					if (!project.isOpen()) project.open(null);
					IFile file = ExtensionsPlugin.getWorkspace().getRoot().getFileForLocation(new Path(tempFile.getAbsolutePath()));
					file.refreshLocal(1, null);
//					FileEditorInput input = new FileEditorInput(file);
					IWorkbenchPage page = ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
					org.eclipse.ui.ide.IDE.openEditor(page, file);
				} catch (Exception e) {
				}
			}
		}
	}

}