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
import com.ibm.etools.iseries.rse.ui.actions.popupmenu.ISeriesAbstractQSYSPopupMenuAction;
import com.ibm.etools.iseries.services.qsys.api.IQSYSJob;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;
import com.ibm.etools.iseries.subsystems.qsys.objects.IRemoteObjectContextProvider;
import com.softlanding.rse.extensions.ExtensionsPlugin;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileFactory;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileFilter;
import com.softlanding.rse.extensions.spooledfiles.SpooledFilesView;

public class JobPopupMenuExtensionAction extends ISeriesAbstractQSYSPopupMenuAction {
	private IQSYSJob iQSYSJob;

	public JobPopupMenuExtensionAction() {
		super();
	}
	
	public void run() {
		if (getFirstSelectedRemoteObject() instanceof IQSYSJob) {
			iQSYSJob = (IQSYSJob)getFirstSelectedRemoteObject();
			String nbr = iQSYSJob.getJobNumber();
			String user = iQSYSJob.getUserName();
			String name = iQSYSJob.getJobName();
			boolean needsRefresh = false;
			try {
				AS400 as400 = IBMiConnection.getConnection(getSystemConnection()).getAS400ToolboxObject();
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
}