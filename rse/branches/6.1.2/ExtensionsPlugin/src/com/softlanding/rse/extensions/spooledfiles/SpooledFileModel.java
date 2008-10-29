/*******************************************************************************
 * Copyright (c) 2005 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *******************************************************************************/
package com.softlanding.rse.extensions.spooledfiles;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.SpooledFile;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class SpooledFileModel implements IPropertySource {
	private SpooledFile spooledFile;

	public static String P_ID_NAME = "file"; //$NON-NLS-1$
	public static String P_NAME = ExtensionsPlugin.getResourceString("File_2");	 //$NON-NLS-1$
	public static String P_ID_OUTQ = "outq"; //$NON-NLS-1$
	public static String P_OUTQ = ExtensionsPlugin.getResourceString("Output_queue_4");	 //$NON-NLS-1$
	public static String P_ID_USERDATA = "dta"; //$NON-NLS-1$
	public static String P_USERDATA = ExtensionsPlugin.getResourceString("User_data_6"); //$NON-NLS-1$
	public static String P_ID_SPLFSTATUS = "sts"; //$NON-NLS-1$
	public static String P_SPLFSTATUS = ExtensionsPlugin.getResourceString("Status_8"); //$NON-NLS-1$
	public static String P_ID_PAGES = "pages"; //$NON-NLS-1$
	public static String P_PAGES = ExtensionsPlugin.getResourceString("Total_pages_10"); //$NON-NLS-1$
	public static String P_ID_CURPAGE = "page"; //$NON-NLS-1$
	public static String P_CURPAGE = ExtensionsPlugin.getResourceString("Current_page_12");	 //$NON-NLS-1$
	public static String P_ID_JOBUSER = "user"; //$NON-NLS-1$
	public static String P_JOBUSER = ExtensionsPlugin.getResourceString("Job_user_14");	 //$NON-NLS-1$
	public static String P_ID_JOB = "job"; //$NON-NLS-1$
	public static String P_JOB = ExtensionsPlugin.getResourceString("Job_name_16"); //$NON-NLS-1$
	public static String P_ID_JOBNBR = "jobnbr"; //$NON-NLS-1$
	public static String P_JOBNBR = ExtensionsPlugin.getResourceString("Job_number_18"); //$NON-NLS-1$
	public static String P_ID_NBR = "nbr"; //$NON-NLS-1$
	public static String P_NBR = ExtensionsPlugin.getResourceString("File_number_20"); //$NON-NLS-1$
	public static String P_ID_OUTPTY = "pty"; //$NON-NLS-1$
	public static String P_OUTPTY = ExtensionsPlugin.getResourceString("Priority_22"); //$NON-NLS-1$
	public static String P_ID_COPIES = "cpy"; //$NON-NLS-1$
	public static String P_COPIES = ExtensionsPlugin.getResourceString("Copies_24"); //$NON-NLS-1$
	public static String P_ID_FORMTYPE = "type"; //$NON-NLS-1$
	public static String P_FORMTYPE = ExtensionsPlugin.getResourceString("Form_type_26"); //$NON-NLS-1$
	public static String P_ID_DATE = "date"; //$NON-NLS-1$
	public static String P_DATE = ExtensionsPlugin.getResourceString("Creation_date_28"); //$NON-NLS-1$
	public static String P_ID_TIME = "time"; //$NON-NLS-1$
	public static String P_TIME = ExtensionsPlugin.getResourceString("Creation_time_30");																				 //$NON-NLS-1$
	public static final String P_DESCRIPTORS = "properties"; //$NON-NLS-1$
	static private List descriptors;
	static
	{	
		descriptors = new ArrayList();
		descriptors.add(new PropertyDescriptor(P_ID_NAME, P_NAME));
		descriptors.add(new PropertyDescriptor(P_ID_OUTQ, P_OUTQ));
		descriptors.add(new PropertyDescriptor(P_ID_USERDATA, P_USERDATA));
		descriptors.add(new PropertyDescriptor(P_ID_SPLFSTATUS, P_SPLFSTATUS));
		descriptors.add(new PropertyDescriptor(P_ID_PAGES, P_PAGES));
		descriptors.add(new PropertyDescriptor(P_ID_CURPAGE, P_CURPAGE));
		descriptors.add(new PropertyDescriptor(P_ID_JOBUSER, P_JOBUSER));
		descriptors.add(new PropertyDescriptor(P_ID_JOB, P_JOB));
		descriptors.add(new PropertyDescriptor(P_ID_JOBNBR, P_JOBNBR));
		descriptors.add(new PropertyDescriptor(P_ID_NBR, P_NBR));
		descriptors.add(new PropertyDescriptor(P_ID_OUTPTY, P_OUTPTY));
		descriptors.add(new PropertyDescriptor(P_ID_COPIES, P_COPIES));
		descriptors.add(new PropertyDescriptor(P_ID_FORMTYPE, P_FORMTYPE));
		descriptors.add(new PropertyDescriptor(P_ID_DATE, P_DATE));
		descriptors.add(new PropertyDescriptor(P_ID_TIME, P_TIME));
	}

	public SpooledFileModel() {
		super();
	}
	
	public SpooledFileModel(SpooledFile spooledFile) {
		this();
		this.spooledFile = spooledFile;
	}

	public Object getEditableValue() {
		try {
			return spooledFile.getStringAttribute(PrintObject.ATTR_SPOOLFILE);
		} catch (Exception e) {
			return ""; //$NON-NLS-1$
		}
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return (IPropertyDescriptor[])getDescriptors().toArray(new IPropertyDescriptor[getDescriptors().size()]);
	}

	public Object getPropertyValue(Object propKey) {
		try {
			if (P_ID_NAME.equals(propKey)) return spooledFile.getName();
			if (P_ID_OUTQ.equals(propKey)) return getOutputQueue();
			if (P_ID_USERDATA.equals(propKey)) return spooledFile.getStringAttribute(PrintObject.ATTR_USERDATA);
			if (P_ID_SPLFSTATUS.equals(propKey)) return spooledFile.getStringAttribute(PrintObject.ATTR_SPLFSTATUS);
			if (P_ID_PAGES.equals(propKey)) return spooledFile.getIntegerAttribute(PrintObject.ATTR_PAGES).toString();
			if (P_ID_CURPAGE.equals(propKey)) return spooledFile.getIntegerAttribute(PrintObject.ATTR_CURPAGE).toString();
			if (P_ID_JOBUSER.equals(propKey)) return spooledFile.getJobUser();
			if (P_ID_JOB.equals(propKey)) return spooledFile.getJobName();
			if (P_ID_JOBNBR.equals(propKey)) return spooledFile.getJobNumber();
			if (P_ID_NBR.equals(propKey)) return new Integer(spooledFile.getNumber()).toString();
			if (P_ID_OUTPTY.equals(propKey)) return spooledFile.getStringAttribute(PrintObject.ATTR_OUTPTY);
			if (P_ID_COPIES.equals(propKey)) return spooledFile.getIntegerAttribute(PrintObject.ATTR_COPIES).toString();
			if (P_ID_FORMTYPE.equals(propKey)) return spooledFile.getStringAttribute(PrintObject.ATTR_FORMTYPE);
			if (P_ID_DATE.equals(propKey)) return getCreateDate();
			if (P_ID_TIME.equals(propKey)) return getCreateTime();
		} catch (Exception e) {}
		return null;
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public void resetPropertyValue(Object id) {
	}

	public void setPropertyValue(Object id, Object value) {
	}
	
	public String getOutputQueue() {
		try {
			String outqdev = spooledFile.getStringAttribute(PrintObject.ATTR_OUTPUT_QUEUE);
			if (outqdev.endsWith(".OUTQ")) { //$NON-NLS-1$
				outqdev = outqdev.substring(10);
				int slash = outqdev.indexOf("/"); //$NON-NLS-1$
				String library = outqdev.substring(0, slash - 4);
				String outq = outqdev.substring(slash + 1);
				return library + "/" + outq.substring(0, outq.indexOf(".OUTQ")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return outqdev;
		} catch (Exception e) {}
		return ""; //$NON-NLS-1$
	}
	
	public String getCreateDate() {
		try {
			String date = spooledFile.getStringAttribute(PrintObject.ATTR_DATE);
			if (date.length() == 7)
				date = date.substring(3, 5) + "/" + date.substring(5, 7) + "/" + date.substring(1, 3); //$NON-NLS-1$ //$NON-NLS-2$
			return  date;
		} catch (Exception e) {
			return ""; //$NON-NLS-1$
		}
	}
	
	public String getCreateTime() {
		try {
			String time = spooledFile.getStringAttribute(PrintObject.ATTR_TIME);
			if (time.length() == 6)
				time = time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6); //$NON-NLS-1$ //$NON-NLS-2$
			return time;
		} catch (Exception e) {
			return ""; //$NON-NLS-1$
		}
	}
	
	private static List getDescriptors() {
		return descriptors;
	}

	/**
	 * Returns the spooledFile.
	 * @return SpooledFile
	 */
	public SpooledFile getSpooledFile() {
		return spooledFile;
	}

	/**
	 * Sets the spooledFile.
	 * @param spooledFile The spooledFile to set
	 */
	public void setSpooledFile(SpooledFile spooledFile) {
		this.spooledFile = spooledFile;
	}

}
