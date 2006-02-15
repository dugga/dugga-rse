package com.softlanding.rse.extensions.compare;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.softlanding.rse.extensions.ExtensionsPlugin;

public class WarningDialog extends MessageDialog {
    private Button doNotShowAgainButton;

    public WarningDialog(Shell parentShell, String dialogTitle,
            Image dialogTitleImage, String dialogMessage, int dialogImageType,
            String[] dialogButtonLabels, int defaultIndex) {
        super(parentShell, dialogTitle, dialogTitleImage, dialogMessage,
                dialogImageType, dialogButtonLabels, defaultIndex);
    }

    protected Control createDialogArea(Composite parent) {
        Control rtnControl = super.createDialogArea(parent);
        
        doNotShowAgainButton = new Button((Composite)rtnControl, SWT.CHECK);
        doNotShowAgainButton.setText(ExtensionsPlugin.getResourceString("WarningDialog.0")); //$NON-NLS-1$
        
        return rtnControl;
    }
    
    
    protected void buttonPressed(int buttonId) {
        if (doNotShowAgainButton.getSelection()) 
            ExtensionsPlugin.getDefault().getPreferenceStore().setValue(ExtensionsPlugin.PREFERENCE_COMPARE_MERGE_WARNING, false);
        super.buttonPressed(buttonId);
    }

}
