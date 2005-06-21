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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;

import com.ibm.etools.systems.dftsubsystem.impl.DefaultSubSystemFactoryImpl;
import com.ibm.etools.systems.filters.SystemFilter;
import com.ibm.etools.systems.filters.SystemFilterPool;
import com.ibm.etools.systems.filters.SystemFilterPoolManager;
import com.ibm.etools.systems.filters.ui.actions.SystemChangeFilterAction;
import com.ibm.etools.systems.filters.ui.actions.SystemNewFilterAction;
import com.ibm.etools.systems.model.SystemConnection;
import com.ibm.etools.systems.subsystems.SubSystem;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class QueuedMessageSubSystemFactory extends DefaultSubSystemFactoryImpl {

	public QueuedMessageSubSystemFactory() {
		super();	
	}
	
	protected SubSystem createSubSystemInternal(SystemConnection conn) {
		QueuedMessageSubSystem subSystem = new QueuedMessageSubSystem();
		return subSystem;
	}
	
	public String getTranslatedFilterTypeProperty(SystemFilter selectedFilter) {
		return ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.0"); //$NON-NLS-1$
	}
	
	protected SystemFilterPool createDefaultFilterPool(SystemFilterPoolManager mgr) {
		SystemFilterPool defaultPool = super.createDefaultFilterPool(mgr);
		Vector strings = new Vector();
		QueuedMessageFilter messageFilter = new QueuedMessageFilter();
		messageFilter.setMessageQueue("*CURRENT"); //$NON-NLS-1$
		strings.add(messageFilter.getFilterString());
		try {
		  SystemFilter filter = mgr.createSystemFilter(defaultPool, ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.2"), strings); //$NON-NLS-1$
		  filter.setType(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.3")); //$NON-NLS-1$
		} catch (Exception exc) {}
		return defaultPool;
	}
	
	protected IAction[] getNewFilterPoolFilterActions(SystemFilterPool selectedPool, Shell shell)
	{
		SystemNewFilterAction filterAction = (SystemNewFilterAction)super.getNewFilterPoolFilterAction(selectedPool, shell);
		filterAction.setWizardPageTitle(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.4")); //$NON-NLS-1$
		filterAction.setPage1Description(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.5")); //$NON-NLS-1$
		filterAction.setType(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.6")); //$NON-NLS-1$
		filterAction.setText(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.7")); //$NON-NLS-1$
		filterAction.setFilterStringEditPane(new QueuedMessageFilterStringEditPane(shell));       		  	
		IAction[] actions = new IAction[1];
		actions[0] = filterAction;
		return actions;
	 }
	 
	protected IAction getChangeFilterAction(SystemFilter selectedFilter, Shell shell)
	{
		SystemChangeFilterAction action = (SystemChangeFilterAction)super.getChangeFilterAction(selectedFilter, shell);
		String type = selectedFilter.getType();
		action.setDialogTitle(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.8")); //$NON-NLS-1$
		action.setFilterStringEditPane(new QueuedMessageFilterStringEditPane(shell));
		return action;
	} 
	
	protected Vector getAdditionalFilterActions(SystemFilter selectedFilter, Shell shell) {
		Vector actions = new Vector();
		return actions;
	}
	
	public ImageDescriptor getSystemFilterImage(SystemFilter filter)
	{
		 return ExtensionsPlugin.getImageDescriptor(ExtensionsPlugin.IMAGE_MESSAGE_FILTER);
	}   	

}