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
package com.softlanding.rse.extensions.dtaqs;

import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

import com.ibm.etools.iseries.rse.ui.actions.popupmenu.ISeriesAbstractQSYSPopupMenuAction;
import com.ibm.etools.iseries.services.qsys.api.IQSYSObject;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class DisplayDataQueueAction extends
        ISeriesAbstractQSYSPopupMenuAction {

    public DisplayDataQueueAction() {
        super();
    }

    public void run() {
        final IQSYSObject[] objects = getSelectedObjects();
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < objects.length; i++) {
                        if (objects[i].getType().equals("*DTAQ")) { //$NON-NLS-1$
                            DisplayDataQueueDialog dialog = new DisplayDataQueueDialog(getShell(), objects[i]);
                            if (dialog.open() == DisplayDataQueueDialog.CANCEL) break;
                        }
                    }                    
                } catch (Exception e) {
                    ExtensionsPlugin.log(e);
                }
            }           
        });
    }

}
