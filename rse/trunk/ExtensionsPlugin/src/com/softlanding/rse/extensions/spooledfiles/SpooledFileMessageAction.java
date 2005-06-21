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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFileMessageAction implements IObjectActionDelegate {
	private IWorkbenchPart part;

	public SpooledFileMessageAction() {
		super();
	}
	
	public void run(IAction action) {
		Item[] items = null;
		if (part instanceof SpooledFilesView) items = ((SpooledFilesView)part).getSelectedItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				SpooledFileModel model = (SpooledFileModel)items[i].getData();
				SpooledFile splf = model.getSpooledFile();
				AS400Message message = null;
				try {
					message = splf.getMessage();
				} catch (Exception e) {
				}
				if (message == null) {
					Object[] messageArguments = {
						splf.getName(),
						new Integer(splf.getNumber())
					};
					MessageDialog.openError(Display.getCurrent().getActiveShell(),
					ExtensionsPlugin.getResourceString("Spooled_File_Message_Error_1"), //$NON-NLS-1$
					ExtensionsPlugin.getString("No_Messages", messageArguments)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					break;
				} else {
					SpooledFileMessageDialog dialog = new SpooledFileMessageDialog(Display.getCurrent().getActiveShell(), splf);
					if (dialog.open() == Window.CANCEL) break;
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