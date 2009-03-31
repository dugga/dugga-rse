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
package com.softlanding.rse.extensions.dataareas;

import org.eclipse.rse.services.clientserver.messages.SystemMessageException;
import org.eclipse.swt.widgets.Display;

import com.ibm.as400.access.AS400;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;
import com.ibm.etools.iseries.services.qsys.api.IQSYSObject;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;
import com.ibm.etools.iseries.subsystems.qsys.objects.IRemoteObjectContextProvider;

public class Qwcrdtaa {
    private ProgramCallDocument pcml;
    private IQSYSObject dataArea;
    private String type;
    
    public final static String CHAR = "*CHAR";
    public final static String DEC = "*DEC";
    public final static String LGL = "*LGL";

    public Qwcrdtaa(IQSYSObject dataArea) throws PcmlException, SystemMessageException {
        super();
        this.dataArea = dataArea;
        AS400 as400 = IBMiConnection.getConnection(((IRemoteObjectContextProvider)dataArea).getRemoteObjectContext().getObjectSubsystem().getHost()).getAS400ToolboxObject();
        pcml = new ProgramCallDocument(as400, "com.softlanding.rse.extensions.extensions", this.getClass().getClassLoader());
    }
    
    public boolean callProgram() throws PcmlException {
        pcml.setValue("qwcrdtaa.receiverLength", new Integer((pcml.getOutputsize("qwcrdtaa.receiver"))));
        String dataAreaName = dataArea.getName();
        while (dataAreaName.length() < 10) dataAreaName = dataAreaName + " ";
        String libraryName = dataArea.getLibrary();
        while (libraryName.length() < 10) libraryName = libraryName + " ";
        pcml.setValue("qwcrdtaa.dataArea", dataAreaName + libraryName);
        boolean returnCode = pcml.callProgram("qwcrdtaa");
        type = (String)pcml.getValue("qwcrdtaa.receiver.typeOfValue");
        return returnCode;
    }

    public String getType() {
        return type;
    }
}
