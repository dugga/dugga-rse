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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFileMoveAction implements IObjectActionDelegate {
	private IWorkbenchPart part;
	private String outputQueuePath;

	public SpooledFileMoveAction() {
		super();
	}
	
	public void run(IAction action) {
		Item[] items = null;
		if (part instanceof SpooledFilesView) items = ((SpooledFilesView)part).getSelectedItems();
		if (items != null) {
			boolean moveAllToSame = false;
			for (int i = 0; i < items.length; i++) {
				SpooledFileModel model = (SpooledFileModel)items[i].getData();
				SpooledFile splf = model.getSpooledFile();
				try {
					if (moveAllToSame) {
						if (!moveSpooledFile(splf)) break;
					} else {
						SpooledFileMoveDialog dialog = new SpooledFileMoveDialog(ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), splf);
						if (dialog.open() == Window.CANCEL) break;
						moveAllToSame = dialog.moveAllToSame();
						outputQueuePath = dialog.getOutputQueuePath();
					}
				} catch (Exception e) {
				}
			}
			if (part instanceof SpooledFilesView) ((SpooledFilesView)part).refresh();
		}
	}

	private boolean moveSpooledFile(SpooledFile splf) {
		OutputQueue outputQueue = new OutputQueue(splf.getSystem(), outputQueuePath);
		try {
			splf.move(outputQueue);
		} catch (Exception e) {
			MessageDialog.openError(ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), ExtensionsPlugin.getResourceString("Move_Spooled_File_Error_1"), e.getMessage()); //$NON-NLS-1$
			return false;
		}
		return true;
	}
	
	public void setActivePart(IAction action, IWorkbenchPart part) {
		if (part != null)
			this.part = part;
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		setActivePart(action, part);
	}

}