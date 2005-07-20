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

public class SpooledFileSubSystemFactory extends DefaultSubSystemFactoryImpl {

	public SpooledFileSubSystemFactory() {
		super();
	}
	
	protected SubSystem createSubSystemInternal(SystemConnection conn) {
	   	return new SpooledFileSubSystem();
	}
	
	public String getTranslatedFilterTypeProperty(SystemFilter selectedFilter) {
	   	return ExtensionsPlugin.getResourceString("Spooled_File_Filter_1");  //$NON-NLS-1$
	}
	
	protected SystemFilterPool createDefaultFilterPool(SystemFilterPoolManager mgr) {
		SystemFilterPool defaultPool = super.createDefaultFilterPool(mgr);
		Vector strings = new Vector();
		strings.add("*CURRENT/*/*/*/*/*/*/*/*"); //$NON-NLS-1$
		try {
		  SystemFilter filter = mgr.createSystemFilter(defaultPool, ExtensionsPlugin.getResourceString("Your_spooled_files_3"), strings); //$NON-NLS-1$
		  filter.setType("spooled file"); //$NON-NLS-1$
		} catch (Exception exc) {}
		return defaultPool;
	}
	
	protected IAction[] getNewFilterPoolFilterActions(SystemFilterPool selectedPool, Shell shell)
	{
	  	SystemNewFilterAction filterAction = (SystemNewFilterAction)super.getNewFilterPoolFilterAction(selectedPool, shell);
	  	filterAction.setWizardPageTitle(ExtensionsPlugin.getResourceString("Spooled_File_Filter_5")); //$NON-NLS-1$
	  	filterAction.setPage1Description(ExtensionsPlugin.getResourceString("Create_a_new_filter_to_list_spooled_files_6")); //$NON-NLS-1$
	  	filterAction.setType(ExtensionsPlugin.getResourceString("Spooled_File_Filter_7")); //$NON-NLS-1$
	  	filterAction.setText(ExtensionsPlugin.getResourceString("Spooled_file_filter_8") + "..."); //$NON-NLS-1$ //$NON-NLS-2$
	  	filterAction.setFilterStringEditPane(new SpooledFileFilterStringEditPane(shell));       		  	
	  	IAction[] actions = new IAction[1];
	  	actions[0] = filterAction;
	  	return actions;
	 }
	 
	protected IAction getChangeFilterAction(SystemFilter selectedFilter, Shell shell)
	{
	  	SystemChangeFilterAction action = (SystemChangeFilterAction)super.getChangeFilterAction(selectedFilter, shell);
	  	String type = selectedFilter.getType();
	  	action.setDialogTitle(ExtensionsPlugin.getResourceString("Change_Spooled_File_Filter_10")); //$NON-NLS-1$
	  	action.setFilterStringEditPane(new SpooledFileFilterStringEditPane(shell));
	  	return action;
	} 
	
	protected Vector getAdditionalFilterActions(SystemFilter selectedFilter, Shell shell) {
		Vector actions = new Vector();
		ShowInTableAction showInTableAction = new ShowInTableAction(ExtensionsPlugin.getResourceString("Show_in_Table_11"), shell, selectedFilter); //$NON-NLS-1$
		actions.add(showInTableAction);
		return actions;
	}
	
	public ImageDescriptor getSystemFilterImage(SystemFilter filter)
	{
	   	 return ExtensionsPlugin.getImageDescriptor(ExtensionsPlugin.IMAGE_SPOOLED_FILE_FILTER);
	}   	

}
