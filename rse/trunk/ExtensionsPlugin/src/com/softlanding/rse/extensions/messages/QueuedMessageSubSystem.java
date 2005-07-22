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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.MessageQueue;
import com.ibm.as400.access.QueuedMessage;
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
import com.ibm.etools.systems.subsystems.CommunicationsEvent;
import com.ibm.etools.systems.subsystems.ICommunicationsListener;
import com.ibm.etools.systems.subsystems.ISystem;
import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.impl.AbstractSystemManager;

public class QueuedMessageSubSystem extends DefaultSubSystemImpl implements IISeriesSubSystem, IQueuedMessageSubSystem {

private ISystem theSystem;
private MonitoredMessageQueue monitoredMessageQueue;

	public QueuedMessageSubSystem() {
		super();
	}

	public AbstractSystemManager getSystemManager() {
		return ISeriesSystemManager.getTheISeriesSystemManager();
	}
	
	protected Object[] internalResolveFilterString(IProgressMonitor monitor, String filterString)
		 throws java.lang.reflect.InvocationTargetException,
				java.lang.InterruptedException                
	{	
		QueuedMessageResource[] queuedMessageResources;
		QueuedMessageFilter queuedMessageFilter = new QueuedMessageFilter(filterString);
		try {
			AS400 as400 = getToolboxAS400Object();
			QueuedMessageFactory factory = new QueuedMessageFactory(as400);
			QueuedMessage[] queuedMessages = factory.getQueuedMessages(queuedMessageFilter);
			queuedMessageResources = new QueuedMessageResource[queuedMessages.length];
			for (int i = 0; i < queuedMessageResources.length; i++) {
				queuedMessageResources[i] = new QueuedMessageResource(this);
				queuedMessageResources[i].setQueuedMessage(queuedMessages[i]);
			}			
		} catch (Exception e) {
			handleError(e);
            SystemMessage msg = SystemPlugin.getPluginMessage(ISystemMessages.MSG_GENERIC_E); 
            msg.makeSubstitution(e.getMessage()); 
            SystemMessageObject msgObj = new SystemMessageObject(msg, ISystemMessageObject.MSGTYPE_ERROR, null); 
            return new Object[] {msgObj}; 
		}
		return queuedMessageResources;
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
	 	ISeriesSystemDataStore iSeriesSystemDataStore = (ISeriesSystemDataStore)getSystem();
	   	return iSeriesSystemDataStore;
	 }
     
	 public FileSubSystem getObjectSubSystem() {
		return ISeriesConnection.getConnection(getSystemConnection()).getISeriesFileSubSystem();
	 }
     
	  public AS400 getToolboxAS400Object() {
		  ISeriesSystemToolbox system = (ISeriesSystemToolbox) getSystem();
		  return system.getAS400Object();
	  }
	  
	  public ISystem getSystem() {
	  	ISystem system = super.getSystem();
	  	if (theSystem == null) {
	  		String monString = getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.MONITOR);
	  		if ((monString != null) && (monString.equals("true"))) { //$NON-NLS-1$
		  		theSystem = system;
				theSystem.addCommunicationsListener(new ICommunicationsListener() {
					public void communicationsStateChange(CommunicationsEvent ce) {
						if (ce.getState() == CommunicationsEvent.AFTER_CONNECT) {
							String monString = getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.MONITOR);
							if ((monString != null) && (monString.equals("true"))) { //$NON-NLS-1$
								QueuedMessageFilter filter = new QueuedMessageFilter();
								filter.setMessageQueue("*CURRENT"); //$NON-NLS-1$
								AS400 as400 = new AS400(getToolboxAS400Object());
								String removeString = getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.REMOVE);
								boolean remove = false;
								if ((removeString != null) && (removeString.equals("true"))) remove = true; //$NON-NLS-1$
								monitoredMessageQueue = new MonitoredMessageQueue(as400, filter.getPath(), filter, new MessageHandler(QueuedMessageSubSystem.this, remove));
								monitoredMessageQueue.startMonitoring(MessageQueue.OLD, MessageQueue.ANY);
							}
						}
						if (ce.getState() == CommunicationsEvent.BEFORE_DISCONNECT) {
							if (monitoredMessageQueue != null)
								monitoredMessageQueue.stopMonitoring();
						}
					}
					public boolean isPassiveCommunicationsListener() {
						return true;
					}
				});
	  		}
	  	}
	  	return system;
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