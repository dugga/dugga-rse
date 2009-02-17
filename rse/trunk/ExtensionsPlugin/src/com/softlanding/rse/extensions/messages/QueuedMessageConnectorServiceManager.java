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

import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.subsystems.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.MessageQueue;

public class QueuedMessageConnectorServiceManager extends AbstractConnectorServiceManager {

	@Override
	public IConnectorService getConnectorService(IHost host,
			Class commonSSinterface) {
		// TODO Auto-generated method stub
		return super.getConnectorService(host, commonSSinterface);
	}

	private static QueuedMessageConnectorServiceManager inst;
	
	public QueuedMessageConnectorServiceManager() {
		super();
	}
	
	public static QueuedMessageConnectorServiceManager getInstance() {
		if (inst == null)
			inst = new QueuedMessageConnectorServiceManager();
		return inst;
	}
	
	public IConnectorService createConnectorService(IHost host) {
		return new QueuedMessageConnectorService(host);
	}

	public Class getSubSystemCommonInterface(ISubSystem subsystem) {
		return IQueuedMessageSubSystem.class;
	}

	public boolean sharesSystem(ISubSystem otherSubSystem) {
		return (otherSubSystem instanceof IQueuedMessageSubSystem);
	}
	
}

