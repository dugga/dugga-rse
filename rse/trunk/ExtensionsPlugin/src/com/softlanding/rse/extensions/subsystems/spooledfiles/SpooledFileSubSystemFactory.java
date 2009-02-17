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

import java.util.Vector;

import org.eclipse.rse.core.filters.*;
import org.eclipse.rse.core.model.*;
import org.eclipse.rse.core.subsystems.*;
import org.eclipse.rse.internal.core.filters.SystemFilter;
import org.eclipse.swt.widgets.Shell;

import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFileSubSystemFactory extends SubSystemConfiguration {

	public SpooledFileSubSystemFactory() {
		super();
	}
	
	public ISubSystem createSubSystemInternal(IHost host) {
	   	return new SpooledFileSubSystem(host, getConnectorService(host));
	}
	
	public IConnectorService getConnectorService(IHost host) {
		return SpooledFileConnectorServiceManager.getInstance().getConnectorService(host, ISpooledFileSubSystem.class);
	}
	
	public String getTranslatedFilterTypeProperty(ISystemFilter selectedFilter) {
	   	return ExtensionsPlugin.getResourceString("Spooled_File_Filter_1");  //$NON-NLS-1$
	}
	
	protected ISystemFilterPool createDefaultFilterPool(ISystemFilterPoolManager mgr) {
		ISystemFilterPool defaultPool = super.createDefaultFilterPool(mgr);
		Vector strings = new Vector();
		strings.add("*CURRENT/*/*/*/*/*/*/*/*"); //$NON-NLS-1$
		try {
		  ISystemFilter filter = mgr.createSystemFilter(defaultPool, ExtensionsPlugin.getResourceString("Your_spooled_files_3"), strings); //$NON-NLS-1$
		  filter.setType("spooled file"); //$NON-NLS-1$
		} catch (Exception exc) {}
		return defaultPool;
	}

	protected Vector getAdditionalFilterActions(SystemFilter selectedFilter, Shell shell) {
		Vector actions = new Vector();
		ShowInTableAction showInTableAction = new ShowInTableAction(ExtensionsPlugin.getResourceString("Show_in_Table_11"), shell, selectedFilter); //$NON-NLS-1$
		actions.add(showInTableAction);
		return actions;
	}
	
}
