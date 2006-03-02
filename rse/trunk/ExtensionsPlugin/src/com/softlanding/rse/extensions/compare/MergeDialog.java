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

import org.eclipse.jface.dialogs.Dialog;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.iseries.core.api.ISeriesMember;
import com.ibm.etools.iseries.core.ui.widgets.ISeriesConnectionCombo;
import com.ibm.etools.iseries.core.ui.widgets.ISeriesMemberPrompt;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class MergeDialog extends Dialog {
    private ISeriesMember member;
    private ISeriesConnectionCombo maintenanceConnectionCombo;
    private ISeriesMemberPrompt maintenanceMemberPrompt;
    private ISeriesConnectionCombo rootConnectionCombo;
    private ISeriesMemberPrompt rootMemberPrompt;
    private Button okButton;
    private ISeriesConnection maintenanceConnection;
    private ISeriesConnection rootConnection;
    private String maintenanceLibrary;
    private String maintenanceFile;
    private String maintenanceMember;
    private String rootLibrary;
    private String rootFile;
    private String rootMember;

    public MergeDialog(Shell parentShell, ISeriesMember member) {
        super(parentShell);
        this.member = member;
    }
    
    public Control createDialogArea(Composite parent) {
		Composite rtnGroup = (Composite)super.createDialogArea(parent);
		parent.getShell().setText(ExtensionsPlugin.getResourceString("MergeDialog.0")); //$NON-NLS-1$
		
		GridLayout rtnLayout = new GridLayout();
		rtnLayout.numColumns = 1;
		rtnGroup.setLayout(rtnLayout);
		rtnGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		Group targetGroup = new Group(rtnGroup, SWT.NONE);
		targetGroup.setText(ExtensionsPlugin.getResourceString("MergeDialog.1")); //$NON-NLS-1$
		GridLayout targetLayout = new GridLayout();
		targetLayout.numColumns = 2;
		targetGroup.setLayout(targetLayout);
		targetGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 
		
		Label targetConnectionLabel = new Label(targetGroup, SWT.NONE);
		targetConnectionLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.2")); //$NON-NLS-1$
		
		Text targetConnectionText = new Text(targetGroup, SWT.BORDER);
		targetConnectionText.setEditable(false);
		GridData gd = new GridData();
		gd.widthHint = 200;
		targetConnectionText.setLayoutData(gd);
		targetConnectionText.setText(member.getISeriesConnection().getConnectionName());
		
		Label targetLibraryLabel = new Label(targetGroup, SWT.NONE);
		targetLibraryLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.3")); //$NON-NLS-1$
		Text targetLibraryText = new Text(targetGroup, SWT.BORDER);
		targetLibraryText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		targetLibraryText.setLayoutData(gd);
		targetLibraryText.setText(member.getLibraryName());
		
		Label targetFileLabel = new Label(targetGroup, SWT.NONE);
		targetFileLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.4")); //$NON-NLS-1$
		Text targetFileText = new Text(targetGroup, SWT.BORDER);
		targetFileText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		targetFileText.setLayoutData(gd);
		targetFileText.setText(member.getFile());
		
		Label targetMemberLabel = new Label(targetGroup, SWT.NONE);
		targetMemberLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.5")); //$NON-NLS-1$
		Text targetMemberText = new Text(targetGroup, SWT.BORDER);
		targetMemberText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		targetMemberText.setLayoutData(gd);
		targetMemberText.setText(member.getName());
		
		Group maintenanceGroup = new Group(rtnGroup, SWT.NONE);
		maintenanceGroup.setText(ExtensionsPlugin.getResourceString("MergeDialog.6")); //$NON-NLS-1$
		GridLayout maintenanceLayout = new GridLayout();
		maintenanceLayout.numColumns = 1;
		maintenanceGroup.setLayout(maintenanceLayout);
		maintenanceGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 

		maintenanceConnectionCombo = new ISeriesConnectionCombo(maintenanceGroup, member.getISeriesConnection(), true);
		gd = new GridData();
		maintenanceConnectionCombo.setLayoutData(gd);
		
		gd = new GridData();
		gd.widthHint = 200;
		maintenanceConnectionCombo.getCombo().setLayoutData(gd);

		maintenanceMemberPrompt = new ISeriesMemberPrompt(maintenanceGroup, SWT.NONE, false, true, ISeriesMemberPrompt.FILETYPE_SRC);
		maintenanceMemberPrompt.setFileName(member.getFile());
		maintenanceMemberPrompt.setLibraryName(member.getLibraryName());
		maintenanceMemberPrompt.setMemberName(member.getName());
		maintenanceMemberPrompt.setSystemConnection(maintenanceConnectionCombo.getSystemConnection());

		maintenanceConnectionCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                okButton.setEnabled(canFinish());
                maintenanceMemberPrompt.setSystemConnection(maintenanceConnectionCombo.getSystemConnection());
            }
		});
		
		Group rootGroup = new Group(rtnGroup, SWT.NONE);
		rootGroup.setText(ExtensionsPlugin.getResourceString("MergeDialog.11")); //$NON-NLS-1$
		GridLayout rootLayout = new GridLayout();
		rootLayout.numColumns = 1;
		rootGroup.setLayout(rootLayout);
		rootGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 		

		rootConnectionCombo = new ISeriesConnectionCombo(rootGroup, member.getISeriesConnection(), true);
		gd = new GridData();
		rootConnectionCombo.setLayoutData(gd);
		
		gd = new GridData();
		gd.widthHint = 200;
		rootConnectionCombo.getCombo().setLayoutData(gd);

		rootMemberPrompt = new ISeriesMemberPrompt(rootGroup, SWT.NONE, false, true, ISeriesMemberPrompt.FILETYPE_SRC);
		rootMemberPrompt.setFileName(member.getFile());
		rootMemberPrompt.setLibraryName(member.getLibraryName());
		rootMemberPrompt.setMemberName(member.getName());
		rootMemberPrompt.setSystemConnection(rootConnectionCombo.getSystemConnection());

		maintenanceConnectionCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                okButton.setEnabled(canFinish());
                maintenanceMemberPrompt.setSystemConnection(maintenanceConnectionCombo.getSystemConnection());
            }
		});
		
		rootConnectionCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                okButton.setEnabled(canFinish());
                rootMemberPrompt.setSystemConnection(rootConnectionCombo.getSystemConnection());
            }
		});
		
		ModifyListener modifyListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                okButton.setEnabled(canFinish());
            }		    
		};		

		maintenanceMemberPrompt.getMemberCombo().addModifyListener(modifyListener);
		maintenanceMemberPrompt.getFileCombo().addModifyListener(modifyListener);
		maintenanceMemberPrompt.getLibraryCombo().addModifyListener(modifyListener);
		rootMemberPrompt.getMemberCombo().addModifyListener(modifyListener);
		rootMemberPrompt.getFileCombo().addModifyListener(modifyListener);
		rootMemberPrompt.getLibraryCombo().addModifyListener(modifyListener);

		maintenanceMemberPrompt.getLibraryCombo().setFocus();
		
		return rtnGroup;
    }
    
    protected void okPressed() {
        if (ExtensionsPlugin.getDefault().getPreferenceStore().getBoolean(ExtensionsPlugin.PREFERENCE_COMPARE_MERGE_WARNING)) {
            WarningDialog dialog = new WarningDialog(getShell(), ExtensionsPlugin.getResourceString("MergeDialog.16"), null, ExtensionsPlugin.getResourceString("MergeDialog.17"), MessageDialog.WARNING, new String[] { ExtensionsPlugin.getResourceString("MergeDialog.18"), ExtensionsPlugin.getResourceString("MergeDialog.19")}, 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            if (dialog.open() == WarningDialog.CANCEL) {
                super.cancelPressed();
                return;
            }
        }
        maintenanceConnection = ISeriesConnection.getConnection(maintenanceConnectionCombo.getSystemConnection());        
        rootConnection = ISeriesConnection.getConnection(rootConnectionCombo.getSystemConnection());        
        maintenanceLibrary = maintenanceMemberPrompt.getLibraryName();
        maintenanceFile = maintenanceMemberPrompt.getFileName();
        maintenanceMember = maintenanceMemberPrompt.getMemberName();        
        rootLibrary = rootMemberPrompt.getLibraryName();
        rootFile = rootMemberPrompt.getFileName();
        rootMember = rootMemberPrompt.getMemberName();
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
        if (maintenanceMemberPrompt.getMemberName() == null || maintenanceMemberPrompt.getMemberName().trim().length() == 0 ||
                maintenanceMemberPrompt.getFileName() == null || maintenanceMemberPrompt.getFileName().trim().length() == 0 ||
                maintenanceMemberPrompt.getLibraryName() == null || maintenanceMemberPrompt.getLibraryName().trim().length() == 0 ||
                rootMemberPrompt.getMemberName() == null || rootMemberPrompt.getMemberName().trim().length() == 0 ||
                rootMemberPrompt.getFileName() == null || rootMemberPrompt.getFileName().trim().length() == 0 ||
                rootMemberPrompt.getLibraryName() == null || rootMemberPrompt.getLibraryName().trim().length() == 0) return false;
        if (maintenanceMemberPrompt.getMemberName().equalsIgnoreCase(rootMemberPrompt.getMemberName()) &&
                maintenanceMemberPrompt.getFileName().equalsIgnoreCase(rootMemberPrompt.getFileName()) &&
                maintenanceMemberPrompt.getLibraryName().equalsIgnoreCase(rootMemberPrompt.getLibraryName()) &&
                maintenanceConnectionCombo.getSystemConnection().getHostName().equals(rootConnectionCombo.getSystemConnection().getHostName())) return false;
        if (maintenanceMemberPrompt.getLibraryName().equalsIgnoreCase(member.getLibraryName()) &&
                maintenanceMemberPrompt.getFileName().equalsIgnoreCase(member.getFile()) &&
                maintenanceMemberPrompt.getMemberName().equalsIgnoreCase(member.getName()) &&
                maintenanceConnectionCombo.getSystemConnection().getHostName().equals(member.getISeriesConnection().getHostName())) return false;        
        if (rootMemberPrompt.getLibraryName().equalsIgnoreCase(member.getLibraryName()) &&
                rootMemberPrompt.getFileName().equalsIgnoreCase(member.getFile()) &&
                rootMemberPrompt.getMemberName().equalsIgnoreCase(member.getName()) &&
                rootConnectionCombo.getSystemConnection().getHostName().equals(member.getISeriesConnection().getHostName())) return false;                
        return true;
    }

    public ISeriesConnection getMaintenanceConnection() {
        return maintenanceConnection;
    }
    public String getMaintenanceFile() {
        return maintenanceFile;
    }
    public String getMaintenanceLibrary() {
        return maintenanceLibrary;
    }
    public String getMaintenanceMember() {
        return maintenanceMember;
    }
    public ISeriesConnection getRootConnection() {
        return rootConnection;
    }
    public String getRootFile() {
        return rootFile;
    }
    public String getRootLibrary() {
        return rootLibrary;
    }
    public String getRootMember() {
        return rootMember;
    }
    public ISeriesMember getTargetMember() {
        return member;
    }
}
