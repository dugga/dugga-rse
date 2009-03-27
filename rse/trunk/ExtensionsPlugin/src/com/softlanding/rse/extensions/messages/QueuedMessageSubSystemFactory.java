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
package com.softlanding.rse.extensions.messages;

import java.util.Vector;

import org.eclipse.rse.core.filters.*;
import org.eclipse.rse.core.model.*;
import org.eclipse.rse.core.subsystems.*;
import org.eclipse.rse.internal.core.filters.SystemFilter;
import org.eclipse.swt.widgets.Shell;

import com.ibm.etools.iseries.subsystems.qsys.objects.QSYSObjectSubSystem;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class QueuedMessageSubSystemFactory extends SubSystemConfiguration {

	public QueuedMessageSubSystemFactory() {
		super();	
	}
	
	public ISubSystem createSubSystemInternal(IHost host) {
		return new QueuedMessageSubSystem(host, getConnectorService(host));
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
		return ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.0"); //$NON-NLS-1$
	}
	
	protected ISystemFilterPool createDefaultFilterPool(ISystemFilterPoolManager mgr) {
		ISystemFilterPool defaultPool = super.createDefaultFilterPool(mgr);
		Vector strings = new Vector();
		QueuedMessageFilter messageFilter = new QueuedMessageFilter();
		messageFilter.setMessageQueue("*CURRENT"); //$NON-NLS-1$
		strings.add(messageFilter.getFilterString());
		try {
		  ISystemFilter filter = mgr.createSystemFilter(defaultPool, ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.2"), strings); //$NON-NLS-1$
		  filter.setType(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.3")); //$NON-NLS-1$
		} catch (Exception exc) {}
		return defaultPool;
	}
	
	public boolean supportsNestedFilters() {
		return false;
	}
}