package com.softlanding.rse.extensions;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class RSEExtensionsPreferences extends PreferencePage implements IWorkbenchPreferencePage {
    private Button warningButton;

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
		
		Composite prefGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		prefGroup.setLayout(layout);
		prefGroup.setLayoutData(
		new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		warningButton = new Button(prefGroup, SWT.CHECK);
		warningButton.setText(ExtensionsPlugin.getResourceString("Preferences.1")); //$NON-NLS-1$
		warningButton.setSelection(getWarningPreference());
		
		return prefGroup;
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
