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

import javax.swing.JFrame;

import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.vaccess.SpooledFileViewer;
import com.ibm.etools.iseries.rse.ui.actions.popupmenu.ISeriesAbstractQSYSPopupMenuAction;

public class ViewPopupMenuExtensionAction extends ISeriesAbstractQSYSPopupMenuAction {

	public ViewPopupMenuExtensionAction() {
		super();
	}
	
	public void run() {
		Object[] selection = getSelectedRemoteObjects();
		for (int i = 0; i < selection.length; i++) {
			if (selection[i] instanceof SpooledFileResource) {
				SpooledFileResource spooledFileResource = (SpooledFileResource)selection[i];
				SpooledFile spooledFile = spooledFileResource.getSpooledFile();
				SpooledFileViewer viewer = new SpooledFileViewer(spooledFile, 1);
				try {
					viewer.load();
					JFrame frame = new JFrame(spooledFile.getName());
					frame.getContentPane().add(viewer);
					frame.pack();
					frame.show();
				} catch (Exception e) {
				}
			}
		}
	}

}
