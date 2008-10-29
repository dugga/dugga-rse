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
package com.softlanding.rse.extensions.dtaqs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import com.ibm.as400.access.DataQueue;
import com.ibm.as400.access.DataQueueEntry;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.etools.iseries.core.api.ISeriesObject;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class DisplayDataQueueDialog extends Dialog {
    private ISeriesObject dataQueue;
    private Qmhqrdqd qmhqrdqd;
    private DataQueue dtaq;
    private Text descriptionText;
    private Text typeText;
    private Text sequenceText;
    private Text keyLengthText;
    private Text lengthText;
    private Text numberOfMessagesText;
    private Text numberOfEntriesCurrentlyAllocatedText;
    private Text maximumNumberOfEntriesAllowedText;
    private Text initialNumberOfEntriesText;
    private Text maximumNumberOfEntriesSpecifiedText;
    private Button includeSenderIDButton;
    private Button forceToAuxilaryStorageButton;
    private Button automaticReclaimButton;
    private Text firstMessageText;
    private int messageLength;
    private int keyLength;
    private String sequence;
    private boolean includeSenderID;
    private boolean forceToAuxilaryStorage;
    private String description;
    private String type;
    private boolean automaticReclaim;
    private int numberOfMessages;
    private int numberOfEntriesCurrentlyAllocated;
    private int maximumNumberOfEntriesAllowed;
    private int initialNumberOfEntries;
    private int maximumNumberOfEntriesSpecified;
    private String firstMessage;
    private boolean updatingChecks;

    public DisplayDataQueueDialog(Shell parentShell, ISeriesObject dataQueue) {
        super(parentShell);
        this.dataQueue = dataQueue;
        getAttributes();
    }
    
    public Control createDialogArea(Composite parent) {       
		Composite rtnGroup = (Composite)super.createDialogArea(parent);
		parent.getShell().setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.0")); //$NON-NLS-1$
		
		GridLayout rtnLayout = new GridLayout();
		rtnLayout.numColumns = 1;
		rtnGroup.setLayout(rtnLayout);
		rtnGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		Group headerGroup = new Group(rtnGroup, SWT.NONE);
		GridLayout headerLayout = new GridLayout();
		headerLayout.numColumns = 2;
		headerGroup.setLayout(headerLayout);
		headerGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		headerGroup.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.1")); //$NON-NLS-1$
		
		Label dataQueueLabel = new Label(headerGroup, SWT.NONE);
		dataQueueLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.2")); //$NON-NLS-1$
		Text dataQueueText = new Text(headerGroup, SWT.BORDER);
		dataQueueText.setEditable(false);
		GridData gd = new GridData();
		gd.widthHint = 75;
		dataQueueText.setLayoutData(gd);
		dataQueueText.setText(dataQueue.getName());
		
		Label libraryLabel = new Label(headerGroup, SWT.NONE);
		libraryLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.3")); //$NON-NLS-1$
		Text libraryText = new Text(headerGroup, SWT.BORDER);
		libraryText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		libraryText.setLayoutData(gd);
		libraryText.setText(dataQueue.getLibraryName());
		
		Label descriptionLabel = new Label(headerGroup, SWT.NONE);
		descriptionLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.4")); //$NON-NLS-1$
		descriptionText = new Text(headerGroup, SWT.BORDER);
		descriptionText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 375;
		descriptionText.setLayoutData(gd);
		
		Group attributeGroup = new Group(rtnGroup, SWT.NONE);
		GridLayout attributeLayout = new GridLayout();
		attributeLayout.numColumns = 2;
		attributeGroup.setLayout(attributeLayout);
		attributeGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		attributeGroup.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.5")); //$NON-NLS-1$
		
		Label typeLabel = new Label(attributeGroup, SWT.NONE);
		typeLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.6")); //$NON-NLS-1$
		typeText = new Text(attributeGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		typeText.setLayoutData(gd);
		typeText.setEditable(false);
		
		Label sequenceLabel = new Label(attributeGroup, SWT.NONE);
		sequenceLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.7")); //$NON-NLS-1$
		sequenceText = new Text(attributeGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		sequenceText.setLayoutData(gd);
		sequenceText.setEditable(false);
		if (sequence.equals(Qmhqrdqd.KEYED)) {
		    Label keyLengthLabel = new Label(attributeGroup, SWT.NONE);
		    keyLengthLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.8")); //$NON-NLS-1$
		    keyLengthText = new Text(attributeGroup, SWT.BORDER);
		    gd = new GridData();
		    gd.widthHint = 75;
		    keyLengthText.setLayoutData(gd);
		    keyLengthText.setEditable(false);
		}
		
		Label lengthLabel = new Label(attributeGroup, SWT.NONE);
		lengthLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.9")); //$NON-NLS-1$
		lengthText = new Text(attributeGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		lengthText.setLayoutData(gd);
		lengthText.setEditable(false);
		
		Label numberOfMessagesLabel = new Label(attributeGroup, SWT.NONE);
		numberOfMessagesLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.10")); //$NON-NLS-1$
		numberOfMessagesText = new Text(attributeGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		numberOfMessagesText.setLayoutData(gd);
		numberOfMessagesText.setEditable(false);
		
		Label numberOfEntriesCurrentlyAllocatedLabel = new Label(attributeGroup, SWT.NONE);
		numberOfEntriesCurrentlyAllocatedLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.11")); //$NON-NLS-1$
		numberOfEntriesCurrentlyAllocatedText = new Text(attributeGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		numberOfEntriesCurrentlyAllocatedText.setLayoutData(gd);
		numberOfEntriesCurrentlyAllocatedText.setEditable(false);
		
		Label maximumNumberOfEntriesAllowedLabel = new Label(attributeGroup, SWT.NONE);
		maximumNumberOfEntriesAllowedLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.12")); //$NON-NLS-1$
		maximumNumberOfEntriesAllowedText = new Text(attributeGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		maximumNumberOfEntriesAllowedText.setLayoutData(gd);
		maximumNumberOfEntriesAllowedText.setEditable(false);
		
		Label initialNumberOfEntriesLabel = new Label(attributeGroup, SWT.NONE);
		initialNumberOfEntriesLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.13")); //$NON-NLS-1$
		initialNumberOfEntriesText = new Text(attributeGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		initialNumberOfEntriesText.setLayoutData(gd);
		initialNumberOfEntriesText.setEditable(false);
		
		Label maximumNumberOfEntriesSpecifiedLabel = new Label(attributeGroup, SWT.NONE);
		maximumNumberOfEntriesSpecifiedLabel.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.14")); //$NON-NLS-1$
		maximumNumberOfEntriesSpecifiedText = new Text(attributeGroup, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 75;
		maximumNumberOfEntriesSpecifiedText.setLayoutData(gd);
		maximumNumberOfEntriesSpecifiedText.setEditable(false);
		
		Composite buttonGroup = new Composite(attributeGroup, SWT.NONE);
		GridLayout buttonLayout = new GridLayout();
		buttonLayout.numColumns = 3;
		buttonGroup.setLayout(buttonLayout);
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		buttonGroup.setLayoutData(gd);
		
		includeSenderIDButton = new Button(buttonGroup, SWT.CHECK);
		includeSenderIDButton.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.15")); //$NON-NLS-1$
		gd = new GridData();
		includeSenderIDButton.setLayoutData(gd);
		
		forceToAuxilaryStorageButton = new Button(buttonGroup, SWT.CHECK);
		forceToAuxilaryStorageButton.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.16")); //$NON-NLS-1$
		gd = new GridData();
		forceToAuxilaryStorageButton.setLayoutData(gd);
		
		automaticReclaimButton = new Button(buttonGroup, SWT.CHECK);
		automaticReclaimButton.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.17")); //$NON-NLS-1$
		gd = new GridData();
		automaticReclaimButton.setLayoutData(gd);
		
		Group messageGroup = new Group(rtnGroup, SWT.NONE);
		GridLayout messageLayout = new GridLayout();
		messageLayout.numColumns = 1;
		messageGroup.setLayout(messageLayout);
		messageGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		messageGroup.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.18")); //$NON-NLS-1$
		
		firstMessageText = new Text(messageGroup, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		gd = new GridData();
		gd.widthHint = 500;
		gd.heightHint = 150;
		firstMessageText.setLayoutData(gd);
		
		refresh();
		
		SelectionListener selectionListener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (updatingChecks) return;
                updatingChecks = true;
                includeSenderIDButton.setSelection(includeSenderID);
                forceToAuxilaryStorageButton.setSelection(forceToAuxilaryStorage);
                automaticReclaimButton.setSelection(automaticReclaim);
                updatingChecks = false;
            }
		};

		includeSenderIDButton.addSelectionListener(selectionListener);
		forceToAuxilaryStorageButton.addSelectionListener(selectionListener);
		automaticReclaimButton.addSelectionListener(selectionListener);
		
		return rtnGroup;
    }
    
	public void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		Button refreshButton = createButton(parent, 2, ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.19"), false); //$NON-NLS-1$
		refreshButton.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent e) {
		        getAttributes();
		        refresh();
		    }
		});
	}
    
    private void getAttributes() {
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            public void run() {
                try {
                    if (qmhqrdqd == null) qmhqrdqd = new Qmhqrdqd(DisplayDataQueueDialog.this.dataQueue);
                    qmhqrdqd.callProgram();
                    messageLength = qmhqrdqd.getMessageLength();
                    keyLength = qmhqrdqd.getKeyLength();
                    sequence = qmhqrdqd.getSequence();
                    includeSenderID = qmhqrdqd.isIncludeSenderID();
                    forceToAuxilaryStorage = qmhqrdqd.isForceToAuxilaryStorage();
                    description = qmhqrdqd.getDescription();
                    type = qmhqrdqd.getType();
                    automaticReclaim = qmhqrdqd.isAutomaticReclaim();
                    numberOfMessages = qmhqrdqd.getNumberOfMessages();
                    numberOfEntriesCurrentlyAllocated = qmhqrdqd.getNumberOfEntriesCurrentlyAllocated();
                    maximumNumberOfEntriesAllowed =qmhqrdqd.getMaximumNumberOfEntriesAllowed();
                    initialNumberOfEntries = qmhqrdqd.getInitialNumberOfEntries();
                    maximumNumberOfEntriesSpecified = qmhqrdqd.getMaximumNumberOfEntriesSpecified();
                    String dataQueueName = DisplayDataQueueDialog.this.dataQueue.getName();
                    while (dataQueueName.length() < 10) dataQueueName = dataQueueName + " ";
                    String libraryName = DisplayDataQueueDialog.this.dataQueue.getLibraryName();
                    while (libraryName.length() < 10) libraryName = libraryName + " ";
                    AS400 as400 = DisplayDataQueueDialog.this.dataQueue.getISeriesConnection().getAS400ToolboxObject(getShell());
                    QSYSObjectPathName path = new QSYSObjectPathName(DisplayDataQueueDialog.this.dataQueue.getLibraryName(), DisplayDataQueueDialog.this.dataQueue.getName(), "DTAQ"); //$NON-NLS-1$                    
                    if (type.equals(Qmhqrdqd.DDM)) firstMessage = ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.20"); //$NON-NLS-1$
                    else {
	                    if (sequence.equals(Qmhqrdqd.KEYED)) firstMessage = ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.21"); //$NON-NLS-1$
	                    else {
	                        dtaq = new DataQueue(as400, path.getPath());
	                        DataQueueEntry entry = dtaq.peek();
	                        if (entry == null) firstMessage = ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.22"); //$NON-NLS-1$
	                        else firstMessage = entry.getString();
	                    }
                    }
                } catch (Exception e) {
                    ExtensionsPlugin.log(e);
                }
            }           
        });         
    }
    
    private void refresh() {
        descriptionText.setText(description);
        if (type.equals(Qmhqrdqd.DDM)) typeText.setText("DDM"); //$NON-NLS-1$
        else typeText.setText(ExtensionsPlugin.getResourceString("DisplayDataQueueDialog.23")); //$NON-NLS-1$
		if (sequence.equals(Qmhqrdqd.FIFO)) sequenceText.setText("*FIFO"); //$NON-NLS-1$
		else if (sequence.equals(Qmhqrdqd.LIFO)) sequenceText.setText("*LIFO"); //$NON-NLS-1$
		else if (sequence.equals(Qmhqrdqd.KEYED)) {
		    sequenceText.setText("*KEYED"); //$NON-NLS-1$
		    if (keyLengthText != null) keyLengthText.setText(Integer.toString(keyLength));
		}
		lengthText.setText(Integer.toString(messageLength));
		numberOfMessagesText.setText(Integer.toString(numberOfMessages));
		numberOfEntriesCurrentlyAllocatedText.setText(Integer.toString(numberOfEntriesCurrentlyAllocated));
		maximumNumberOfEntriesAllowedText.setText(Integer.toString(maximumNumberOfEntriesAllowed));
		initialNumberOfEntriesText.setText(Integer.toString(initialNumberOfEntries));
		if (maximumNumberOfEntriesSpecified == Qmhqrdqd.MAX16MB) maximumNumberOfEntriesSpecifiedText.setText("*MAX16MB"); //$NON-NLS-1$
		else if (maximumNumberOfEntriesSpecified == Qmhqrdqd.MAX2GB) maximumNumberOfEntriesSpecifiedText.setText("*MAX2GB"); //$NON-NLS-1$
		else maximumNumberOfEntriesSpecifiedText.setText(Integer.toString(maximumNumberOfEntriesSpecified));
		includeSenderIDButton.setSelection(includeSenderID);
		forceToAuxilaryStorageButton.setSelection(forceToAuxilaryStorage);
		automaticReclaimButton.setSelection(automaticReclaim);
		firstMessageText.setText(firstMessage);
    }
    
}
