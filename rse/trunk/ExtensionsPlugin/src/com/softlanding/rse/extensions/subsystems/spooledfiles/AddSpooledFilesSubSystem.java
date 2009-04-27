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

import org.eclipse.rse.core.events.*;
import org.eclipse.rse.core.model.*;
import org.eclipse.rse.core.subsystems.*;

public class AddSpooledFilesSubSystem  {

	public AddSpooledFilesSubSystem() {
		super();
	}
	
	public String AddSubSystem() {
		String list = "";
		ISystemRegistry systemRegistry = SystemStartHere.getSystemRegistry();
		ISubSystemConfiguration ssc = systemRegistry.getSubSystemConfiguration("com.softlanding.rse.extensions.spooledfiles.subsystems.factory");
		ssc.getFilterPoolManager(systemRegistry.getSystemProfileManager().getDefaultPrivateSystemProfile(), true);
		if (ssc != null) {
			IHost[] hosts = systemRegistry.getHosts();
			for (int i = 0; i< hosts.length; i++) {
				boolean exists = false;
				IHost host = hosts[i];
				ISubSystem[] subSystems = host.getSubSystems();
				for (int i2 = 0; i2 < subSystems.length; i2++) {
					if (subSystems[i2].getSubSystemConfiguration() == ssc) {
						exists = true;
						break;
					}
				}
				if (!exists && host.getSystemType().getId().toString().equals("org.eclipse.rse.systemtype.iseries")) {
					ssc.createSubSystem(host, true, null);
					systemRegistry.fireEvent(new SystemResourceChangeEvent(host,
					ISystemResourceChangeEvents.EVENT_REFRESH, host));
					host.commit();
					list = list + "   " + host.getAliasName() + " - " + host.getDescription() + "\n";
				}
			}
		}
		return list;
	}
}