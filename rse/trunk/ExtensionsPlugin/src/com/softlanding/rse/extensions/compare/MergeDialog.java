package com.softlanding.rse.extensions.compare;

import org.eclipse.jface.dialogs.Dialog;
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

public class MergeDialog extends Dialog {
    private ISeriesMember member;
    private ISeriesConnection[] iSeriesConnections;
    private Combo maintenanceConnectionCombo;
    private Text maintenanceLibraryText;
    private Text maintenanceFileText;
    private Text maintenanceMemberText;
    private Combo rootConnectionCombo;
    private Text rootLibraryText;
    private Text rootFileText;
    private Text rootMemberText;
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
        getRseConnections();
        
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
		maintenanceLayout.numColumns = 2;
		maintenanceGroup.setLayout(maintenanceLayout);
		maintenanceGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 
		
		Label maintenanceConnectionLabel = new Label(maintenanceGroup, SWT.NONE);
		maintenanceConnectionLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.7")); //$NON-NLS-1$
		maintenanceConnectionCombo = new Combo(maintenanceGroup, SWT.READ_ONLY);
		gd = new GridData();
		gd.widthHint = 200;
		maintenanceConnectionCombo.setLayoutData(gd);
		
		Label maintenanceLibraryLabel = new Label(maintenanceGroup, SWT.NONE);
		maintenanceLibraryLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.8")); //$NON-NLS-1$
		maintenanceLibraryText = new Text(maintenanceGroup, SWT.BORDER);
		maintenanceLibraryText.setTextLimit(10);
		gd = new GridData();
		gd.widthHint = 75;
		maintenanceLibraryText.setLayoutData(gd);
		
		Label maintenanceFileLabel = new Label(maintenanceGroup, SWT.NONE);
		maintenanceFileLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.9")); //$NON-NLS-1$
		maintenanceFileText = new Text(maintenanceGroup, SWT.BORDER);
		maintenanceFileText.setTextLimit(10);
		gd = new GridData();
		gd.widthHint = 75;
		maintenanceFileText.setLayoutData(gd);
		maintenanceFileText.setText(member.getFile());
		
		Label maintenanceMemberLabel = new Label(maintenanceGroup, SWT.NONE);
		maintenanceMemberLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.10")); //$NON-NLS-1$
		maintenanceMemberText = new Text(maintenanceGroup, SWT.BORDER);
		maintenanceMemberText.setTextLimit(10);
		gd = new GridData();
		gd.widthHint = 75;
		maintenanceMemberText.setLayoutData(gd);
		maintenanceMemberText.setText(member.getName());
		
		Group rootGroup = new Group(rtnGroup, SWT.NONE);
		rootGroup.setText(ExtensionsPlugin.getResourceString("MergeDialog.11")); //$NON-NLS-1$
		GridLayout rootLayout = new GridLayout();
		rootLayout.numColumns = 2;
		rootGroup.setLayout(rootLayout);
		rootGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL)); 		

		Label rootConnectionLabel = new Label(rootGroup, SWT.NONE);
		rootConnectionLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.12")); //$NON-NLS-1$
		rootConnectionCombo = new Combo(rootGroup, SWT.READ_ONLY);
		gd = new GridData();
		gd.widthHint = 200;
		rootConnectionCombo.setLayoutData(gd);
		
		Label rootLibraryLabel = new Label(rootGroup, SWT.NONE);
		rootLibraryLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.13")); //$NON-NLS-1$
		rootLibraryText = new Text(rootGroup, SWT.BORDER);
		rootLibraryText.setTextLimit(10);
		gd = new GridData();
		gd.widthHint = 75;
		rootLibraryText.setLayoutData(gd);
		
		Label rootFileLabel = new Label(rootGroup, SWT.NONE);
		rootFileLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.14")); //$NON-NLS-1$
		rootFileText = new Text(rootGroup, SWT.BORDER);
		rootFileText.setTextLimit(10);
		gd = new GridData();
		gd.widthHint = 75;
		rootFileText.setLayoutData(gd);
		rootFileText.setText(member.getFile());
		
		Label rootMemberLabel = new Label(rootGroup, SWT.NONE);
		rootMemberLabel.setText(ExtensionsPlugin.getResourceString("MergeDialog.15")); //$NON-NLS-1$
		rootMemberText = new Text(rootGroup, SWT.BORDER);
		rootMemberText.setTextLimit(10);
		gd = new GridData();
		gd.widthHint = 75;
		rootMemberText.setLayoutData(gd);
		rootMemberText.setText(member.getName());		
		
		for (int i = 0; i < iSeriesConnections.length; i++) {
		    maintenanceConnectionCombo.add(iSeriesConnections[i].getConnectionName());
		    rootConnectionCombo.add(iSeriesConnections[i].getConnectionName());
		}
		maintenanceConnectionCombo.select(maintenanceConnectionCombo.indexOf(member.getISeriesConnection().getConnectionName()));
		rootConnectionCombo.select(rootConnectionCombo.indexOf(member.getISeriesConnection().getConnectionName()));
		
		ModifyListener modifyListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                okButton.setEnabled(canFinish());
            }		    
		};
		
		maintenanceLibraryText.addModifyListener(modifyListener);
		maintenanceFileText.addModifyListener(modifyListener);
		maintenanceMemberText.addModifyListener(modifyListener);
		rootLibraryText.addModifyListener(modifyListener);
		rootFileText.addModifyListener(modifyListener);
		rootMemberText.addModifyListener(modifyListener);
		
		maintenanceLibraryText.setFocus();
		
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
        maintenanceConnection = iSeriesConnections[maintenanceConnectionCombo.getSelectionIndex()];
        rootConnection = iSeriesConnections[rootConnectionCombo.getSelectionIndex()];
        maintenanceLibrary = maintenanceLibraryText.getText().trim().toUpperCase();
        maintenanceFile = maintenanceFileText.getText().trim().toUpperCase();
        maintenanceMember = maintenanceMemberText.getText().trim().toUpperCase();
        rootLibrary = rootLibraryText.getText().trim().toUpperCase();
        rootFile = rootFileText.getText().trim().toUpperCase();
        rootMember = rootMemberText.getText().trim().toUpperCase();
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
        if (maintenanceLibraryText.getText().trim().length() == 0 ||
                maintenanceFileText.getText().trim().length() == 0 ||
                maintenanceMemberText.getText().trim().length() == 0 ||
                rootLibraryText.getText().trim().length() == 0 ||
                rootFileText.getText().trim().length() == 0 ||
                rootMemberText.getText().trim().length() == 0) return false;
        if (maintenanceLibraryText.getText().trim().equalsIgnoreCase(rootLibraryText.getText().trim()) &&
                maintenanceFileText.getText().trim().equalsIgnoreCase(rootFileText.getText().trim()) &&
                maintenanceMemberText.getText().trim().equalsIgnoreCase(rootMemberText.getText().trim())) return false;
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
