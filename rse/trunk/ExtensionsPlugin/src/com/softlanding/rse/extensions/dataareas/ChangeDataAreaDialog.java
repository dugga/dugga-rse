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
package com.softlanding.rse.extensions.dataareas;

import java.math.BigDecimal;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.rse.services.clientserver.messages.SystemMessageException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
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

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.LogicalDataArea;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.etools.iseries.services.qsys.api.IQSYSObject;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;
import com.ibm.etools.iseries.subsystems.qsys.objects.IRemoteObjectContextProvider;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class ChangeDataAreaDialog extends Dialog {
    private IQSYSObject dataArea;
    private CharacterDataArea characterDataArea;
    private DecimalDataArea decimalDataArea;
    private LogicalDataArea logicalDataArea;
    private Qwcrdtaa qwcrdtaa;
    private Text lengthText;
    private Text decimalPositionsText;
    private Text descriptionText;
    private Text valueText;
    private Button trueButton;
    private Button falseButton;
    private String value;
    private boolean logicalValue;
    private int length;
    private int decimals;
    private String description;
    private boolean success;
    private AS400 as400;

    public ChangeDataAreaDialog(Shell parentShell, IQSYSObject dataArea) {
        super(parentShell);
        this.dataArea = dataArea;
        try {
			as400 = IBMiConnection.getConnection(((IRemoteObjectContextProvider)dataArea).getRemoteObjectContext().getObjectSubsystem().getHost()).getAS400ToolboxObject();
		} catch (SystemMessageException e1) {
			e1.printStackTrace();
		}
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            public void run() {
                try {
                    qwcrdtaa = new Qwcrdtaa(ChangeDataAreaDialog.this.dataArea);
                    qwcrdtaa.callProgram();
                    String dataAreaName = ChangeDataAreaDialog.this.dataArea.getName();
                    while (dataAreaName.length() < 10) dataAreaName = dataAreaName + " "; //$NON-NLS-1$
                    String libraryName = ChangeDataAreaDialog.this.dataArea.getLibrary();
                    while (libraryName.length() < 10) libraryName = libraryName + " "; //$NON-NLS-1$
                    QSYSObjectPathName path = new QSYSObjectPathName(ChangeDataAreaDialog.this.dataArea.getLibrary(), ChangeDataAreaDialog.this.dataArea.getName(), "DTAARA"); //$NON-NLS-1$
                    description = ChangeDataAreaDialog.this.dataArea.getDescription();
                    if (qwcrdtaa.getType().equals(Qwcrdtaa.CHAR)) {
                        characterDataArea = new CharacterDataArea(as400, path.getPath());
                        value = characterDataArea.read();
                        length = characterDataArea.getLength();
                    }
                    if (qwcrdtaa.getType().equals(Qwcrdtaa.DEC)) {
                        decimalDataArea = new DecimalDataArea(as400, path.getPath());
                        value = decimalDataArea.read().toString();
                        length = decimalDataArea.getLength();
                        decimals = decimalDataArea.getDecimalPositions();
                    }
                    if (qwcrdtaa.getType().equals(Qwcrdtaa.LGL)) {
                        logicalDataArea = new LogicalDataArea(as400, path.getPath());
                        logicalValue = logicalDataArea.read();
                    }
                } catch (Exception e) {
                    ExtensionsPlugin.log(e);
                }
            }            
        });
    }
    
    public Control createDialogArea(Composite parent) {       
		Composite rtnGroup = (Composite)super.createDialogArea(parent);
		parent.getShell().setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.0")); //$NON-NLS-1$
		
		GridLayout rtnLayout = new GridLayout();
		rtnLayout.numColumns = 1;
		rtnGroup.setLayout(rtnLayout);
		rtnGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		Group headerGroup = new Group(rtnGroup, SWT.NONE);
		GridLayout headerLayout = new GridLayout();
		headerLayout.numColumns = 2;
		headerGroup.setLayout(headerLayout);
		headerGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		headerGroup.setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.1")); //$NON-NLS-1$
		
		Label dataAreaLabel = new Label(headerGroup, SWT.NONE);
		dataAreaLabel.setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.2")); //$NON-NLS-1$
		Text dataAreaText = new Text(headerGroup, SWT.BORDER);
		dataAreaText.setEditable(false);
		GridData gd = new GridData();
		gd.widthHint = 75;
		dataAreaText.setLayoutData(gd);
		dataAreaText.setText(dataArea.getName());
		
		Label libraryLabel = new Label(headerGroup, SWT.NONE);
		libraryLabel.setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.3")); //$NON-NLS-1$
		Text libraryText = new Text(headerGroup, SWT.BORDER);
		libraryText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		libraryText.setLayoutData(gd);
		libraryText.setText(dataArea.getLibrary());
		
		Label typeLabel = new Label(headerGroup, SWT.NONE);
		typeLabel.setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.4")); //$NON-NLS-1$
		Text typeText = new Text(headerGroup, SWT.BORDER);
		typeText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		typeText.setLayoutData(gd);
		typeText.setText(qwcrdtaa.getType());
		
		if (!qwcrdtaa.getType().equals("*LGL")) {
			Label lengthLabel = new Label(headerGroup, SWT.NONE);
			lengthLabel.setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.8")); //$NON-NLS-1$
			Text lengthText = new Text(headerGroup, SWT.BORDER);
			lengthText.setEditable(false);
			gd = new GridData();
			gd.widthHint = 75;
			lengthText.setLayoutData(gd);
			lengthText.setText(String.valueOf(length));
		}
		
		if (qwcrdtaa.getType().equals("*DEC")) {
			Label decimalLabel = new Label(headerGroup, SWT.NONE);
			decimalLabel.setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.9")); //$NON-NLS-1$
			Text decimalText = new Text(headerGroup, SWT.BORDER);
			decimalText.setEditable(false);
			gd = new GridData();
			gd.widthHint = 75;
			decimalText.setLayoutData(gd);
			decimalText.setText(String.valueOf(decimals));
		}
		
		Label descriptionLabel = new Label(headerGroup, SWT.NONE);
		descriptionLabel.setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.10")); //$NON-NLS-1$
		Text descriptionText = new Text(headerGroup, SWT.BORDER);
		descriptionText.setEditable(false);
		gd = new GridData();
		if (description == " ") {
			gd.widthHint = 75;
		}
		descriptionText.setLayoutData(gd);
		descriptionText.setText(description);
		
		Group valueGroup = new Group(rtnGroup, SWT.NONE);
		valueGroup.setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.5")); //$NON-NLS-1$
		GridLayout valueLayout = new GridLayout();
		valueLayout.numColumns = 1;
		valueGroup.setLayout(valueLayout);
		valueGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		if (qwcrdtaa.getType().equals(Qwcrdtaa.LGL)) {
		    trueButton = new Button(valueGroup, SWT.RADIO);
		    trueButton.setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.6")); //$NON-NLS-1$
		    falseButton = new Button(valueGroup, SWT.RADIO);
		    falseButton.setText(ExtensionsPlugin.getResourceString("ChangeDataAreaDialog.7")); //$NON-NLS-1$
		    if (logicalValue) trueButton.setSelection(true);
		    else falseButton.setSelection(true);
		} else {
		    gd = new GridData();
		    if (length > 50) {
		        valueText = new Text(valueGroup, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
				gd.widthHint = 300;
				gd.heightHint = 100;
		    } else {
		        valueText = new Text(valueGroup, SWT.BORDER);
		        if (length < 5) gd.widthHint = 40;
		        else if (length <= 10) gd.widthHint = 75;
		        else if (length <= 20) gd.widthHint = 150;
		        else gd.widthHint = 300;
		    }
		    valueText.setLayoutData(gd);
		    valueText.setText(value);
		    if (qwcrdtaa.getType().equals(Qwcrdtaa.DEC)) valueText.setTextLimit(length + 1);
		    else valueText.setTextLimit(length);
		    valueText.setFocus();
		}
		
		return rtnGroup;
    }
   
    protected void okPressed() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            public void run() {
                try {
                    if (qwcrdtaa.getType().equals(Qwcrdtaa.LGL)) {
                        logicalDataArea.write(trueButton.getSelection());
                    }
                    if (qwcrdtaa.getType().equals(Qwcrdtaa.CHAR)) {
                    	if (valueText.getText().trim().length() > 0) {
                    		characterDataArea.clear();
                    		characterDataArea.write(valueText.getText().trim());
                    	}
                    	else
                    		characterDataArea.clear();
                    }
                    if (qwcrdtaa.getType().equals(Qwcrdtaa.DEC)) {
                        decimalDataArea.write(new BigDecimal(valueText.getText().trim()));
                    }
                    success = true;
                } catch (Exception e) {
                    ExtensionsPlugin.log(e);
                    success = false;
                }
            }            
        });
        if (!success) return;
        super.okPressed();
    }
}
