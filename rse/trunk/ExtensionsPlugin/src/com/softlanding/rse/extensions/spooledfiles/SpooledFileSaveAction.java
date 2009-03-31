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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.ibm.as400.access.SpooledFile;

public class SpooledFileSaveAction implements IObjectActionDelegate {
	private IWorkbenchPart part;

	public SpooledFileSaveAction() {
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
					SaveToFileDialog dialog = new SaveToFileDialog(Display.getCurrent().getActiveShell(), splf);
					if (dialog.open() == SaveToFileDialog.CANCEL) break;
					File file = dialog.getFile();
					if (file != null) {
						Program.launch(file.getAbsolutePath());
					}
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