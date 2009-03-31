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
package com.softlanding.rse.extensions.dtaqs;

import org.eclipse.rse.services.clientserver.messages.SystemMessageException;
import com.ibm.as400.access.AS400;
import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;
import com.ibm.etools.iseries.services.qsys.api.IQSYSObject;
import com.ibm.etools.iseries.subsystems.qsys.api.IBMiConnection;
import com.ibm.etools.iseries.subsystems.qsys.objects.IRemoteObjectContextProvider;

public class Qmhqrdqd {
    private ProgramCallDocument pcml;
    private IQSYSObject dataQueue;
    private int bytesReturned;
    private int bytesAvailable;
    private int messageLength;
    private int keyLength;
    private String sequence;
    private String includeSenderID;
    private String forceToAuxilaryStorage;
    private String description;
    private String type;
    private String automaticReclaim;
    private String reserved;
    private int numberOfMessages;
    private int numberOfEntriesCurrentlyAllocated;
    private int maximumNumberOfEntriesAllowed;
    private int initialNumberOfEntries;
    private int maximumNumberOfEntriesSpecified;
    
    public static final int MAX16MB = -1;
    public static final int MAX2GB = -2;
    public static final String FIFO = "F";
    public static final String KEYED = "K";
    public static final String LIFO = "L";
    public static final String DDM = "1";

    public Qmhqrdqd(IQSYSObject dataQueue) throws PcmlException, SystemMessageException {
        super();
        this.dataQueue = dataQueue;
        AS400 as400 = IBMiConnection.getConnection(((IRemoteObjectContextProvider)dataQueue).getRemoteObjectContext().getObjectSubsystem().getHost()).getAS400ToolboxObject();
        pcml = new ProgramCallDocument(as400, "com.softlanding.rse.extensions.extensions", this.getClass().getClassLoader());
    }
    
    public boolean callProgram() throws PcmlException {
        pcml.setValue("qmhqrdqd.receiverLength", new Integer((pcml.getOutputsize("qmhqrdqd.receiver"))));
        String dataQueueName = dataQueue.getName();
        while (dataQueueName.length() < 10) dataQueueName = dataQueueName + " ";
        String libraryName = dataQueue.getLibrary();
        while (libraryName.length() < 10) libraryName = libraryName + " ";
        pcml.setValue("qmhqrdqd.dataQueue", dataQueueName + libraryName);
        boolean returnCode = pcml.callProgram("qmhqrdqd");
        bytesReturned = pcml.getIntValue("qmhqrdqd.receiver.bytesReturned");
        bytesAvailable = pcml.getIntValue("qmhqrdqd.receiver.bytesAvailable");
        messageLength = pcml.getIntValue("qmhqrdqd.receiver.messageLength");
        keyLength = pcml.getIntValue("qmhqrdqd.receiver.keyLength");
        sequence = (String)pcml.getValue("qmhqrdqd.receiver.sequence");
        includeSenderID = (String)pcml.getValue("qmhqrdqd.receiver.includeSender");
        forceToAuxilaryStorage = (String)pcml.getValue("qmhqrdqd.receiver.force");
        description = (String)pcml.getValue("qmhqrdqd.receiver.description");
        type = (String)pcml.getValue("qmhqrdqd.receiver.type");
        automaticReclaim = (String)pcml.getValue("qmhqrdqd.receiver.automaticReclaim");
        numberOfMessages = pcml.getIntValue("qmhqrdqd.receiver.numberMessages");
        numberOfEntriesCurrentlyAllocated = pcml.getIntValue("qmhqrdqd.receiver.numberAllocated");
        maximumNumberOfEntriesAllowed = pcml.getIntValue("qmhqrdqd.receiver.maximumEntries");
        initialNumberOfEntries = pcml.getIntValue("qmhqrdqd.receiver.initialEntries");
        maximumNumberOfEntriesSpecified = pcml.getIntValue("qmhqrdqd.receiver.maximumEntriesSpecified");
        return returnCode;
    }

    public String getAutomaticReclaim() {
        return automaticReclaim;
    }
    public boolean isAutomaticReclaim() {
        return automaticReclaim.equals("1");
    }
    public int getBytesAvailable() {
        return bytesAvailable;
    }
    public int getBytesReturned() {
        return bytesReturned;
    }
    public String getDescription() {
        return description;
    }
    public String getForceToAuxilaryStorage() {
        return forceToAuxilaryStorage;
    }
    public boolean isForceToAuxilaryStorage() {
        return forceToAuxilaryStorage.equals("Y");
    }
    public String getIncludeSenderID() {
        return includeSenderID;
    }
    public boolean isIncludeSenderID() {
        return includeSenderID.equals("Y");
    }
    public int getInitialNumberOfEntries() {
        return initialNumberOfEntries;
    }
    public int getKeyLength() {
        return keyLength;
    }
    public int getMaximumNumberOfEntriesAllowed() {
        return maximumNumberOfEntriesAllowed;
    }
    public int getMaximumNumberOfEntriesSpecified() {
        return maximumNumberOfEntriesSpecified;
    }
    public int getMessageLength() {
        return messageLength;
    }
    public int getNumberOfEntriesCurrentlyAllocated() {
        return numberOfEntriesCurrentlyAllocated;
    }
    public int getNumberOfMessages() {
        return numberOfMessages;
    }
    public String getSequence() {
        return sequence;
    }
    public String getType() {
        return type;
    }
}
