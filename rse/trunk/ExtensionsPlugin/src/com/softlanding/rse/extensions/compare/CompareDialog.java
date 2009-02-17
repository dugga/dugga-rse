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

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PluginVersionIdentifier;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.etools.iseries.rse.ui.widgets.IBMiConnectionCombo;
import com.ibm.etools.iseries.rse.ui.widgets.QSYSMemberPrompt;
import com.ibm.etools.iseries.services.qsys.api.IQSYSMember;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;
import com.ibm.etools.iseries.subsystems.qsys.objects.IRemoteObjectContextProvider;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class CompareDialog extends Dialog {
    private IQSYSMember member;
    private IBMiConnectionCombo compareConnectionCombo;
    private QSYSMemberPrompt compareMemberPrompt;
    private Button editButton;
    private Button browseButton;
    private IBMiConnection compareConnection;
    private String compareLibrary;
    private String compareFile;
    private String compareMember;
    private boolean openForEdit;
    private Button okButton;
    private IDialogSettings settings;
    private IBMiConnection connection;

    public CompareDialog(Shell parentShell, IQSYSMember member) {
        super(parentShell);
        this.member = member;
        connection = IBMiConnection.getConnection(((IRemoteObjectContextProvider)member).getRemoteObjectContext().getObjectSubsystem().getHost());
    }
    
    public Control createDialogArea(Composite parent) {       
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
		referenceConnectionText.setText(connection.getConnectionName());
		
		Label referenceLibraryLabel = new Label(referenceGroup, SWT.NONE);
		referenceLibraryLabel.setText(ExtensionsPlugin.getResourceString("CompareDialog.3")); //$NON-NLS-1$
		Text referenceLibraryText = new Text(referenceGroup, SWT.BORDER);
		referenceLibraryText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		referenceLibraryText.setLayoutData(gd);
		referenceLibraryText.setText(member.getLibrary());
		
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
		compareLayout.numColumns = 1;
		compareGroup.setLayout(compareLayout);
		compareGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 

		
		compareConnectionCombo = new IBMiConnectionCombo(compareGroup, connection, true);
		gd = new GridData();
		compareConnectionCombo.setLayoutData(gd);
		
		gd = new GridData();
		gd.widthHint = 200;
		compareConnectionCombo.getCombo().setLayoutData(gd);
		
		compareMemberPrompt = new QSYSMemberPrompt(compareGroup, SWT.NONE, false, true, QSYSMemberPrompt.FILETYPE_SRC);
		compareMemberPrompt.setFileName(member.getFile());
		compareMemberPrompt.setLibraryName(member.getLibrary());
		compareMemberPrompt.setMemberName(member.getName());
		compareMemberPrompt.setSystemConnection(connection.getHost());

		compareConnectionCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                okButton.setEnabled(canFinish());
                compareMemberPrompt.setSystemConnection(connection.getHost());
            }
		});
		
		Composite editGroup = new Composite(rtnGroup, SWT.NONE);
		GridLayout editLayout = new GridLayout();
		editLayout.numColumns = 2;
		editGroup.setLayout(editLayout);
		editGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 		
		
		editButton = new Button(editGroup, SWT.RADIO);
		editButton.setText(ExtensionsPlugin.getResourceString("CompareDialog.11")); //$NON-NLS-1$
		browseButton = new Button(editGroup, SWT.RADIO);
		browseButton.setText(ExtensionsPlugin.getResourceString("CompareDialog.12")); //$NON-NLS-1$
	
        String rseVersion = (String) Platform.getBundle("com.ibm.etools.iseries.rse.ui").getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION); 
        PluginVersionIdentifier current = new PluginVersionIdentifier(rseVersion); 
        PluginVersionIdentifier required = new PluginVersionIdentifier(6, 0, 1, "2"); 
                
        if (required.isGreaterThan(current)) 
        { 
                Composite warningGroup = new Composite(rtnGroup, SWT.NONE); 
                GridLayout warningLayout = new GridLayout(); 
                warningLayout.numColumns = 2; 
                warningGroup.setLayout(warningLayout); 
                warningGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));                 
                                
                new Label(warningGroup, SWT.NONE).setImage(Display.getDefault().getSystemImage(SWT.ICON_WARNING)); 
                new Label(warningGroup, SWT.WRAP).setText(ExtensionsPlugin.getResourceString("CompareDialog.19")); //$NON-NLS-1$                                 
        } 
		
		
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

		compareMemberPrompt.getMemberCombo().addModifyListener(modifyListener);
		compareMemberPrompt.getFileCombo().addModifyListener(modifyListener);
		compareMemberPrompt.getLibraryCombo().addModifyListener(modifyListener);
		compareMemberPrompt.getLibraryCombo().setFocus();
		
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
        compareConnection = connection;
        compareLibrary = compareMemberPrompt.getLibraryName();
        compareFile = compareMemberPrompt.getFileName();
        compareMember = compareMemberPrompt.getMemberName();
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

    private boolean canFinish() {
        if (compareMemberPrompt.getMemberName() == null || compareMemberPrompt.getMemberName().trim().length() == 0) return false;
         if (compareMemberPrompt.getMemberName().equalsIgnoreCase(member.getName()) &&
                 compareMemberPrompt.getFileName().equalsIgnoreCase(member.getFile()) &&
                 compareMemberPrompt.getLibraryName().equalsIgnoreCase(member.getLibrary()) &&
                 compareConnectionCombo.getISeriesConnection().getHostName().equalsIgnoreCase(connection.getHostName()))
             	 return false;
        return true;
    }
    
    public IQSYSMember getReferenceMember() {
        return member;
    }

    public IBMiConnection getCompareConnection() {
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
