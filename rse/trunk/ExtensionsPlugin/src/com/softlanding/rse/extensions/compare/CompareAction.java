package com.softlanding.rse.extensions.compare;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;

import com.ibm.etools.iseries.core.api.ISeriesMember;
import com.ibm.etools.iseries.core.ui.actions.isv.ISeriesAbstractQSYSPopupMenuExtensionAction;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class CompareAction extends ISeriesAbstractQSYSPopupMenuExtensionAction {

    public CompareAction() {
        super();
    }

    public void run() {
        ISeriesMember[] members = getSelectedMembers();
        for (int i = 0; i < members.length; i++) {
            final CompareDialog dialog = new CompareDialog(getShell(), members[i]);
            if (dialog.open() == CompareDialog.CANCEL) break;
            BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                public void run() {
                    try {
                        ISeriesMember compareMember = dialog.getCompareConnection().getISeriesMember(getShell(), dialog.getCompareLibrary(), dialog.getCompareFile(), dialog.getCompareMember());
                        if (compareMember == null || !compareMember.exists()) {
                			MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("CompareAction.0"), //$NON-NLS-1$
                					ExtensionsPlugin.getString("MergeAction.1",                           //$NON-NLS-1$
                							new Object[] {dialog.getCompareMember(), dialog.getCompareFile(), dialog.getCompareLibrary()}));  
                            return;
                        } 
                        CompareConfiguration cc = new CompareConfiguration();
                        cc.setLeftEditable(dialog.isOpenForEdit());
                        cc.setRightEditable(false);
                        MemberCompareInput fInput = new MemberCompareInput(cc, dialog.getReferenceMember(), compareMember);
                		fInput.initializeCompareConfiguration();
                		CompareUI.openCompareEditor(fInput);
                		fInput.cleanup();
                		fInput= null;                    
                    } catch (Exception e) { e.printStackTrace(); }
                }
            });
        }
    }

}
