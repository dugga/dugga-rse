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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SpooledFile;
import com.ibm.etools.iseries.core.IISeriesSubSystem;
import com.ibm.etools.iseries.core.IISeriesSubSystemCommandExecutionProperties;
import com.ibm.etools.iseries.core.ISeriesSystemDataStore;
import com.ibm.etools.iseries.core.ISeriesSystemManager;
import com.ibm.etools.iseries.core.ISeriesSystemToolbox;
import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.systems.as400cmdsubsys.CmdSubSystem;
import com.ibm.etools.systems.as400cmdsubsys.impl.CmdSubSystemImpl;
import com.ibm.etools.systems.as400filesubsys.FileSubSystem;
import com.ibm.etools.systems.as400filesubsys.impl.FileSubSystemImpl;
import com.ibm.etools.systems.core.ISystemMessages;
import com.ibm.etools.systems.core.SystemPlugin;
import com.ibm.etools.systems.core.messages.SystemMessage;
import com.ibm.etools.systems.dftsubsystem.impl.DefaultSubSystemImpl;
import com.ibm.etools.systems.model.ISystemMessageObject;
import com.ibm.etools.systems.model.SystemConnection;
import com.ibm.etools.systems.model.SystemRegistry;
import com.ibm.etools.systems.model.impl.SystemMessageObject;
import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.impl.AbstractSystemManager;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileFactory;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileFilter;

public class SpooledFileSubSystem extends DefaultSubSystemImpl implements IISeriesSubSystem, ISpooledFileSubSystem {

	public SpooledFileSubSystem() {
		super();
	}
	
//    public AbstractSystemManager getSystemManager() { 
//    	return SpooledFileSystemManager.getSpooledFileSystemManager(); 
//    } 
    
    public AbstractSystemManager getSystemManager() {
    	return ISeriesSystemManager.getTheISeriesSystemManager();
    }
	
	protected Object[] internalResolveFilterString(IProgressMonitor monitor, String filterString)
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
            SystemMessage msg = SystemPlugin.getPluginMessage(ISystemMessages.MSG_GENERIC_E); 
            msg.makeSubstitution(e.getMessage()); 
            SystemMessageObject msgObj = new SystemMessageObject(msg, ISystemMessageObject.MSGTYPE_ERROR, null); 
            return new Object[] {msgObj}; 
		}
		return spooledFileResources;
	}  
	
	protected Object[] internalResolveFilterString(IProgressMonitor monitor, Object parent, String filterString)
         throws java.lang.reflect.InvocationTargetException,
                java.lang.InterruptedException
	{
		return internalResolveFilterString(monitor, filterString);
	}
	
    public CmdSubSystem getCmdSubSystem() {
    	SystemConnection sc = getSystemConnection();
        SystemRegistry registry = SystemPlugin.getTheSystemRegistry();
        SubSystem[] subsystems = registry.getSubSystems(sc);
        SubSystem subsystem;
        for (int ssIndx = 0; ssIndx < subsystems.length; ssIndx++) {
        	subsystem = subsystems[ssIndx];
            if (subsystem instanceof CmdSubSystemImpl)
            	return (CmdSubSystemImpl) subsystem;
        }
        return null;
      }
      
     public IISeriesSubSystemCommandExecutionProperties getCommandExecutionProperties() {
      return (FileSubSystemImpl) getObjectSubSystem();
     }
      
     public ISeriesSystemDataStore getISeriesSystem() {
       return (ISeriesSystemDataStore)getSystem();
     }
     
     public FileSubSystem getObjectSubSystem() {
     	return ISeriesConnection.getConnection(getSystemConnection()).getISeriesFileSubSystem();
     }
     
      public AS400 getToolboxAS400Object() {
          ISeriesSystemToolbox system = (ISeriesSystemToolbox) getSystem();
          return system.getAS400Object();
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
