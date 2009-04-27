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
package com.softlanding.rse.extensions.subsystems.spooledfiles;

import java.util.Vector;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.core.filters.ISystemFilter;
import org.eclipse.rse.core.filters.ISystemFilterPool;
import org.eclipse.rse.core.filters.ISystemFilterPoolManager;
import org.eclipse.rse.core.filters.ISystemFilterString;
import org.eclipse.rse.core.subsystems.ISubSystemConfiguration;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.rse.ui.filters.actions.SystemChangeFilterAction;
import org.eclipse.rse.ui.filters.actions.SystemNewFilterAction;
import org.eclipse.rse.ui.propertypages.SystemChangeFilterPropertyPage;
import org.eclipse.rse.ui.propertypages.SystemFilterStringPropertyPage;
import org.eclipse.rse.ui.view.SubSystemConfigurationAdapter;
import org.eclipse.swt.widgets.Shell;

import com.softlanding.rse.extensions.ExtensionsPlugin;

/**
 * Adds functionality to the basic SubSystemConfiguration.
 */
public class SpooledFileSubSystemConfigurationAdapter extends SubSystemConfigurationAdapter
{

	/**
	 * Constructor for SpooledFileSubSystemConfigurationAdapter.
	 */
	public SpooledFileSubSystemConfigurationAdapter()
	{
		super();
	}

	protected IAction[] getNewFilterPoolFilterActions(SystemMenuManager menu,
			IStructuredSelection selection, Shell shell, String menuGroup,
			ISubSystemConfiguration config, ISystemFilterPool selectedPool) {
		SystemNewFilterAction filterAction = (SystemNewFilterAction)super.getNewFilterPoolFilterAction(config, selectedPool, shell);
		filterAction.setWizardPageTitle(ExtensionsPlugin.getResourceString("Spooled_File_Filter_5")); //$NON-NLS-1$
		filterAction.setPage1Description(ExtensionsPlugin.getResourceString("Create_a_new_filter_to_list_spooled_files_6")); //$NON-NLS-1$
		filterAction.setType(ExtensionsPlugin.getResourceString("Spooled_File_Filter_7")); //$NON-NLS-1$
		filterAction.setText(ExtensionsPlugin.getResourceString("Spooled_file_filter_8") + "..."); //$NON-NLS-1$ //$NON-NLS-2$
		filterAction.setFilterStringEditPane(new SpooledFileFilterStringEditPane(shell));
		
		ISystemFilterPoolManager[] filterPoolManager = config.getSystemFilterPoolManagers();
		ISystemFilterPool poolsToSelectFrom[] = null;
		for (int i=0; i<filterPoolManager.length; i++) {
			poolsToSelectFrom = filterPoolManager[i].getSystemFilterPools();
			break;
		}
		if (poolsToSelectFrom != null) {
			filterAction.setAllowFilterPoolSelection(poolsToSelectFrom);
		}
		

		IAction[] actions = new IAction[1];
		actions[0] = filterAction;
		return actions;
	}

	protected IAction getChangeFilterAction(ISubSystemConfiguration factory, ISystemFilter selectedFilter, Shell shell)
	{
		SystemChangeFilterAction action = (SystemChangeFilterAction)super.getChangeFilterAction(factory, selectedFilter, shell);
		String type = selectedFilter.getType();
		action.setDialogTitle(ExtensionsPlugin.getResourceString("Change_Spooled_File_Filter_10")); //$NON-NLS-1$
		action.setFilterStringEditPane(new SpooledFileFilterStringEditPane(shell));
		return action;
	}	        

	public ImageDescriptor getSystemFilterImage(ISystemFilter filter)
	{
		return ExtensionsPlugin.getDefault().getImageDescriptor(ExtensionsPlugin.IMAGE_SPOOLED_FILE_FILTER);
	}

	public void customizeChangeFilterPropertyPage(
			ISubSystemConfiguration config,
			SystemChangeFilterPropertyPage page, ISystemFilter selectedFilter,
			Shell shell) {
		super.customizeChangeFilterPropertyPage(config, page, selectedFilter, shell);
	}

	public void customizeFilterStringPropertyPage(
			ISubSystemConfiguration config,
			SystemFilterStringPropertyPage page,
			ISystemFilterString selectedFilterString, Shell shell) {
		super.customizeFilterStringPropertyPage(config, page, selectedFilterString,
				shell);
	}

	protected Vector getAdditionalFilterActions(ISubSystemConfiguration config,
		ISystemFilter selectedFilter, Shell shell) {
		Vector actions = new Vector();
		actions.add(getChangeFilterAction(config, selectedFilter, shell));
		return actions;
	}
}
