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

public class SpooledFileFilter {
	private String description;
	private String endDateFilter;
	private String endTimeFilter;
	private String formTypeFilter;
	private String jobSystemFilter;
	private String queueFilter;
	private String queueLibraryFilter;
	private String startDateFilter;
	private String startTimeFilter;
	private String userDataFilter;
	private String userFilter;
	private String jobFilter;
	private String jobNumberFilter;
	private String[] jobNames;

	public SpooledFileFilter() {
		super();
	}
	
	public SpooledFileFilter(String filterString) {
		this();
		setFilters(filterString);
	}

	/**
	 * Returns the endDateFilter.
	 * @return String
	 */
	public String getEndDateFilter() {
		return endDateFilter;
	}

	/**
	 * Returns the endTimeFilter.
	 * @return String
	 */
	public String getEndTimeFilter() {
		return endTimeFilter;
	}

	/**
	 * Returns the formTypeFilter.
	 * @return String
	 */
	public String getFormTypeFilter() {
		return formTypeFilter;
	}

	/**
	 * Returns the jobSystemFilter.
	 * @return String
	 */
	public String getJobSystemFilter() {
		return jobSystemFilter;
	}

	/**
	 * Returns the queueFilter.
	 * @return String
	 */
	public String getQueueFilter() {
		return queueFilter;
	}

	/**
	 * Returns the startDateFilter.
	 * @return String
	 */
	public String getStartDateFilter() {
		return startDateFilter;
	}

	/**
	 * Returns the startTimeFilter.
	 * @return String
	 */
	public String getStartTimeFilter() {
		return startTimeFilter;
	}

	/**
	 * Returns the userDataFilter.
	 * @return String
	 */
	public String getUserDataFilter() {
		return userDataFilter;
	}

	/**
	 * Returns the userFilter.
	 * @return String
	 */
	public String getUserFilter() {
		return userFilter;
	}

	/**
	 * Sets the endDateFilter.
	 * @param endDateFilter The endDateFilter to set
	 */
	public void setEndDateFilter(String endDateFilter) {
		this.endDateFilter = endDateFilter;
	}

	/**
	 * Sets the endTimeFilter.
	 * @param endTimeFilter The endTimeFilter to set
	 */
	public void setEndTimeFilter(String endTimeFilter) {
		this.endTimeFilter = endTimeFilter;
	}

	/**
	 * Sets the formTypeFilter.
	 * @param formTypeFilter The formTypeFilter to set
	 */
	public void setFormTypeFilter(String formTypeFilter) {
		this.formTypeFilter = formTypeFilter;
	}

	/**
	 * Sets the jobSystemFilter.
	 * @param jobSystemFilter The jobSystemFilter to set
	 */
	public void setJobSystemFilter(String jobSystemFilter) {
		this.jobSystemFilter = jobSystemFilter;
	}

	/**
	 * Sets the queueFilter.
	 * @param queueFilter The queueFilter to set
	 */
	public void setQueueFilter(String queueFilter) {
		this.queueFilter = queueFilter;
	}

	/**
	 * Sets the startDateFilter.
	 * @param startDateFilter The startDateFilter to set
	 */
	public void setStartDateFilter(String startDateFilter) {
		this.startDateFilter = startDateFilter;
	}

	/**
	 * Sets the startTimeFilter.
	 * @param startTimeFilter The startTimeFilter to set
	 */
	public void setStartTimeFilter(String startTimeFilter) {
		this.startTimeFilter = startTimeFilter;
	}

	/**
	 * Sets the userDataFilter.
	 * @param userDataFilter The userDataFilter to set
	 */
	public void setUserDataFilter(String userDataFilter) {
		this.userDataFilter = userDataFilter;
	}

	/**
	 * Sets the userFilter.
	 * @param userFilter The userFilter to set
	 */
	public void setUserFilter(String userFilter) {
		this.userFilter = userFilter; 
	}
	
	/**
	 * Returns the queueLibraryFilter.
	 * @return String
	 */
	public String getQueueLibraryFilter() {
		return queueLibraryFilter;
	}

	/**
	 * Sets the queueLibraryFilter.
	 * @param queueLibraryFilter The queueLibraryFilter to set
	 */
	public void setQueueLibraryFilter(String queueLibraryFilter) {
		this.queueLibraryFilter = queueLibraryFilter;
	}
	
	/**
	 * Returns the jobFilter.
	 * @return String
	 */
	public String getJobFilter() {
		return jobFilter;
	}

	/**
	 * Returns the jobNumberFilter.
	 * @return String
	 */
	public String getJobNumberFilter() {
		return jobNumberFilter;
	}

	/**
	 * Sets the jobFilter.
	 * @param jobFilter The jobFilter to set
	 */
	public void setJobFilter(String jobFilter) {
		this.jobFilter = jobFilter;
	}

	/**
	 * Sets the jobNumberFilter.
	 * @param jobNumberFilter The jobNumberFilter to set
	 */
	public void setJobNumberFilter(String jobNumberFilter) {
		this.jobNumberFilter = jobNumberFilter;
	}

	/**
	 * Returns the description.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * @param description The description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	

	/**
	 * Returns the jobNames.
	 * @return String[]
	 */
	public String[] getJobNames() {
		return jobNames;
	}

	/**
	 * Sets the jobNames.
	 * @param jobNames The jobNames to set
	 */
	public void setJobNames(String[] jobNames) {
		this.jobNames = jobNames;
	}

	
	public String getFilterString() {
		StringBuffer filterString = new StringBuffer();
		if (userFilter == null) filterString.append("*/"); //$NON-NLS-1$
		else filterString.append(userFilter + "/"); //$NON-NLS-1$
		if (queueFilter == null) filterString.append("*/"); //$NON-NLS-1$
		else filterString.append(queueFilter + "/"); //$NON-NLS-1$
		if (queueLibraryFilter == null) filterString.append("*/"); //$NON-NLS-1$
		else filterString.append(queueLibraryFilter + "/"); //$NON-NLS-1$
		if (userDataFilter == null) filterString.append("*/"); //$NON-NLS-1$
		else filterString.append(userDataFilter + "/"); //$NON-NLS-1$
		if (formTypeFilter == null) filterString.append("*/"); //$NON-NLS-1$
		else filterString.append(formTypeFilter + "/"); //$NON-NLS-1$
		if (startDateFilter == null) filterString.append("*/"); //$NON-NLS-1$
		else filterString.append(startDateFilter + "/"); //$NON-NLS-1$
		if (startTimeFilter == null) filterString.append("*/"); //$NON-NLS-1$
		else filterString.append(startTimeFilter + "/"); //$NON-NLS-1$
		if (endDateFilter == null) filterString.append("*/"); //$NON-NLS-1$
		else filterString.append(endDateFilter + "/"); //$NON-NLS-1$
		if (endTimeFilter == null) filterString.append("*"); //$NON-NLS-1$
		else filterString.append(endTimeFilter);
		return filterString.toString();
	}
	
	private void setFilters(String filterString) {
		int index;
		index = filterString.indexOf("/"); //$NON-NLS-1$
		String temp = filterString.substring(0, index);
		if (!temp.equals("*")) setUserFilter(temp); //$NON-NLS-1$
		String parseText = filterString.substring(index + 1);
		index = parseText.indexOf("/"); //$NON-NLS-1$
		temp = parseText.substring(0, index);
		if (!temp.equals("*")) setQueueFilter(temp); //$NON-NLS-1$
		parseText = parseText.substring(index + 1);
		index = parseText.indexOf("/"); //$NON-NLS-1$
		temp = parseText.substring(0, index);
		if (!temp.equals("*")) setQueueLibraryFilter(temp); //$NON-NLS-1$
		parseText = parseText.substring(index + 1);
		index = parseText.indexOf("/"); //$NON-NLS-1$
		temp = parseText.substring(0, index);
		if (!temp.equals("*")) setUserDataFilter(temp); //$NON-NLS-1$
		parseText = parseText.substring(index + 1);
		index = parseText.indexOf("/"); //$NON-NLS-1$
		temp = parseText.substring(0, index);
		if (!temp.equals("*")) setFormTypeFilter(temp); //$NON-NLS-1$
	}

}
