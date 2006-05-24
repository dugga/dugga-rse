package com.softlanding.rse.extensions.dataareas;

import java.math.BigDecimal;

import org.eclipse.jface.dialogs.Dialog;
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
import com.ibm.etools.iseries.core.api.ISeriesObject;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class ChangeDataAreaDialog extends Dialog {
    private ISeriesObject dataArea;
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
    private boolean success;

    public ChangeDataAreaDialog(Shell parentShell, ISeriesObject dataArea) {
        super(parentShell);
        this.dataArea = dataArea;
        BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
            public void run() {
                try {
                    qwcrdtaa = new Qwcrdtaa(ChangeDataAreaDialog.this.dataArea);
                    qwcrdtaa.callProgram();
                    String dataAreaName = ChangeDataAreaDialog.this.dataArea.getName();
                    while (dataAreaName.length() < 10) dataAreaName = dataAreaName + " ";
                    String libraryName = ChangeDataAreaDialog.this.dataArea.getLibraryName();
                    while (libraryName.length() < 10) libraryName = libraryName + " ";
                    AS400 as400 = ChangeDataAreaDialog.this.dataArea.getISeriesConnection().getAS400ToolboxObject(getShell());
                    QSYSObjectPathName path = new QSYSObjectPathName(ChangeDataAreaDialog.this.dataArea.getLibraryName(), ChangeDataAreaDialog.this.dataArea.getName(), "DTAARA");
                    if (qwcrdtaa.getType().equals(Qwcrdtaa.CHAR)) {
                        characterDataArea = new CharacterDataArea(as400, path.getPath());
                        value = characterDataArea.read();
                        length = characterDataArea.getLength();
                    }
                    if (qwcrdtaa.getType().equals(Qwcrdtaa.DEC)) {
                        decimalDataArea = new DecimalDataArea(as400, path.getPath());
                        value = decimalDataArea.read().toString();
                        length = decimalDataArea.getLength();
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
		parent.getShell().setText("Change Data Area");
		
		GridLayout rtnLayout = new GridLayout();
		rtnLayout.numColumns = 1;
		rtnGroup.setLayout(rtnLayout);
		rtnGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		Group headerGroup = new Group(rtnGroup, SWT.NONE);
		GridLayout headerLayout = new GridLayout();
		headerLayout.numColumns = 2;
		headerGroup.setLayout(headerLayout);
		headerGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		headerGroup.setText("Data area:");
		
		Label dataAreaLabel = new Label(headerGroup, SWT.NONE);
		dataAreaLabel.setText("Name:");
		Text dataAreaText = new Text(headerGroup, SWT.BORDER);
		dataAreaText.setEditable(false);
		GridData gd = new GridData();
		gd.widthHint = 75;
		dataAreaText.setLayoutData(gd);
		dataAreaText.setText(dataArea.getName());
		
		Label libraryLabel = new Label(headerGroup, SWT.NONE);
		libraryLabel.setText("Library:");
		Text libraryText = new Text(headerGroup, SWT.BORDER);
		libraryText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		libraryText.setLayoutData(gd);
		libraryText.setText(dataArea.getLibraryName());
		
		Label typeLabel = new Label(headerGroup, SWT.NONE);
		typeLabel.setText("Type:");
		Text typeText = new Text(headerGroup, SWT.BORDER);
		typeText.setEditable(false);
		gd = new GridData();
		gd.widthHint = 75;
		typeText.setLayoutData(gd);
		typeText.setText(qwcrdtaa.getType());
		
		Group valueGroup = new Group(rtnGroup, SWT.NONE);
		valueGroup.setText("Value:");
		GridLayout valueLayout = new GridLayout();
		valueLayout.numColumns = 1;
		valueGroup.setLayout(valueLayout);
		valueGroup.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		
		if (qwcrdtaa.getType().equals(Qwcrdtaa.LGL)) {
		    trueButton = new Button(valueGroup, SWT.RADIO);
		    trueButton.setText("true");
		    falseButton = new Button(valueGroup, SWT.RADIO);
		    falseButton.setText("false");
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
                        characterDataArea.write(valueText.getText().trim());
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
