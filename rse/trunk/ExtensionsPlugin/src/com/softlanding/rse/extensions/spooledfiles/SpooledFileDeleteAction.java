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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFileDeleteAction implements IObjectActionDelegate {
	private IWorkbenchPart part;

	public SpooledFileDeleteAction() {
		super();
	}
	
	public void run(IAction action) {
		Item[] items = null;
		if (part instanceof SpooledFilesView) items = ((SpooledFilesView)part).getSelectedItems();
		if (items != null) {
			if (MessageDialog.openQuestion(ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
			ExtensionsPlugin.getResourceString("Confirm_Delete_Title"), //$NON-NLS-1$
			ExtensionsPlugin.getResourceString("Confirm_Delete_Message"))) { //$NON-NLS-1$
				for (int i = 0; i < items.length; i++) {
					SpooledFileModel model = (SpooledFileModel)items[i].getData();
					SpooledFile splf = model.getSpooledFile();
					try {
						splf.delete();
					} catch (Exception e) {
					}
				}
			}
			if (part instanceof SpooledFilesView) ((SpooledFilesView)part).refresh();
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
