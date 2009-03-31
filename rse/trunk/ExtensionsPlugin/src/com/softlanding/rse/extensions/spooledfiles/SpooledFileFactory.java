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
package com.softlanding.rse.extensions.spooledfiles;

import java.util.ArrayList;
import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.access.SpooledFileList;

public class SpooledFileFactory {
	private AS400 as400;
	private static int[] attributes = 
		{ PrintObject.ATTR_JOBNAME,
		  PrintObject.ATTR_JOBNUMBER,
		  PrintObject.ATTR_JOBUSER,
		  PrintObject.ATTR_OUTPUT_QUEUE,
		  PrintObject.ATTR_SPLFNUM,
		  PrintObject.ATTR_SPLFSTATUS,
		  PrintObject.ATTR_SPOOLFILE,
		  PrintObject.ATTR_USERDATA,
		  PrintObject.ATTR_COPIES,
		  PrintObject.ATTR_CURPAGE,
		  PrintObject.ATTR_DATE,
		  PrintObject.ATTR_PAGES };
	
	public SpooledFileFactory(AS400 as400) {
		super();
		this.as400 = as400;
	}
	
	public SpooledFile[] getSpooledFiles(SpooledFileFilter filter) throws Exception {
		if (filter.getJobNames() != null) return getSpooledFiles(filter.getJobNames(), filter.getUserFilter());
		if (filter.getJobNumberFilter() != null) return getSpooledFiles(filter.getJobNumberFilter(), filter.getUserFilter(), filter.getJobFilter());
		SpooledFileList splfList = new SpooledFileList(as400);
//		splfList.setAttributesToRetrieve(attributes);
		if (filter.getEndDateFilter() != null) splfList.setEndDateFilter(filter.getEndDateFilter());
		if (filter.getEndTimeFilter() != null) splfList.setEndTimeFilter(filter.getEndTimeFilter());
		if (filter.getFormTypeFilter() != null) splfList.setFormTypeFilter(filter.getFormTypeFilter());
		if (filter.getJobSystemFilter() != null) splfList.setJobSystemFilter(filter.getJobSystemFilter());
		if (filter.getQueueFilter() != null) {
			String outqLib = filter.getQueueLibraryFilter();
			if (outqLib == null) outqLib = "%LIBL%"; //$NON-NLS-1$
			splfList.setQueueFilter("/QSYS.LIB/" + outqLib + ".LIB/" + filter.getQueueFilter() + ".OUTQ"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if (filter.getStartDateFilter() != null) splfList.setStartDateFilter(filter.getStartDateFilter());
		if (filter.getStartTimeFilter() != null) splfList.setStartTimeFilter(filter.getStartTimeFilter());
		if (filter.getUserDataFilter() != null) splfList.setUserDataFilter(filter.getUserDataFilter());
		if (filter.getUserFilter() != null) splfList.setUserFilter(filter.getUserFilter());
		else splfList.setUserFilter("*ALL"); //$NON-NLS-1$
		splfList.openSynchronously();
		Enumeration enumer = splfList.getObjects();
		ArrayList splfs = new ArrayList();
		while(enumer.hasMoreElements()) {
			SpooledFile splf = (SpooledFile)enumer.nextElement();
			if ( splf != null ) splfs.add(splf);
		}
		SpooledFile[] spooledFiles = new SpooledFile[splfs.size()];
		splfs.toArray(spooledFiles);
		splfList.close();
		return spooledFiles;
	}
	
	public SpooledFile[] getSpooledFiles(String[] jobNames, String user) throws Exception {
		SpooledFileFilter filter = new SpooledFileFilter();
		filter.setUserFilter(user);
		SpooledFile[] userFiles = getSpooledFiles(filter);
		ArrayList splfs = new ArrayList();
		for (int i = 0; i < userFiles.length; i++) {
			for (int j = 0; j < jobNames.length; j++) {
				if (userFiles[i].getJobName().equals(jobNames[j])) {
					splfs.add(userFiles[i]);
					break;
				}
			}
		}
		SpooledFile[] spooledFiles = new SpooledFile[splfs.size()];
		splfs.toArray(spooledFiles);
		return spooledFiles;
	}
	
	public SpooledFile[] getSpooledFiles(String user) throws Exception {
		SpooledFileFilter filter = new SpooledFileFilter();
		filter.setUserFilter(user);
		return getSpooledFiles(filter);
	}
	
	public SpooledFile[] getSpooledFiles(String outq, String outqLib) throws Exception {
		SpooledFileFilter filter = new SpooledFileFilter();
		filter.setQueueFilter(outq);
		filter.setQueueLibraryFilter(outqLib);
		filter.setUserFilter("*ALL"); //$NON-NLS-1$
		return getSpooledFiles(filter);
	}
	
	public SpooledFile[] getSpooledFiles(String jobnbr, String jobuser, String jobname) throws Exception {
		SpooledFileFilter filter = new SpooledFileFilter();
		filter.setUserFilter(jobuser);
		SpooledFile[] userFiles = getSpooledFiles(filter);
		ArrayList jobFiles = new ArrayList();
		for (int i = 0; i < userFiles.length; i++) {
			SpooledFile splf = userFiles[i];
			if (splf.getJobName().equals(jobname) && splf.getJobNumber().equals(jobnbr))
				jobFiles.add(splf);
		}
		SpooledFile[] splfArray = new SpooledFile[jobFiles.size()];
		jobFiles.toArray(splfArray);
		return splfArray;
	}	
	
}
