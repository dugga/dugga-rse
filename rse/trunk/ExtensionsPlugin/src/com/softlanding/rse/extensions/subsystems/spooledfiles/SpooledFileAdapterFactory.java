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

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.rse.ui.view.AbstractSystemRemoteAdapterFactory;
import org.eclipse.rse.ui.view.ISystemViewElementAdapter;
import org.eclipse.ui.views.properties.IPropertySource;

public class SpooledFileAdapterFactory	extends AbstractSystemRemoteAdapterFactory
	implements IAdapterFactory {
		
	private SpooledFileResourceAdapter spooledFileAdapter = new SpooledFileResourceAdapter();

	public SpooledFileAdapterFactory() {
		super();
	}

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		ISystemViewElementAdapter adapter = null;
		if (adaptableObject instanceof SpooledFileResource)
		  adapter = spooledFileAdapter;
		if ((adapter != null) && (adapterType == IPropertySource.class))
		  adapter.setPropertySourceInput(adaptableObject);
		return adapter;

	}

}
