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
package com.softlanding.rse.extensions.subsystems.spooledfiles;

import org.eclipse.rse.core.subsystems.*;
import com.ibm.as400.access.SpooledFile;

public class SpooledFileResource extends AbstractResource {
	private SpooledFile spooledFile;

	public SpooledFileResource(ISubSystem parentSubSystem) {
		super(parentSubSystem);
	}

	public SpooledFileResource() {
		super();
	}

	public SpooledFileResource(SpooledFileSubSystem spooledFileSubSystem) {
		super(spooledFileSubSystem);
	}

	public SpooledFile getSpooledFile() {
		return spooledFile;
	}

	public void setSpooledFile(SpooledFile spooledFile) {
		this.spooledFile = spooledFile;
	}

}
