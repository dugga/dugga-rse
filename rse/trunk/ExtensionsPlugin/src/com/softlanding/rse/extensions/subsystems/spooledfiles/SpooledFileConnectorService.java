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

import org.eclipse.core.runtime.*;
import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.subsystems.*;

public class SpooledFileConnectorService extends AbstractConnectorService {

	private boolean connected = false;

	public SpooledFileConnectorService(IHost host) {
		super("SpooledFileConnectorService", null, host, 0);
	}
	
	public boolean isConnected() {
		return connected;
	}	
	
	protected void internalConnect(IProgressMonitor monitor) throws Exception {
		connected = true;
	}

	protected void internalDisconnect(IProgressMonitor monitor) throws Exception {
		connected = false;
	}
	
	@Override
	public void acquireCredentials(boolean arg0)
			throws OperationCanceledException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearCredentials() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearPassword(boolean arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPassword(boolean arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean inheritsCredentials() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean isSuppressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removePassword() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserId() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requiresPassword() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requiresUserId() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void savePassword() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserId() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPassword(String arg0, String arg1, boolean arg2, boolean arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSuppressed(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserId(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean sharesCredentials() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsPassword() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsUserId() {
		// TODO Auto-generated method stub
		return false;
	}
}
	

