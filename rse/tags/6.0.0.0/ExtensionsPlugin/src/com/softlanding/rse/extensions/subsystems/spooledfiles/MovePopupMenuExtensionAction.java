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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.SpooledFile;
import com.ibm.etools.iseries.core.ui.actions.isv.ISeriesAbstractQSYSPopupMenuExtensionAction;
import com.softlanding.rse.extensions.ExtensionsPlugin;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileMoveDialog;

public class MovePopupMenuExtensionAction extends ISeriesAbstractQSYSPopupMenuExtensionAction {
	private String outputQueuePath;

	public MovePopupMenuExtensionAction() {
		super();
	}
	
	public void run() {
		boolean moveAllToSame = false;
		Object[] selection = getSelectedRemoteObjects();
		for (int i = 0; i < selection.length; i++) {
			if (selection[i] instanceof SpooledFileResource) {
				SpooledFileResource spooledFileResource = (SpooledFileResource)selection[i];
				SpooledFile spooledFile = spooledFileResource.getSpooledFile();
				try {
					if (moveAllToSame) {
						if (!moveSpooledFile(spooledFile)) break;
					} else {
						SpooledFileMoveDialog dialog = new SpooledFileMoveDialog(ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), spooledFile);
						if (dialog.open() == Window.CANCEL) break;
						moveAllToSame = dialog.moveAllToSame();
						outputQueuePath = dialog.getOutputQueuePath();
					}
				} catch (Exception e) {
				}
			}
		}
	}
	
	private boolean moveSpooledFile(SpooledFile spooledFile) {
		OutputQueue outputQueue = new OutputQueue(spooledFile.getSystem(), outputQueuePath);
		try {
			spooledFile.move(outputQueue);
		} catch (Exception e) {
			MessageDialog.openError(ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), ExtensionsPlugin.getResourceString("Move_Spooled_File_Error_1"), e.getMessage()); //$NON-NLS-1$
			return false;
		}
		return true;
	}

}