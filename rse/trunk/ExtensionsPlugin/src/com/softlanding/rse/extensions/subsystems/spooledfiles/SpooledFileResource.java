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
package com.softlanding.rse.extensions.subsystems.spooledfiles;

import com.ibm.as400.access.SpooledFile;
import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.impl.AbstractResource;

public class SpooledFileResource extends AbstractResource {
	private SpooledFile spooledFile;

	public SpooledFileResource(SubSystem subSystem) {
		super(subSystem);
	}

	public SpooledFileResource() {
		super();
	}

	public SpooledFile getSpooledFile() {
		return spooledFile;
	}

	public void setSpooledFile(SpooledFile spooledFile) {
		this.spooledFile = spooledFile;
	}

}
