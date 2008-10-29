/*******************************************************************************
 * Copyright (c) 2005-2006 SoftLanding Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     SoftLanding - initial API and implementation
 *******************************************************************************/
package com.softlanding.rse.extensions;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

/**
 * Preference Initializer. Called at startup by Eclipse to initialize any
 * default preferences.
 * 
 * @author markphip
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public PreferenceInitializer() {
		super();
	}

	public void initializeDefaultPreferences() {
		ExtensionsPlugin.getDefault().getPluginPreferences().setDefault(
				ExtensionsPlugin.PREFERENCE_COMPARE_MERGE_WARNING,
				ExtensionsPlugin.DEFAULT_COMPARE_MERGE_WARNING);
	}

}
