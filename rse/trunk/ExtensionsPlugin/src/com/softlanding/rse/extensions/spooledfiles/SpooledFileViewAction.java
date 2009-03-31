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

import javax.swing.JFrame;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.vaccess.SpooledFileViewer;

public class SpooledFileViewAction implements IObjectActionDelegate {
	private IWorkbenchPart part;

	public SpooledFileViewAction() {
		super();
	}
	
	public void run(IAction action) {
		Item[] items = null;
		if (part instanceof SpooledFilesView) items = ((SpooledFilesView)part).getSelectedItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				SpooledFileModel model = (SpooledFileModel)items[i].getData();
				SpooledFile splf = model.getSpooledFile();
				SpooledFileViewer splfv = new SpooledFileViewer(splf, 1);
				try {
					splfv.load();
					JFrame frame = new JFrame(splf.getName());
					frame.getContentPane().add(splfv);
					frame.pack();
					frame.show();
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
