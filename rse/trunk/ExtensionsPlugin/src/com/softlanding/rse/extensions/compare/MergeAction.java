/*******************************************************************************
 * Copyright (c) 2005-2006 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *******************************************************************************/
package com.softlanding.rse.extensions.compare;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

import com.ibm.etools.iseries.core.api.ISeriesMember;
import com.ibm.etools.iseries.core.ui.actions.isv.ISeriesAbstractQSYSPopupMenuExtensionAction;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class MergeAction extends ISeriesAbstractQSYSPopupMenuExtensionAction {

    public MergeAction() {
        super();
    }

    public void run() {
        ISeriesMember[] members = getSelectedMembers();
        for (int i = 0; i < members.length; i++) {
            final MergeDialog dialog = new MergeDialog(getShell(), members[i]);
            if (dialog.open() == MergeDialog.CANCEL) break;
            BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                public void run() {
                    try {
                        ISeriesMember maintenanceMember = dialog.getMaintenanceConnection().getISeriesMember(getShell(), dialog.getMaintenanceLibrary(), dialog.getMaintenanceFile(), dialog.getMaintenanceMember());
                        if (maintenanceMember == null || !maintenanceMember.exists()) {
                			MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("MergeAction.0"), //$NON-NLS-1$
                					ExtensionsPlugin.getString("MergeAction.1",                           //$NON-NLS-1$
                							new Object[] {dialog.getMaintenanceMember(), dialog.getMaintenanceFile(), dialog.getMaintenanceLibrary()}));  
                            return;
                        }
                        ISeriesMember rootMember = dialog.getRootConnection().getISeriesMember(getShell(), dialog.getRootLibrary(), dialog.getRootFile(), dialog.getRootMember());
                        if (rootMember == null || !rootMember.exists()) {
                			MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("MergeAction.0"), //$NON-NLS-1$
                					ExtensionsPlugin.getString("MergeAction.1",                           //$NON-NLS-1$
                							new Object[] {dialog.getRootMember(), dialog.getRootFile(), dialog.getRootLibrary()}));  
                            return;
                        }     
                        CompareConfiguration cc = new CompareConfiguration();
                		MergeCompareInput fInput = new MergeCompareInput(cc, rootMember, dialog.getTargetMember(), maintenanceMember);
                		fInput.initializeCompareConfiguration();
                		CompareUI.openCompareEditorOnPage(fInput, ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage());
                		fInput.cleanup();
                		fInput = null;
                    } catch (Exception e) { e.printStackTrace(); }
                }               
            });
        }
    }

}
