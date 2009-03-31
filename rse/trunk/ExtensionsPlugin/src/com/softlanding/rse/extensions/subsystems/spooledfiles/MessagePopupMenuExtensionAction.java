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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.SpooledFile;
import com.ibm.etools.iseries.rse.ui.actions.popupmenu.ISeriesAbstractQSYSPopupMenuAction;
import com.softlanding.rse.extensions.ExtensionsPlugin;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileMessageDialog;

public class MessagePopupMenuExtensionAction extends ISeriesAbstractQSYSPopupMenuAction {

	public MessagePopupMenuExtensionAction() {
		super();
	}
	
	public void run() {
		Object[] selection = getSelectedRemoteObjects();
		for (int i = 0; i < selection.length; i++) {
			if (selection[i] instanceof SpooledFileResource) {
				SpooledFileResource spooledFileResource = (SpooledFileResource)selection[i];
				SpooledFile spooledFile = spooledFileResource.getSpooledFile();
				try {
					AS400Message message = null;
					try {
						message = spooledFile.getMessage();
					} catch (Exception e) {
					}
					if (message == null) {
						Object[] messageArguments = {
							spooledFile.getName(),
							new Integer(spooledFile.getNumber())
						};
						MessageDialog.openError(Display.getCurrent().getActiveShell(),
						ExtensionsPlugin.getResourceString("Spooled_File_Message_Error_1"), //$NON-NLS-1$
						ExtensionsPlugin.getString("No_Messages", messageArguments)); //$NON-NLS-1$
						break;
					} else {
						SpooledFileMessageDialog dialog = new SpooledFileMessageDialog(Display.getCurrent().getActiveShell(), spooledFile);
						if (dialog.open() == Window.CANCEL) break;
					}
				} catch (Exception e) {
				}
			}
		}
	}

}