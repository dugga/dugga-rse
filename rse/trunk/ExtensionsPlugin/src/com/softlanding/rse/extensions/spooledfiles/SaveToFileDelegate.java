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

public class SaveToFileDelegate {
	private ISaveToFile saveToFile;
	private String name;
	private String fileExtension;
	private String printerDeviceType;

	public SaveToFileDelegate() {
		super();
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public String getName() {
		return name;
	}

	public ISaveToFile getSaveToFile() {
		return saveToFile;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSaveToFile(ISaveToFile saveToFile) {
		this.saveToFile = saveToFile;
	}

	public String getPrinterDeviceType() {
		return printerDeviceType;
	}

	public void setPrinterDeviceType(String printerDeviceType) {
		this.printerDeviceType = printerDeviceType;
	}

}
