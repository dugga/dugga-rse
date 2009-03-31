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

import org.eclipse.swt.widgets.Display;

import com.ibm.as400.access.SpooledFile;
import com.ibm.etools.iseries.rse.ui.actions.popupmenu.ISeriesAbstractQSYSPopupMenuAction;
import com.softlanding.rse.extensions.spooledfiles.SaveToFileDialog;

public class SavePopupMenuExtensionAction extends ISeriesAbstractQSYSPopupMenuAction {

	public SavePopupMenuExtensionAction() {
		super();
	}
	
	public void run() {
		Object[] selection = getSelectedRemoteObjects();
		for (int i = 0; i < selection.length; i++) {
			if (selection[i] instanceof SpooledFileResource) {
				SpooledFileResource spooledFileResource = (SpooledFileResource)selection[i];
				SpooledFile spooledFile = spooledFileResource.getSpooledFile();
				try {
					SaveToFileDialog dialog = new SaveToFileDialog(Display.getCurrent().getActiveShell(), spooledFile);
					if (dialog.open() == SaveToFileDialog.CANCEL) break;					
				} catch (Exception e) {
				}
			}
		}
	}

}