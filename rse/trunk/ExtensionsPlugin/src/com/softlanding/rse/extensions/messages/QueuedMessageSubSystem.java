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
import org.eclipse.rse.core.model.*;
import org.eclipse.rse.core.subsystems.*;
import org.eclipse.rse.internal.core.model.*;
import org.eclipse.rse.services.clientserver.messages.*;
import org.eclipse.rse.ui.*;
import org.eclipse.rse.ui.model.ISystemRegistryUI;
import org.eclipse.swt.widgets.Shell;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.MessageQueue;
import com.ibm.as400.access.QueuedMessage;
import com.ibm.etools.iseries.subsystems.qsys.IISeriesSubSystem;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;
import com.ibm.etools.iseries.subsystems.qsys.commands.QSYSCommandSubSystem;
import com.ibm.etools.iseries.subsystems.qsys.objects.IQSYSSubSystemCommandExecutionProperties;
import com.ibm.etools.iseries.subsystems.qsys.objects.QSYSObjectSubSystem;

public class QueuedMessageSubSystem extends SubSystem implements IISeriesSubSystem, IQueuedMessageSubSystem {

private IHost theHost;
private MonitoredMessageQueue monitoredMessageQueue;

	public QueuedMessageSubSystem(IHost host, IConnectorService connectorService) {
		super(host, connectorService);
		connectorService.addCommunicationsListener(new ICommunicationsListener() {
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

	protected Object[] internalResolveFilterString(String filterString, IProgressMonitor monitor)
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
            SystemMessage msg = RSEUIPlugin.getPluginMessage(ISystemMessages.MSG_GENERIC_E); 
            msg.makeSubstitution(e.getMessage()); 
            SystemMessageObject msgObj = new SystemMessageObject(msg, ISystemMessageObject.MSGTYPE_ERROR, null); 
            return new Object[] {msgObj}; 
		}
		return queuedMessageResources;
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
		SubSystem subsystem;
		for (int ssIndx = 0; ssIndx < iSubSystems.length; ssIndx++) {
			subsystem = (SubSystem) iSubSystems[ssIndx];
			if (subsystem instanceof QSYSCommandSubSystem)
				return (QSYSCommandSubSystem) subsystem;
		}
		return null;
	  }
      
	public ISubSystem getObjectSubSystem() {
		IHost iHost = getHost();
		ISubSystem[] iSubSystems = iHost.getSubSystems();
		ISubSystem iSubSystem;
		for (int ssIndx = 0; ssIndx < iSubSystems.length; ssIndx++) {
			iSubSystem = iSubSystems[ssIndx];
			if (iSubSystem instanceof QSYSObjectSubSystem)
				return iSubSystem;
		}
		return null;
	}
	
	
	 public QSYSObjectSubSystem getCommandExecutionProperties() {
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

//	  public IHost getHost() {
//		  IHost iHost = super.getHost();
//		  if (theHost == null) {
//			  String monString = getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.MONITOR);
//			  if ((monString != null) && (monString.equals("true"))) { //$NON-NLS-1$
//				  theHost = iHost;
//				  ISubSystem[] iSubSystems = iHost.getSubSystems();
//				  ISubSystem iSubSystem;
//				  for (int ssIndx = 0; ssIndx < iSubSystems.length; ssIndx++) {
//					  iSubSystem = iSubSystems[ssIndx];
//					  if (iSubSystem instanceof QueuedMessageSubSystem)
//						  iSubSystem.getConnectorService().addCommunicationsListener(new ICommunicationsListener() {
//							  public void communicationsStateChange(CommunicationsEvent ce) {
//								  if (ce.getState() == CommunicationsEvent.AFTER_CONNECT) {
//									  String monString = getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.MONITOR);
//									  if ((monString != null) && (monString.equals("true"))) { //$NON-NLS-1$
//										  QueuedMessageFilter filter = new QueuedMessageFilter();
//										  filter.setMessageQueue("*CURRENT"); //$NON-NLS-1$
//										  AS400 as400 = new AS400(getToolboxAS400Object());
//										  String removeString = getVendorAttribute(MonitoringProperties.VENDOR_ID, MonitoringProperties.REMOVE);
//										  boolean remove = false;
//										  if ((removeString != null) && (removeString.equals("true"))) remove = true; //$NON-NLS-1$
//										  monitoredMessageQueue = new MonitoredMessageQueue(as400, filter.getPath(), filter, new MessageHandler(QueuedMessageSubSystem.this, remove));
//										  monitoredMessageQueue.startMonitoring(MessageQueue.OLD, MessageQueue.ANY);
//									  }
//								  }
//								  if (ce.getState() == CommunicationsEvent.BEFORE_DISCONNECT) {
//									  if (monitoredMessageQueue != null)
//										  monitoredMessageQueue.stopMonitoring();
//								  }
//							  }
//							  public boolean isPassiveCommunicationsListener() {
//								  return true;
//							  }
//						  });
//				  }	
//			  }
//		  }
//		  return iHost;
//	  }
     
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