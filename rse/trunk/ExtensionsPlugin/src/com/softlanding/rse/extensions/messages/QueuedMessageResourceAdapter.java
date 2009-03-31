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
package com.softlanding.rse.extensions.messages;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.rse.ui.SystemMenuManager;
import org.eclipse.rse.ui.view.*;

import com.ibm.as400.access.QueuedMessage;
import com.softlanding.rse.extensions.ExtensionsPlugin;


public class QueuedMessageResourceAdapter
	extends AbstractSystemViewAdapter
	implements ISystemRemoteElementAdapter {

	public QueuedMessageResourceAdapter() {
		super();
	}

	public void addActions(SystemMenuManager menu, IStructuredSelection selection, Shell parent, String menuGroup) {
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		QueuedMessageResource queuedMessageResource = (QueuedMessageResource)object;
		if (queuedMessageResource.getQueuedMessage().getType() == QueuedMessage.INQUIRY)
			return ExtensionsPlugin.getDefault().getImageDescriptor(ExtensionsPlugin.IMAGE_INQUIRY);
		else
			return ExtensionsPlugin.getDefault().getImageDescriptor(ExtensionsPlugin.IMAGE_MESSAGE);
	}
	
	public boolean handleDoubleClick(Object object) {
		if (object instanceof QueuedMessageResource) {
			QueuedMessageResource queuedMessageResource = (QueuedMessageResource)object;
			QueuedMessage queuedMessage = queuedMessageResource.getQueuedMessage();
			QueuedMessageDialog dialog = new QueuedMessageDialog(Display.getCurrent().getActiveShell(), queuedMessage);
			dialog.open();
		}
		return false;
	}

	public String getText(Object element) {
		return ((QueuedMessageResource)element).getQueuedMessage().getText();
	}

	public String getAbsoluteName(Object object) {
		QueuedMessageResource queuedMessageResource = (QueuedMessageResource)object;
		return ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.0") + queuedMessageResource.getQueuedMessage().getKey(); //$NON-NLS-1$
	}

	public String getType(Object element) {
		return ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.1"); //$NON-NLS-1$
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
		QueuedMessageResource queuedMessageResource = (QueuedMessageResource)element;
		QueuedMessage queuedMessage = queuedMessageResource.getQueuedMessage();
		try {
			queuedMessage.getQueue().remove(queuedMessage.getKey());
		} catch (Exception e) {
			String errorMessage = null;
			if (e.getMessage() == null) errorMessage = e.toString();
			else errorMessage = e.getMessage();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.2"), errorMessage); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	@Override
	public Object[] getChildren(IAdaptable arg0, IProgressMonitor arg1) {
		return new Object[0];
	}

	protected IPropertyDescriptor[] internalGetPropertyDescriptors() {
		PropertyDescriptor[] ourPDs = new PropertyDescriptor[9];
		ourPDs[0] = new PropertyDescriptor("from", ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.4")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[0].setDescription(ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.5")); //$NON-NLS-1$
		ourPDs[1] = new PropertyDescriptor("msgid", ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.7")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[1].setDescription(ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.8")); //$NON-NLS-1$
		ourPDs[2] = new PropertyDescriptor("sev", ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.10")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[2].setDescription(ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.11")); //$NON-NLS-1$
		ourPDs[3] = new PropertyDescriptor("type", ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.13")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[3].setDescription(ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.14")); //$NON-NLS-1$
		ourPDs[4] = new PropertyDescriptor("date", ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.16")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[4].setDescription(ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.17")); //$NON-NLS-1$
		ourPDs[5] = new PropertyDescriptor("job", ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.19")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[5].setDescription(ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.20")); //$NON-NLS-1$
		ourPDs[6] = new PropertyDescriptor("jobnbr", ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.22")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[6].setDescription(ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.23")); //$NON-NLS-1$
		ourPDs[7] = new PropertyDescriptor("pgm", ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.25")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[7].setDescription(ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.26")); //$NON-NLS-1$
		ourPDs[8] = new PropertyDescriptor("replySts", ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.28")); //$NON-NLS-1$ //$NON-NLS-2$
		ourPDs[8].setDescription(ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.29")); //$NON-NLS-1$
		return ourPDs;
	}

	public Object internalGetPropertyValue(Object propKey) {
		try {
			QueuedMessageResource queuedMessage = (QueuedMessageResource)propertySourceInput; 
			if (propKey.equals("from")) return queuedMessage.getQueuedMessage().getUser(); //$NON-NLS-1$
			if (propKey.equals("msgid")) return queuedMessage.getQueuedMessage().getID(); //$NON-NLS-1$
			if (propKey.equals("sev")) return new Integer(queuedMessage.getQueuedMessage().getSeverity()).toString(); //$NON-NLS-1$
			if (propKey.equals("type")) { //$NON-NLS-1$
				switch (queuedMessage.getQueuedMessage().getType()) {
					case QueuedMessage.COMPLETION :
						return "Completion"; //$NON-NLS-1$
					case QueuedMessage.DIAGNOSTIC :
						return "Diagnostic"; //$NON-NLS-1$
					case QueuedMessage.INFORMATIONAL :
						return "Informational"; //$NON-NLS-1$
					case QueuedMessage.INQUIRY :
						return "Inquiry"; //$NON-NLS-1$
					case QueuedMessage.SENDERS_COPY :
						return "Senders copy"; //$NON-NLS-1$
					case QueuedMessage.REQUEST :
						return "Request"; //$NON-NLS-1$
					case QueuedMessage.REQUEST_WITH_PROMPTING :
						return "Request with prompting"; //$NON-NLS-1$
					case QueuedMessage.NOTIFY :
						return "Notify"; //$NON-NLS-1$
					default :
						return ""; //$NON-NLS-1$
				}
			}
			if (propKey.equals("date")) return queuedMessage.getQueuedMessage().getDate().getTime().toString(); //$NON-NLS-1$
			if (propKey.equals("job")) return queuedMessage.getQueuedMessage().getFromJobName(); //$NON-NLS-1$
			if (propKey.equals("jobnbr")) return queuedMessage.getQueuedMessage().getFromJobNumber(); //$NON-NLS-1$
			if (propKey.equals("pgm")) return queuedMessage.getQueuedMessage().getFromProgram(); //$NON-NLS-1$
			if (propKey.equals("replyStatus")) return queuedMessage.getQueuedMessage().getReplyStatus(); //$NON-NLS-1$
		} catch (Exception e) {}
		return null;
	}

	public String getAbsoluteParentName(Object element) {
		return "root"; //$NON-NLS-1$
	}

	public String getSubSystemFactoryId(Object element) {
		return "com.softlanding.rse.extensions.messages.factory"; //$NON-NLS-1$
	}

	public String getRemoteTypeCategory(Object element) {
		return ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.50"); //$NON-NLS-1$
	}

	public String getRemoteType(Object element) {
		return ExtensionsPlugin.getResourceString("QueuedMessageResourceAdapter.51"); //$NON-NLS-1$
	}

	public String getRemoteSubType(Object arg0) {
		return null;
	}

	public boolean refreshRemoteObject(Object oldElement, Object newElement) {
		QueuedMessageResource oldQueuedMessage = (QueuedMessageResource)oldElement;
		QueuedMessageResource newQueuedMessage = (QueuedMessageResource)newElement;
		newQueuedMessage.setQueuedMessage(oldQueuedMessage.getQueuedMessage());
		return false;
	}

	public Object getRemoteParent(Shell arg0, Object arg1) throws Exception {
		return null;
	}

	public String[] getRemoteParentNamesInUse(Shell shell, Object element)
		throws Exception {
		return null;	
	}

	/* (non-Javadoc)
	 * @see com.ibm.etools.systems.core.ui.view.ISystemRemoteElementAdapter#supportsUserDefinedActions(java.lang.Object)
	 */
	public boolean supportsUserDefinedActions(Object arg0) {
		// TODO Auto-generated method stub
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

	@Override
	public String getSubSystemConfigurationId(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}