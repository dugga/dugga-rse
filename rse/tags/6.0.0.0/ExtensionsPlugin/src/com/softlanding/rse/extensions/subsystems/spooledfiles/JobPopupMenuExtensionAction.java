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

import com.ibm.as400.access.AS400;
import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.iseries.core.ui.actions.isv.ISeriesAbstractQSYSPopupMenuExtensionAction;
import com.ibm.etools.iseries.core.ui.view.ISeriesJobAdapter;
import com.softlanding.rse.extensions.ExtensionsPlugin;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileFactory;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileFilter;
import com.softlanding.rse.extensions.spooledfiles.SpooledFilesView;

public class JobPopupMenuExtensionAction extends ISeriesAbstractQSYSPopupMenuExtensionAction {

	public JobPopupMenuExtensionAction() {
		super();
	}
	
	public void run() {
		ISeriesJobAdapter adapter = (ISeriesJobAdapter)getFirstSelectedRemoteObjectAdapter();
		String absoluteName = adapter.getAbsoluteName(getFirstSelectedRemoteObject());
		String nbr = absoluteName.substring(0, 6);
		String user = absoluteName.substring(7, absoluteName.indexOf("/", 7)); //$NON-NLS-1$
		String name = absoluteName.substring(absoluteName.indexOf("/", 7) + 1); //$NON-NLS-1$
		boolean needsRefresh = false;
		try {
			AS400 as400 = ISeriesConnection.getConnection(adapter.getSubSystem(getFirstSelectedRemoteObject()).getSystemConnection()).getAS400ToolboxObject(getShell());
			SpooledFileFactory factory = new SpooledFileFactory(as400);
			SpooledFileFilter filter = new SpooledFileFilter();
			filter.setJobFilter(name);
			filter.setJobNumberFilter(nbr);
			filter.setUserFilter(user);
			filter.setDescription(ExtensionsPlugin.getResourceString("Spooled_Files_for_Job__3") + nbr + "/" + user + "/" + name); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			SpooledFilesView.setSpooledFileFactory(factory);
			SpooledFilesView.setFilter(filter);
			if (SpooledFilesView.getView() != null) SpooledFilesView.getView().refresh();
			else needsRefresh = true;
			ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("com.softlanding.rse.extensions.spooledfiles.SpooledFilesView"); //$NON-NLS-1$
			if (needsRefresh && (SpooledFilesView.getView() != null)) SpooledFilesView.getView().refresh();
		} catch (Exception e) {
		}
	}

}