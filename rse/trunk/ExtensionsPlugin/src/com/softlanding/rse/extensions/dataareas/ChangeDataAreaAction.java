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
package com.softlanding.rse.extensions.dataareas;

import com.ibm.etools.iseries.core.api.ISeriesObject;
import com.ibm.etools.iseries.core.ui.actions.isv.ISeriesAbstractQSYSPopupMenuExtensionAction;

public class ChangeDataAreaAction extends
        ISeriesAbstractQSYSPopupMenuExtensionAction {

    public ChangeDataAreaAction() {
        super();
    }

    public void run() {
        ISeriesObject[] objects = getSelectedObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i].getType().equals("*DTAARA")) { //$NON-NLS-1$
                ChangeDataAreaDialog dialog = new ChangeDataAreaDialog(getShell(), objects[i]);
                if (dialog.open() == ChangeDataAreaDialog.CANCEL) break;
            }
        }
    }

}
