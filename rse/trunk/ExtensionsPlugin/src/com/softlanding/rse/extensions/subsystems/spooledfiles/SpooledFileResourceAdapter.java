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

import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.rse.ui.view.*;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;
import com.softlanding.rse.extensions.spooledfiles.SaveToFile;
import com.softlanding.rse.extensions.spooledfiles.SpooledFileModel;

public class SpooledFileResourceAdapter	extends AbstractSystemViewAdapter
	implements ISystemRemoteElementAdapter {

	public SpooledFileResourceAdapter() {
		super();
	}

	public void addActions(SystemMenuManager menu, IStructuredSelection selection, Shell parent, String menuGroup) {
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return ExtensionsPlugin.getDefault().getImageDescriptor(ExtensionsPlugin.IMAGE_SPOOLED_FILE);
	}
	
	public boolean handleDoubleClick(Object object) {
		if (object instanceof SpooledFileResource) {
			SpooledFileResource spooledFileResource = (SpooledFileResource)object;
			SpooledFile splf = spooledFileResource.getSpooledFile();
			try {
				File tempDir = new File(ExtensionsPlugin.getWorkspace().getRoot().getLocation().toString() + File.separator + ExtensionsPlugin.getTempProject());
				if (!tempDir.exists())
					tempDir.mkdirs();
				File tempFile = new File(tempDir + File.separator + splf.getName() + "_" + splf.getJobNumber() + "_" + splf.getJobUser() + "_" + splf.getJobName() + "_" + splf.getNumber() + ".txt"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				FileOutputStream out = new FileOutputStream(tempFile);
				SaveToFile.saveAsText(splf, out);
				IProject project = ExtensionsPlugin.getWorkspace().getRoot().getProject(ExtensionsPlugin.getTempProject());
				if (!project.exists()) {
					IProjectDescription description = ExtensionsPlugin.getWorkspace().newProjectDescription(project.getName());
					project.create(description, null);
					project.open(null);
				}
				if (!project.isOpen()) project.open(null);
				IFile file = ExtensionsPlugin.getWorkspace().getRoot().getFileForLocation(new Path(tempFile.getAbsolutePath()));
				file.refreshLocal(1, null);
				IWorkbenchPage page = ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
				org.eclipse.ui.ide.IDE.openEditor(page, file);
			} catch (Exception e) {
			}
		}
		return false;
	}

	public String getText(Object element) {
		try {
			String userData = ((SpooledFileResource)element).getSpooledFile().getStringAttribute(PrintObject.ATTR_USERDATA);
			if ((userData != null) && (userData.length() > 0))
				return ((SpooledFileResource)element).getSpooledFile().getName() + " (" + userData + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {}
		return ((SpooledFileResource)element).getSpooledFile().getName();
	}

	public String getAbsoluteName(Object object) {
		SpooledFileResource spooledFileResource = (SpooledFileResource)object;
		return ExtensionsPlugin.getResourceString("Spooled_file__3") + spooledFileResource.getSpooledFile().getName() + " " + spooledFileResource.getSpooledFile().getJobName() + " " + spooledFileResource.getSpooledFile().getJobNumber() + " " + spooledFileResource.getSpooledFile().getJobUser() + " " + spooledFileResource.getSpooledFile().getNumber(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	public String getType(Object element) {
		return ExtensionsPlugin.getResourceString("Spooled_file_resource_8"); //$NON-NLS-1$
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return false;
	}
	
	public boolean showDelete(Object element) {
		return true;
	}
	
	public boolean canDelete(Object element) {
		return true;
	}
	
	public boolean doDelete(Shell shell, Object element) {
		SpooledFileResource spooledFileResource = (SpooledFileResource)element;
		SpooledFile spooledFile = spooledFileResource.getSpooledFile();
		try {
			spooledFile.delete();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Object[] getChildren(IAdaptable arg0, IProgressMonitor arg1) {
		return new Object[0];
	}

	protected IPropertyDescriptor[] internalGetPropertyDescriptors() {
		PropertyDescriptor[] ourPDs = new PropertyDescriptor[15];
		ourPDs[0] = new PropertyDescriptor("file", ExtensionsPlugin.getResourceString("File_10")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[0].setDescription(ExtensionsPlugin.getResourceString("File_10")); //$NON-NLS-1$
		ourPDs[1] = new PropertyDescriptor("outq", ExtensionsPlugin.getResourceString("Output_queue_13")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[1].setDescription(ExtensionsPlugin.getResourceString("Output_queue_13")); //$NON-NLS-1$
		ourPDs[2] = new PropertyDescriptor("dta", ExtensionsPlugin.getResourceString("User_data_16")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[2].setDescription(ExtensionsPlugin.getResourceString("User_data_16")); //$NON-NLS-1$
		ourPDs[3] = new PropertyDescriptor("sts", ExtensionsPlugin.getResourceString("Status_19")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[3].setDescription(ExtensionsPlugin.getResourceString("Status_19")); //$NON-NLS-1$
		ourPDs[4] = new PropertyDescriptor("pages", ExtensionsPlugin.getResourceString("Total_pages_22")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[4].setDescription(ExtensionsPlugin.getResourceString("Total_pages_22")); //$NON-NLS-1$
		ourPDs[5] = new PropertyDescriptor("page", ExtensionsPlugin.getResourceString("Current_page_25")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[5].setDescription(ExtensionsPlugin.getResourceString("Current_page_25")); //$NON-NLS-1$
		ourPDs[6] = new PropertyDescriptor("user", ExtensionsPlugin.getResourceString("Job_user_28")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[6].setDescription(ExtensionsPlugin.getResourceString("Job_user_28")); //$NON-NLS-1$
		ourPDs[7] = new PropertyDescriptor("job", ExtensionsPlugin.getResourceString("Job_name_31")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[7].setDescription(ExtensionsPlugin.getResourceString("Job_name_31")); //$NON-NLS-1$
		ourPDs[8] = new PropertyDescriptor("jobnbr", ExtensionsPlugin.getResourceString("Job_number_34")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[8].setDescription(ExtensionsPlugin.getResourceString("Job_number_34")); //$NON-NLS-1$
		ourPDs[9] = new PropertyDescriptor("nbr", ExtensionsPlugin.getResourceString("File_number_37")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[9].setDescription(ExtensionsPlugin.getResourceString("File_number_37")); //$NON-NLS-1$
		ourPDs[10] = new PropertyDescriptor("pty", ExtensionsPlugin.getResourceString("Priority_40")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[10].setDescription(ExtensionsPlugin.getResourceString("Priority_40")); //$NON-NLS-1$
		ourPDs[11] = new PropertyDescriptor("cpy", ExtensionsPlugin.getResourceString("Copies_43")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[11].setDescription(ExtensionsPlugin.getResourceString("Copies_43")); //$NON-NLS-1$
		ourPDs[12] = new PropertyDescriptor("type", ExtensionsPlugin.getResourceString("Form_type_46")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[12].setDescription(ExtensionsPlugin.getResourceString("Form_type_46")); //$NON-NLS-1$
		ourPDs[13] = new PropertyDescriptor("date", ExtensionsPlugin.getResourceString("Creation_date_49")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[13].setDescription(ExtensionsPlugin.getResourceString("Creation_date_49")); //$NON-NLS-1$
		ourPDs[14] = new PropertyDescriptor("time", ExtensionsPlugin.getResourceString("Creation_time_52")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[14].setDescription(ExtensionsPlugin.getResourceString("Creation_time_52")); //$NON-NLS-1$
		return ourPDs;
	}

	public Object internalGetPropertyValue(Object propKey) {
		try {
			SpooledFileResource spooledFile = (SpooledFileResource)propertySourceInput; 
			if (propKey.equals("file")) return spooledFile.getSpooledFile().getName(); //$NON-NLS-1$
			if ("outq".equals(propKey)) { //$NON-NLS-1$
				SpooledFileModel model = new SpooledFileModel(spooledFile.getSpooledFile());
				return model.getOutputQueue();
			}
			if ("dta".equals(propKey)) return spooledFile.getSpooledFile().getStringAttribute(PrintObject.ATTR_USERDATA); //$NON-NLS-1$
			if ("sts".equals(propKey)) return spooledFile.getSpooledFile().getStringAttribute(PrintObject.ATTR_SPLFSTATUS); //$NON-NLS-1$
			if ("pages".equals(propKey)) return spooledFile.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_PAGES).toString(); //$NON-NLS-1$
			if ("page".equals(propKey)) return spooledFile.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_CURPAGE).toString(); //$NON-NLS-1$
			if ("user".equals(propKey)) return spooledFile.getSpooledFile().getJobUser(); //$NON-NLS-1$
			if ("job".equals(propKey)) return spooledFile.getSpooledFile().getJobName(); //$NON-NLS-1$
			if ("jobnbr".equals(propKey)) return spooledFile.getSpooledFile().getJobNumber(); //$NON-NLS-1$
			if ("nbr".equals(propKey)) return new Integer(spooledFile.getSpooledFile().getNumber()).toString(); //$NON-NLS-1$
			if ("pty".equals(propKey)) return spooledFile.getSpooledFile().getStringAttribute(PrintObject.ATTR_OUTPTY); //$NON-NLS-1$
			if ("cpy".equals(propKey)) return spooledFile.getSpooledFile().getIntegerAttribute(PrintObject.ATTR_COPIES).toString(); //$NON-NLS-1$
			if ("type".equals(propKey)) return spooledFile.getSpooledFile().getStringAttribute(PrintObject.ATTR_FORMTYPE); //$NON-NLS-1$
			if ("date".equals(propKey)) { //$NON-NLS-1$
				SpooledFileModel model = new SpooledFileModel(spooledFile.getSpooledFile());
				return model.getCreateDate();
			}
			if ("time".equals(propKey)) { //$NON-NLS-1$
				SpooledFileModel model = new SpooledFileModel(spooledFile.getSpooledFile());
				return model.getCreateTime();
			}
		} catch (Exception e) {}
		return null;

	}

	public String getAbsoluteParentName(Object element) {
		return "root"; //$NON-NLS-1$
	}

	public String getSubSystemFactoryId(Object element) {
		return "com.softlanding.rse.extensions.spooledfiles.subsystems.factory"; //$NON-NLS-1$
	}

	public String getRemoteTypeCategory(Object element) {
		return "spooled files"; //$NON-NLS-1$
	}

	public String getRemoteType(Object element) {
		return "spooled file"; //$NON-NLS-1$
	}

	public String getRemoteSubType(Object arg0) {
		return null;
	}

	public boolean refreshRemoteObject(Object oldElement, Object newElement) {
		SpooledFileResource oldSpooledFile = (SpooledFileResource)oldElement;
		SpooledFileResource newSpooledFile = (SpooledFileResource)newElement;
		newSpooledFile.setSpooledFile(oldSpooledFile.getSpooledFile());
		return false;
	}

	/* (non-Javadoc)
	 * @see com.ibm.etools.systems.core.ui.view.ISystemRemoteElementAdapter#supportsUserDefinedActions(java.lang.Object)
	 */
	public boolean supportsUserDefinedActions(Object arg0) {
		return false;
	}

	@Override
	public boolean hasChildren(IAdaptable arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getRemoteParent(Object arg0, IProgressMonitor arg1)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRemoteParentNamesInUse(Object arg0, IProgressMonitor arg1)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSubSystemConfigurationId(Object arg0) {
		return "com.softlanding.rse.extensions.spooledfiles.subsystems.factory"; //$NON-NLS-1$
	}
}
