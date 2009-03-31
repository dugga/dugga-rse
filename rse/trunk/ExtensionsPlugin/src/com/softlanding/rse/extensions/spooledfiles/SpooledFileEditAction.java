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

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFileEditAction implements IObjectActionDelegate {
	private IWorkbenchPart part;

	public SpooledFileEditAction() {
		super();
	}
	
	public void run(IAction action) {
		Item[] items = null;
		if (part instanceof SpooledFilesView) items = ((SpooledFilesView)part).getSelectedItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				SpooledFileModel model = (SpooledFileModel)items[i].getData();
				SpooledFile splf = model.getSpooledFile();
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
	
	public void setActivePart(IAction action, IWorkbenchPart part) {
		if (part != null)
			this.part = part;
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		setActivePart(action, part);
	}

}