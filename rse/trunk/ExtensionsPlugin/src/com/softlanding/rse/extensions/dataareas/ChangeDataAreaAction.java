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
            if (objects[i].getType().equals("*DTAARA")) {
                ChangeDataAreaDialog dialog = new ChangeDataAreaDialog(getShell(), objects[i]);
                if (dialog.open() == ChangeDataAreaDialog.CANCEL) break;
            }
        }
    }

}
