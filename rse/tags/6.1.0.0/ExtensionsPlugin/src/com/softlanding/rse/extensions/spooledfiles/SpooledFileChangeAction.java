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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.ui.util.CommandPrompter;
import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.iseries.core.util.clprompter.CLPrompter;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFileChangeAction implements IObjectActionDelegate {
	private IWorkbenchPart part;

	public SpooledFileChangeAction() {
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
					String cmd = "CHGSPLFA FILE(" + splf.getName() + ") JOB(" + splf.getJobNumber() + "/" + splf.getJobUser() + "/" + splf.getJobName() + ") SPLNBR(" + splf.getNumber() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
					CLPrompter command = new CLPrompter();
					command.setCommandString(cmd);
					command.setParent(Display.getCurrent().getActiveShell());
					command.setConnection(ISeriesConnection.getConnection(splf.getSystem().getSystemName()));			
//					CommandPrompter command = new CommandPrompter(new Frame(), splf.getSystem(), cmd, false, false);
					int rtnCode = command.showDialog();
					if (rtnCode == CommandPrompter.CANCEL) break;
					CommandCall commandCall = new CommandCall(splf.getSystem(), command.getCommandString());
					if (!commandCall.run()) {
						AS400Message message = commandCall.getMessageList(0);
						if (message != null)
							handleError(new Exception(message.getText()));
					}
				} catch (Exception e) {
					handleError(e);
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
	
	private void handleError(Exception e) {
		MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("Change_Spooled_File_Error_7"), e.getMessage()); //$NON-NLS-1$
	}

}