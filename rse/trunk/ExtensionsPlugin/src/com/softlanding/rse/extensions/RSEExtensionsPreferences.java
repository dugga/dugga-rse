/*******************************************************************************
 * Copyright (c) 2005-2009 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *******************************************************************************/
package com.softlanding.rse.extensions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.softlanding.rse.extensions.messages.AddQueuedMessageSubSystem;
import com.softlanding.rse.extensions.subsystems.spooledfiles.AddSpooledFilesSubSystem;


public class RSEExtensionsPreferences extends PreferencePage implements IWorkbenchPreferencePage {
    private Button warningButton;
    private Button enableSpooledFileSubSystemButton;
    private Button enableQueuedMessageSubSystemButton;

    public RSEExtensionsPreferences() {
        super();
    }

    public RSEExtensionsPreferences(String title) {
        super(title);
    }

    public RSEExtensionsPreferences(String title, ImageDescriptor image) {
        super(title, image);
    }

    protected Control createContents(Composite parent) {
		parent.setLayout(new GridLayout());
		parent.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		Composite rtnGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		rtnGroup.setLayout(layout);
		rtnGroup.setLayoutData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		
		Group prefGroup = new Group(rtnGroup, SWT.NONE);
		GridLayout prefLayout = new GridLayout();
		prefLayout.numColumns = 1;
		prefGroup.setLayout(prefLayout);
		prefGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		warningButton = new Button(prefGroup, SWT.CHECK);
		warningButton.setText(ExtensionsPlugin.getResourceString("Preferences.1")); //$NON-NLS-1$
		warningButton.setSelection(getWarningPreference());
		
		Group subSystemGroup = new Group(rtnGroup, SWT.NONE);
		subSystemGroup.setText("RSE Extensions SubSystems");
		GridLayout subSystemLayout = new GridLayout();
		subSystemGroup.setLayout(subSystemLayout);
		subSystemGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		Composite textGroup = new Composite(subSystemGroup, SWT.NONE);
		GridLayout textLayout = new GridLayout();
		textLayout.numColumns = 1;
		textGroup.setLayout(textLayout);
		Label textLabel = new Label(subSystemGroup, SWT.NONE);
		textLabel.setText("Add RSE Extenstions SubSystems to existing connections by clicking the button");
		
		Composite buttonGroup = new Composite(subSystemGroup, SWT.NONE);
		GridLayout buttonLayout = new GridLayout();
		buttonLayout.numColumns = 2;
		buttonGroup.setLayout(buttonLayout);
		buttonGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		Label spooledFileSubSystemLabel = new Label(buttonGroup, SWT.NONE );
		spooledFileSubSystemLabel.setText("Spooled File SubSystem");
		enableSpooledFileSubSystemButton = new Button(buttonGroup, SWT.PUSH);
		enableSpooledFileSubSystemButton.setText("Add");
		enableSpooledFileSubSystemButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {
				String list;
				AddSpooledFilesSubSystem add = new AddSpooledFilesSubSystem();
				list = add.AddSubSystem();
				if (list != "")
					MessageDialog.openInformation(RSEExtensionsPreferences.this.getShell(), "SubSystem Added", "Spooled File SubSystem Added to: \n" + list);
				else
					MessageDialog.openInformation(RSEExtensionsPreferences.this.getShell(), "SubSystem Exists", "All IBM i systems have the Spooled File SubSystem defined.");
			}
		});	
		
		Label queuedMessageSubSystemLabel = new Label(buttonGroup, SWT.NONE);
		queuedMessageSubSystemLabel.setText("Queued Message SubSystem");
		enableQueuedMessageSubSystemButton = new Button(buttonGroup, SWT.PUSH);
		enableQueuedMessageSubSystemButton.setText("Add");
		enableQueuedMessageSubSystemButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {
				String list;
				AddQueuedMessageSubSystem add = new AddQueuedMessageSubSystem();
				list = add.AddSubSystem();
				if (list != "")
					MessageDialog.openInformation(RSEExtensionsPreferences.this.getShell(), "SubSystem Added", "Queued Message SubSystem Added to: \n" + list);
				else
					MessageDialog.openInformation(RSEExtensionsPreferences.this.getShell(), "SubSystem Exists", "All IBM i systems have the Queued Message Subsystem defined.");
			}
		});		
		
		return rtnGroup;
    }

    public void init(IWorkbench workbench) {
        setPreferenceStore(ExtensionsPlugin.getDefault().getPreferenceStore());
    }
    
    public boolean getWarningPreference() {
        return getPreferenceStore().getBoolean(ExtensionsPlugin.PREFERENCE_COMPARE_MERGE_WARNING);
    }
    
    public void setWarningPreference(boolean warning) {
        getPreferenceStore().setValue(ExtensionsPlugin.PREFERENCE_COMPARE_MERGE_WARNING, warning);
    }
    
    public boolean getDefaultWarningPreference() {
        return ExtensionsPlugin.DEFAULT_COMPARE_MERGE_WARNING;
    }
    
	public boolean performOk() {
		setWarningPreference(warningButton.getSelection());
		return super.performOk();
	}
    
	public void performDefaults() {
		warningButton.setSelection(getDefaultWarningPreference());
		super.performDefaults();
	}	

}
