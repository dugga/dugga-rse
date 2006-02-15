package com.softlanding.rse.extensions.compare;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.iseries.core.api.ISeriesMember;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class CompareDialog extends Dialog {
    private ISeriesMember member;
    private ISeriesConnection[] iSeriesConnections;
    private Combo compareConnectionCombo;
    private Text compareLibraryText;
    private Text compareFileText;
    private Text compareMemberText;
    private Button editButton;
    private Button browseButton;
    private ISeriesConnection compareConnection;
    private String compareLibrary;
    private String compareFile;
    private String compareMember;
    private boolean openForEdit;
    private Button okButton;
    private IDialogSettings settings;

    public CompareDialog(Shell parentShell, ISeriesMember member) {
        super(parentShell);
        this.member = member;
    }
    
    public Control createDialogArea(Composite parent) {
        getRseConnections();
        
		Composite rtnGroup = (Composite)super.createDialogArea(parent);
		parent.getShell().setText(ExtensionsPlugin.getResourceString("CompareAction.0")); //$NON-NLS-1$
		
		GridLayout rtnLayout = new GridLayout();
		rtnLayout.numColumns = 1;
		rtnGroup.setLayout(rtnLayout);
		rtnGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		Group referenceGroup = new Group(rtnGroup, SWT.NONE);
		referenceGroup.setText(ExtensionsPlugin.getResourceString("CompareDialog.1")); //$NON-NLS-1$
		GridLayout referenceLayout = new GridLayout();
		referenceLayout.numColumns = 2;
		referenceGroup.setLayout(referenceLayout);
		referenceGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 		
	
		Label referenceConnectionLabel = new Label(referenceGroup, SWT.NONE);
		referenceConnectionLabel.setText(ExtensionsPlugin.getResourceString("CompareDialog.2")); //$NON-NLS-1$
		
		Text referenceConnectionText = new Text(referenceGroup, SWT.BORDER);
		referenceConnectionText.setEditable(false);
		GridData gd = new GridData();
		gd.widthHint = 200;
		referenceConnectionText.setLayoutData(gd);
		referenceConnectionText.setText(member.getISeriesConnection().getConnectionName());
		
		Label referenceLibraryLabel = new Label(referenceGroup, SWT.NONE);
		referenceLibraryLabel.setText(ExtensionsPlugin.getResourceString("CompareDialog.3")); //$NON-NLS-1$
		Text referenceLibraryText = new Text(referenceGroup, SWT.BORDER);
		referenceLibraryText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		referenceLibraryText.setLayoutData(gd);
		referenceLibraryText.setText(member.getLibraryName());
		
		Label referenceFileLabel = new Label(referenceGroup, SWT.NONE);
		referenceFileLabel.setText(ExtensionsPlugin.getResourceString("CompareDialog.4")); //$NON-NLS-1$
		Text referenceFileText = new Text(referenceGroup, SWT.BORDER);
		referenceFileText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		referenceFileText.setLayoutData(gd);
		referenceFileText.setText(member.getFile());
		
		Label referenceMemberLabel = new Label(referenceGroup, SWT.NONE);
		referenceMemberLabel.setText(ExtensionsPlugin.getResourceString("CompareDialog.5")); //$NON-NLS-1$
		Text referenceMemberText = new Text(referenceGroup, SWT.BORDER);
		referenceMemberText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		referenceMemberText.setLayoutData(gd);
		referenceMemberText.setText(member.getName());
		
		Group compareGroup = new Group(rtnGroup, SWT.NONE);
		compareGroup.setText(ExtensionsPlugin.getResourceString("CompareDialog.6")); //$NON-NLS-1$
		GridLayout compareLayout = new GridLayout();
		compareLayout.numColumns = 2;
		compareGroup.setLayout(compareLayout);
		compareGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 
		
		Label compareConnectionLabel = new Label(compareGroup, SWT.NONE);
		compareConnectionLabel.setText(ExtensionsPlugin.getResourceString("CompareDialog.7")); //$NON-NLS-1$
		compareConnectionCombo = new Combo(compareGroup, SWT.READ_ONLY);
		gd = new GridData();
		gd.widthHint = 200;
		compareConnectionCombo.setLayoutData(gd);
		
		Label compareLibraryLabel = new Label(compareGroup, SWT.NONE);
		compareLibraryLabel.setText(ExtensionsPlugin.getResourceString("CompareDialog.8")); //$NON-NLS-1$
		compareLibraryText = new Text(compareGroup, SWT.BORDER);
		compareLibraryText.setTextLimit(10);
		gd = new GridData();
		gd.widthHint = 75;
		compareLibraryText.setLayoutData(gd);
		
		Label compareFileLabel = new Label(compareGroup, SWT.NONE);
		compareFileLabel.setText(ExtensionsPlugin.getResourceString("CompareDialog.9")); //$NON-NLS-1$
		compareFileText = new Text(compareGroup, SWT.BORDER);
		compareFileText.setTextLimit(10);
		gd = new GridData();
		gd.widthHint = 75;
		compareFileText.setLayoutData(gd);
		compareFileText.setText(member.getFile());
		
		Label compareMemberLabel = new Label(compareGroup, SWT.NONE);
		compareMemberLabel.setText(ExtensionsPlugin.getResourceString("CompareDialog.10")); //$NON-NLS-1$
		compareMemberText = new Text(compareGroup, SWT.BORDER);
		compareMemberText.setTextLimit(10);
		gd = new GridData();
		gd.widthHint = 75;
		compareMemberText.setLayoutData(gd);
		compareMemberText.setText(member.getName());
		
		for (int i = 0; i < iSeriesConnections.length; i++) {
		    compareConnectionCombo.add(iSeriesConnections[i].getConnectionName());
		}
		compareConnectionCombo.select(compareConnectionCombo.indexOf(member.getISeriesConnection().getConnectionName()));
		
		Composite editGroup = new Composite(rtnGroup, SWT.NONE);
		GridLayout editLayout = new GridLayout();
		editLayout.numColumns = 2;
		editGroup.setLayout(editLayout);
		editGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 		
		
		editButton = new Button(editGroup, SWT.RADIO);
		editButton.setText(ExtensionsPlugin.getResourceString("CompareDialog.11")); //$NON-NLS-1$
		browseButton = new Button(editGroup, SWT.RADIO);
		browseButton.setText(ExtensionsPlugin.getResourceString("CompareDialog.12")); //$NON-NLS-1$
		
		settings = ExtensionsPlugin.getDefault().getDialogSettings();
		String mode = settings.get("CompareDialog.mode"); //$NON-NLS-1$
		if (mode == null) editButton.setSelection(true);
		else {
		    if (mode.equals("b")) browseButton.setSelection(true); //$NON-NLS-1$
		    else editButton.setSelection(true);
		}
		
		ModifyListener modifyListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                okButton.setEnabled(canFinish());
            }		    
		};
		
		compareLibraryText.addModifyListener(modifyListener);
		compareFileText.addModifyListener(modifyListener);
		compareMemberText.addModifyListener(modifyListener);
		
		compareLibraryText.setFocus();
		
		return rtnGroup;
    }
    
    protected void okPressed() {
        if (editButton.getSelection() && ExtensionsPlugin.getDefault().getPreferenceStore().getBoolean(ExtensionsPlugin.PREFERENCE_COMPARE_MERGE_WARNING)) {
            WarningDialog dialog = new WarningDialog(getShell(), ExtensionsPlugin.getResourceString("CompareDialog.15"), null, ExtensionsPlugin.getResourceString("CompareDialog.16"), MessageDialog.WARNING, new String[] { ExtensionsPlugin.getResourceString("CompareDialog.17"), ExtensionsPlugin.getResourceString("CompareDialog.18")}, 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            if (dialog.open() == WarningDialog.CANCEL) {
                super.cancelPressed();
                return;
            }            
        }
        if (editButton.getSelection()) settings.put("CompareDialog.mode", "e"); //$NON-NLS-1$ //$NON-NLS-2$
        else settings.put("CompareDialog.mode", "b"); //$NON-NLS-1$ //$NON-NLS-2$
        openForEdit = editButton.getSelection();
        compareConnection = iSeriesConnections[compareConnectionCombo.getSelectionIndex()];
        compareLibrary = compareLibraryText.getText().trim().toUpperCase();
        compareFile = compareFileText.getText().trim().toUpperCase();
        compareMember = compareMemberText.getText().trim().toUpperCase();
        super.okPressed();
    }
    
	public Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		Button button = super.createButton(parent, id, label, defaultButton);
		if (id == OK) {
			okButton = button;
			okButton.setEnabled(false);
		}
		return button;
	}
	
    private void getRseConnections() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            public void run() {
                iSeriesConnections = ISeriesConnection.getConnections();
            }           
        });
    }
    
    private boolean canFinish() {
        if (compareLibraryText.getText().trim().length() == 0 ||
                compareFileText.getText().trim().length() == 0 ||
                compareMemberText.getText().trim().length() == 0) return false;
        if (compareLibraryText.getText().trim().equalsIgnoreCase(member.getLibraryName()) &&
                compareFileText.getText().trim().equalsIgnoreCase(member.getFile()) &&
                compareMemberText.getText().trim().equalsIgnoreCase(member.getName())) return false;
        return true;
    }
    
    public ISeriesMember getReferenceMember() {
        return member;
    }

    public ISeriesConnection getCompareConnection() {
        return compareConnection;
    }
    public String getCompareFile() {
        return compareFile;
    }
    public String getCompareLibrary() {
        return compareLibrary;
    }
    public String getCompareMember() {
        return compareMember;
    }
    public boolean isOpenForEdit() {
        return openForEdit;
    }
}
