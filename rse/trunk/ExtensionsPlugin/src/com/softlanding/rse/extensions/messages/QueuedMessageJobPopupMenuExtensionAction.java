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

import org.eclipse.rse.internal.ui.view.*;
import com.ibm.as400.access.QueuedMessage;
import com.ibm.etools.iseries.rse.ui.QSYSViewHelpers;
import com.ibm.etools.iseries.rse.ui.actions.popupmenu.ISeriesAbstractQSYSPopupMenuAction;


public class QueuedMessageJobPopupMenuExtensionAction extends ISeriesAbstractQSYSPopupMenuAction {

	public QueuedMessageJobPopupMenuExtensionAction() {
		super();
	}
	
	public void run() {
		Object[] selection = getSelectedRemoteObjects();
		for (int i = 0; i < selection.length; i++) {
			if (selection[i] instanceof QueuedMessageResource) {
				QueuedMessageResource queuedMessageResource = (QueuedMessageResource)selection[i];
				QueuedMessage queuedMessage = queuedMessageResource.getQueuedMessage();
				SystemPerspectiveHelpers.showView(SystemViewPart.ID);
				SystemViewPart viewPart = (SystemViewPart)SystemPerspectiveHelpers.findView(SystemViewPart.ID);
				SystemView view = viewPart.getSystemView();
				QSYSViewHelpers.selectAndReveal(view, getISeriesConnection(), queuedMessage.getFromJobNumber().trim() + "/" + queuedMessage.getUser().trim() + "/" + queuedMessage.getFromJobName(), true); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			}
		}
	}

}
