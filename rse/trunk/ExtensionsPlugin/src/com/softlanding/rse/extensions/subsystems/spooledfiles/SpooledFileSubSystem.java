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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.rse.core.model.*;
import org.eclipse.rse.core.subsystems.*;
import org.eclipse.rse.services.clientserver.messages.*;
import org.eclipse.rse.ui.*;
import org.eclipse.swt.widgets.Shell;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SpooledFile;
import com.ibm.etools.iseries.subsystems.qsys.*;
import com.ibm.etools.iseries.subsystems.qsys.api.*;
import com.ibm.etools.iseries.subsystems.qsys.commands.*;
import com.ibm.etools.iseries.subsystems.qsys.objects.*;
import com.softlanding.rse.extensions.spooledfiles.*;

public class SpooledFileSubSystem extends SubSystem implements IISeriesSubSystem, ISpooledFileSubSystem {

	public SpooledFileSubSystem(IHost host, IConnectorService connectorService) {
		super(host, connectorService);
	}
	
	protected Object[] internalResolveFilterString(String filterString, IProgressMonitor monitor)
         throws java.lang.reflect.InvocationTargetException,
                java.lang.InterruptedException                
	{	
		SpooledFileResource[] spooledFileResources;
		SpooledFileFilter spooledFileFilter = new SpooledFileFilter(filterString);
		try {
			AS400 as400 = getToolboxAS400Object();
			SpooledFileFactory factory = new SpooledFileFactory(as400);
			SpooledFile[] spooledFiles = factory.getSpooledFiles(spooledFileFilter);
			spooledFileResources = new SpooledFileResource[spooledFiles.length];
			for (int i = 0; i < spooledFileResources.length; i++) {
				spooledFileResources[i] = new SpooledFileResource(this);
				spooledFileResources[i].setSpooledFile(spooledFiles[i]);
			}			
		} catch (Exception e) {
			handleError(e);
            SystemMessage msg = RSEUIPlugin.getPluginMessage(ISystemMessages.MSG_GENERIC_E); 
            msg.makeSubstitution(e.getMessage()); 
            SystemMessageObject msgObj = new SystemMessageObject(msg, ISystemMessageObject.MSGTYPE_ERROR, null); 
            return new Object[] {msgObj}; 
		}
		return spooledFileResources;
	}  
	
	protected Object[] internalResolveFilterString(Object parent, String filterString, IProgressMonitor monitor)
         throws java.lang.reflect.InvocationTargetException,
                java.lang.InterruptedException
	{
		return internalResolveFilterString(filterString, monitor);
	}
	
    public QSYSCommandSubSystem getCmdSubSystem() {
    	IHost iHost = getHost();
    	ISubSystem[] iSubSystems = iHost.getSubSystems();
    	ISubSystem iSubSystem;
    	for (int ssIndx = 0; ssIndx < iSubSystems.length; ssIndx++) {
    		iSubSystem = iSubSystems[ssIndx];
    		if (iSubSystem instanceof QSYSCommandSubSystem)
    			return (QSYSCommandSubSystem) iSubSystem;
    	}
    	return null;
    }
      
     public QSYSObjectSubSystem getCommandExecutionProperties() {
      return (QSYSObjectSubSystem) getObjectSubSystem();
     }
     
     public QSYSObjectSubSystem getObjectSubSystem() {
    	 return IBMiConnection.getConnection(getHost()).getQSYSObjectSubSystem();
     }
     
      public AS400 getToolboxAS400Object() {
    	  AS400 as400 = null;
		try {
			as400 = IBMiConnection.getConnection(getHost()).getAS400ToolboxObject();
		} catch (SystemMessageException e) {
			e.printStackTrace();
		}
    	  return as400;
      }
     
     public void setShell(Shell shell) {
     	this.shell = shell;
     }
     
      public Shell getShell()
      {
         if (shell != null) // Damn, this caused me a lot of grief! Phil
            return shell;
         else
            return super.getShell();
      }

	private void handleError(Exception e) {
	}

}
