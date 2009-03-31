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

import java.util.Vector;

import org.eclipse.rse.core.filters.*;
import org.eclipse.rse.core.model.*;
import org.eclipse.rse.core.subsystems.*;
import com.ibm.etools.iseries.subsystems.qsys.objects.QSYSObjectSubSystem;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFileSubSystemFactory extends SubSystemConfiguration {

	public SpooledFileSubSystemFactory() {
		super();
	}
	
	public ISubSystem createSubSystemInternal(IHost host) {
	   	return new SpooledFileSubSystem(host, getConnectorService(host));
	}
	
	public IConnectorService getConnectorService(IHost host) {
		ISubSystem[] subSystems = host.getSubSystems();
		for (int i = 0; i < subSystems.length; i++) {
			ISubSystem subSystem = subSystems[i];
			if (subSystem instanceof QSYSObjectSubSystem) {
				return subSystem.getConnectorService();
			}
			
		}
		return null;
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

	public boolean supportsNestedFilters() {
		return false;
	}
}
