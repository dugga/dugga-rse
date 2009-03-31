/*******************************************************************************
 * Copyright (c) 2009 SoftLanding Systems, Inc. and others.
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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.core.filters.ISystemFilter;
import org.eclipse.rse.core.filters.ISystemFilterPool;
import org.eclipse.rse.core.subsystems.ISubSystemConfiguration;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.rse.ui.filters.actions.SystemChangeFilterAction;
import org.eclipse.rse.ui.filters.actions.SystemNewFilterAction;
import org.eclipse.rse.ui.view.SubSystemConfigurationAdapter;
import org.eclipse.swt.widgets.Shell;

import com.softlanding.rse.extensions.ExtensionsPlugin;
import com.softlanding.rse.extensions.messages.QueuedMessageFilterStringEditPane;

public class QueuedMessageSubSystemConfigurationAdapter extends SubSystemConfigurationAdapter {

	/**
	 * Constructor for SpooledFileSubSystemConfigurationAdapter.
	 */
	public QueuedMessageSubSystemConfigurationAdapter()
	{
		super();
	}

	protected IAction[] getNewFilterPoolFilterActions(SystemMenuManager menu,
			IStructuredSelection selection, Shell shell, String menuGroup,
			ISubSystemConfiguration config, ISystemFilterPool selectedPool) {
		SystemNewFilterAction filterAction = (SystemNewFilterAction)super.getNewFilterPoolFilterAction(config, selectedPool, shell);
		filterAction.setWizardPageTitle(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.4")); //$NON-NLS-1$
		filterAction.setPage1Description(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.5")); //$NON-NLS-1$
		filterAction.setType(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.6")); //$NON-NLS-1$
		filterAction.setText(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.7")); //$NON-NLS-1$
		filterAction.setFilterStringEditPane(new QueuedMessageFilterStringEditPane(shell));       		  	
		IAction[] actions = new IAction[1];
		actions[0] = filterAction;
		return actions;
	}

	protected IAction getChangeFilterAction(ISubSystemConfiguration factory, ISystemFilter selectedFilter, Shell shell)
	{
		SystemChangeFilterAction action = (SystemChangeFilterAction)super.getChangeFilterAction(factory, selectedFilter, shell);
		String type = selectedFilter.getType();
		action.setDialogTitle(ExtensionsPlugin.getResourceString("QueuedMessageSubSystemFactory.8")); //$NON-NLS-1$
		action.setFilterStringEditPane(new QueuedMessageFilterStringEditPane(shell));
		return action;
	}	        

	public ImageDescriptor getSystemFilterImage(ISystemFilter filter)
	{
		return ExtensionsPlugin.getDefault().getImageDescriptor(ExtensionsPlugin.IMAGE_MESSAGE_FILTER);
	}

	protected Vector getAdditionalFilterActions(ISubSystemConfiguration config,
			ISystemFilter selectedFilter, Shell shell) {
		Vector actions = new Vector();
		actions.add(getChangeFilterAction(config, selectedFilter, shell));
		return actions;
	}
	
}
