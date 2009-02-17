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

public class MergeDialog extends Dialog {
    private IQSYSMember member;
    private IBMiConnectionCombo maintenanceConnectionCombo;
    private QSYSMemberPrompt maintenanceMemberPrompt;
    private IBMiConnectionCombo rootConnectionCombo;
    private QSYSMemberPrompt rootMemberPrompt;
    private Button okButton;
    private IBMiConnection maintenanceConnection;
    private IBMiConnection rootConnection;
    private IBMiConnection memberConnection;
    private String maintenanceLibrary;
    private String maintenanceFile;
    private String maintenanceMember;
    private String rootLibrary;
    private String rootFile;
    private String rootMember;

    public MergeDialog(Shell parentShell, IQSYSMember member) {
        super(parentShell);
        this.member = member;
        memberConnection = IBMiConnection.getConnection(((IRemoteObjectContextProvider)member).getRemoteObjectContext().getObjectSubsystem().getHost());
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
		targetConnectionText.setText(memberConnection.getConnectionName());
		
		Label targetLibraryLabel = new Label(targetGroup, SWT.NONE);
		targetLibraryLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.3")); //$NON-NLS-1$
		Text targetLibraryText = new Text(targetGroup, SWT.BORDER);
		targetLibraryText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		targetLibraryText.setLayoutData(gd);
		targetLibraryText.setText(member.getLibrary());
		
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

		maintenanceConnectionCombo = new IBMiConnectionCombo(maintenanceGroup, memberConnection, true);
		gd = new GridData();
		maintenanceConnectionCombo.setLayoutData(gd);
		
		gd = new GridData();
		gd.widthHint = 200;
		maintenanceConnectionCombo.getCombo().setLayoutData(gd);

		maintenanceMemberPrompt = new QSYSMemberPrompt(maintenanceGroup, SWT.NONE, false, true, QSYSMemberPrompt.FILETYPE_SRC);
		maintenanceMemberPrompt.setFileName(member.getFile());
		maintenanceMemberPrompt.setLibraryName(member.getLibrary());
		maintenanceMemberPrompt.setMemberName(member.getName());
		maintenanceMemberPrompt.setSystemConnection(maintenanceConnectionCombo.getHost());

		maintenanceConnectionCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                okButton.setEnabled(canFinish());
                maintenanceMemberPrompt.setSystemConnection(maintenanceConnectionCombo.getHost());
            }
		});
		
		Group rootGroup = new Group(rtnGroup, SWT.NONE);
		rootGroup.setText(ExtensionsPlugin.getResourceString("MergeDialog.11")); //$NON-NLS-1$
		GridLayout rootLayout = new GridLayout();
		rootLayout.numColumns = 1;
		rootGroup.setLayout(rootLayout);
		rootGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 		

		rootConnectionCombo = new IBMiConnectionCombo(rootGroup, memberConnection, true);
		gd = new GridData();
		rootConnectionCombo.setLayoutData(gd);
		
		gd = new GridData();
		gd.widthHint = 200;
		rootConnectionCombo.getCombo().setLayoutData(gd);

		rootMemberPrompt = new QSYSMemberPrompt(rootGroup, SWT.NONE, false, true, QSYSMemberPrompt.FILETYPE_SRC);
		rootMemberPrompt.setFileName(member.getFile());
		rootMemberPrompt.setLibraryName(member.getLibrary());
		rootMemberPrompt.setMemberName(member.getName());
		rootMemberPrompt.setSystemConnection(rootConnectionCombo.getHost());

		maintenanceConnectionCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                okButton.setEnabled(canFinish());
                maintenanceMemberPrompt.setSystemConnection(maintenanceConnectionCombo.getHost());
            }
		});
		
		rootConnectionCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                okButton.setEnabled(canFinish());
                rootMemberPrompt.setSystemConnection(rootConnectionCombo.getHost());
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
                new Label(warningGroup, SWT.WRAP).setText(ExtensionsPlugin.getResourceString("MergeDialog.20")); //$NON-NLS-1$                                 
        } 

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
        maintenanceConnection = IBMiConnection.getConnection(maintenanceConnectionCombo.getHost());        
        rootConnection = IBMiConnection.getConnection(rootConnectionCombo.getHost());        
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
                maintenanceConnectionCombo.getHost().getHostName().equals(rootConnectionCombo.getHost().getHostName())) return false;
        if (maintenanceMemberPrompt.getLibraryName().equalsIgnoreCase(member.getLibrary()) &&
                maintenanceMemberPrompt.getFileName().equalsIgnoreCase(member.getFile()) &&
                maintenanceMemberPrompt.getMemberName().equalsIgnoreCase(member.getName()) &&
                maintenanceConnectionCombo.getHost().getHostName().equals(memberConnection.getHostName())) return false;        
        if (rootMemberPrompt.getLibraryName().equalsIgnoreCase(member.getLibrary()) &&
                rootMemberPrompt.getFileName().equalsIgnoreCase(member.getFile()) &&
                rootMemberPrompt.getMemberName().equalsIgnoreCase(member.getName()) &&
                rootConnectionCombo.getHost().getHostName().equals(memberConnection.getHostName())) return false;                
        return true;
    }

    public IBMiConnection getMaintenanceConnection() {
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
    public IBMiConnection getRootConnection() {
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
    public IQSYSMember getTargetMember() {
        return member;
    }
}
