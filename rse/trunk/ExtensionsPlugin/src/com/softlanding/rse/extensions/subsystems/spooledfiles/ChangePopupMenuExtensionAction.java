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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.ui.util.CommandPrompter;
import com.ibm.etools.iseries.rse.ui.actions.popupmenu.ISeriesAbstractQSYSPopupMenuAction;
import com.ibm.etools.iseries.rse.util.clprompter.*;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class ChangePopupMenuExtensionAction extends ISeriesAbstractQSYSPopupMenuAction {

	public ChangePopupMenuExtensionAction() {
		super();
	}
	
	public void run() {
		Object[] selection = getSelectedRemoteObjects();
		for (int i = 0; i < selection.length; i++) {
			if (selection[i] instanceof SpooledFileResource) {
				SpooledFileResource spooledFileResource = (SpooledFileResource)selection[i];
				SpooledFile spooledFile = spooledFileResource.getSpooledFile();
				try {
					String cmd = "CHGSPLFA FILE(" + spooledFile.getName() + ") JOB(" + spooledFile.getJobNumber() + "/" + spooledFile.getJobUser() + "/" + spooledFile.getJobName() + ") SPLNBR(" + spooledFile.getNumber() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
					CLPrompter command = new CLPrompter();
					command.setCommandString(cmd);
					command.setConnection(getISeriesConnection());
					command.setParent(Display.getCurrent().getActiveShell());
//					CommandPrompter command = new CommandPrompter(new Frame(), spooledFile.getSystem(), cmd, false, false);
					int rtnCode = command.showDialog();
					if (rtnCode == CommandPrompter.CANCEL) break;	
					CommandCall commandCall = new CommandCall(spooledFile.getSystem(), command.getCommandString());
					if (!commandCall.run()) {
						AS400Message message = commandCall.getMessageList(0);
						if (message != null)
							handleError(new Exception(message.getText()));
					}				
				} catch (Exception e) {
					handleError(e);
				}
			}
		}
	}
	
	private void handleError(Exception e) {
		MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("Change_Spooled_File_Error_7"), e.getMessage()); //$NON-NLS-1$
	}

}