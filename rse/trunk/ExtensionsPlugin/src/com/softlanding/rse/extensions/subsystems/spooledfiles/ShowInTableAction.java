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

import org.eclipse.rse.core.filters.*;
import org.eclipse.rse.core.subsystems.*;
import org.eclipse.rse.internal.core.filters.*;
import org.eclipse.rse.ui.actions.*;
import org.eclipse.swt.widgets.Shell;

import com.ibm.as400.access.AS400;
import com.softlanding.rse.extensions.ExtensionsPlugin;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileFactory;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileFilter;
import com.softlanding.rse.extensions.spooledfiles.SpooledFilesView;

public class ShowInTableAction extends SystemBaseAction {
	private SystemFilter filter;

	public ShowInTableAction(String text, Shell parent) {
		super(text, parent);
		allowOnMultipleSelection(false);
	}
	
	public ShowInTableAction(String text, Shell parent, SystemFilter filter) {
		this(text, parent);
		this.filter = filter;
		
	}
	
	public void run() {
		boolean needsRefresh = false;
		String[] filterStrings = filter.getFilterStrings();
		SpooledFileFilter spooledFileFilter = new SpooledFileFilter(filterStrings[0]);
		spooledFileFilter.setDescription(filter.getName());
		SystemFilterReference systemFilterReference = (SystemFilterReference)getFirstSelection();
		SpooledFileSubSystem subSystem = (SpooledFileSubSystem)SubSystemHelpers.getParentSubSystemConfiguration(systemFilterReference);
		try {
			//subSystem.connect();
			//subSystem.getSystem().connect(null);  // changed to comment - fixd WDSC 7.0 BUG PRH
			AS400 as400 = subSystem.getToolboxAS400Object(); 
			SpooledFileFactory factory = new SpooledFileFactory(as400);
			SpooledFilesView.setSpooledFileFactory(factory);
			SpooledFilesView.setFilter(spooledFileFilter);
			if (SpooledFilesView.getView() != null) SpooledFilesView.getView().refresh();
			else needsRefresh = true;
			ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("com.softlanding.rse.extensions.spooledfiles.SpooledFilesView"); //$NON-NLS-1$
			if (needsRefresh && (SpooledFilesView.getView() != null)) SpooledFilesView.getView().refresh();
		} catch (Exception e) {
		}
	}
	
}
