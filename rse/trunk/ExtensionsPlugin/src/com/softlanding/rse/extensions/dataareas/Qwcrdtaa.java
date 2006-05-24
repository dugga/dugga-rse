package com.softlanding.rse.extensions.dataareas;

import org.eclipse.swt.widgets.Display;

import com.ibm.as400.data.PcmlException;
import com.ibm.as400.data.ProgramCallDocument;
import com.ibm.etools.iseries.core.api.ISeriesObject;
import com.ibm.etools.systems.core.messages.SystemMessageException;

public class Qwcrdtaa {
    private ProgramCallDocument pcml;
    private ISeriesObject dataArea;
    private String type;
    
    public final static String CHAR = "*CHAR";
    public final static String DEC = "*DEC";
    public final static String LGL = "*LGL";

    public Qwcrdtaa(ISeriesObject dataArea) throws PcmlException, SystemMessageException {
        super();
        this.dataArea = dataArea;
        pcml = new ProgramCallDocument(dataArea.getISeriesConnection().getAS400ToolboxObject(Display.getCurrent().getActiveShell()), "com.softlanding.rse.extensions.extensions", this.getClass().getClassLoader());
    }
    
    public boolean callProgram() throws PcmlException {
        pcml.setValue("qwcrdtaa.receiverLength", new Integer((pcml.getOutputsize("qwcrdtaa.receiver"))));
        String dataAreaName = dataArea.getName();
        while (dataAreaName.length() < 10) dataAreaName = dataAreaName + " ";
        String libraryName = dataArea.getLibraryName();
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
