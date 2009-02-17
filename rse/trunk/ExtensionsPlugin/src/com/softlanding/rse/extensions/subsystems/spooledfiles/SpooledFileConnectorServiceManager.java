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

import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.subsystems.*;

public class SpooledFileConnectorServiceManager extends AbstractConnectorServiceManager {

	private static SpooledFileConnectorServiceManager inst;
	
	public SpooledFileConnectorServiceManager() {
		super();
	}
	
	public static SpooledFileConnectorServiceManager getInstance() {
		if (inst == null)
			inst = new SpooledFileConnectorServiceManager();
		return inst;
	}
	
	public IConnectorService createConnectorService(IHost host) {
		return new SpooledFileConnectorService(host);
	}

	public Class getSubSystemCommonInterface(ISubSystem subsystem) {
		return ISpooledFileSubSystem.class;
	}

	public boolean sharesSystem(ISubSystem otherSubSystem) {
		return (otherSubSystem instanceof ISpooledFileSubSystem);
	}
	
}

