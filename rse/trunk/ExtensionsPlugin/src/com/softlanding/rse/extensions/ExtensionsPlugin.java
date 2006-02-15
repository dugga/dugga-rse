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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.softlanding.rse.extensions.messages.QueuedMessageAdapterFactory;
import com.softlanding.rse.extensions.messages.QueuedMessageResource;
import com.softlanding.rse.extensions.spooledfiles.ISaveToFile;
import com.softlanding.rse.extensions.spooledfiles.SaveToFileDelegate;
import com.softlanding.rse.extensions.subsystems.spooledfiles.SpooledFileAdapterFactory;
import com.softlanding.rse.extensions.subsystems.spooledfiles.SpooledFileResource;

/**
 * The main plugin class to be used in the desktop.
 */
public class ExtensionsPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static ExtensionsPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	private static URL installURL;
	private static SaveToFileDelegate[] saveToFileDelegates;
	
	public static final String IMAGE_REFRESH = "refresh.gif"; //$NON-NLS-1$
	public static final String IMAGE_SPOOLED_FILE = "spooled_file.gif"; //$NON-NLS-1$
	public static final String IMAGE_SPOOLED_FILES = "spooled_files.gif"; //$NON-NLS-1$
	public static final String IMAGE_SPOOLED_FILE_FILTER = "spooled_file_filter.gif"; //$NON-NLS-1$
	public static final String IMAGE_MESSAGE = "message.gif"; //$NON-NLS-1$
	public static final String IMAGE_MESSAGES_CONNECTED = "messages_connected.gif"; //$NON-NLS-1$
	public static final String IMAGE_MESSAGES = "messages.gif"; //$NON-NLS-1$
	public static final String IMAGE_MESSAGE_FILTER = "message_filter.gif"; //$NON-NLS-1$
	public static final String IMAGE_INQUIRY = "inquiry.gif"; //$NON-NLS-1$
	public static final String IMAGE_ERROR = "error.gif"; //$NON-NLS-1$
	public static final String IMAGE_WARNING = "warning.gif"; //$NON-NLS-1$
	
	public static final String PREFERENCE_COMPARE_MERGE_WARNING = "com.softlanding.rse.extensions.compareMergeWarning"; //$NON-NLS-1$
	public static final boolean DEFAULT_COMPARE_MERGE_WARNING = true;
	/**
	 * The constructor.
	 */
	public ExtensionsPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("com.softlanding.rse.extensions.Resources"); //$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		ExtensionsPlugin.installURL = context.getBundle().getEntry("/"); //$NON-NLS-1$
		setupAdapters();	
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static ExtensionsPlugin getDefault() {
		return plugin;
	}
	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public static String getString(String key, Object[] messageArguments) {
		MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
		formatter.applyPattern(getResourceString(key));
		return formatter.format(messageArguments);
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ExtensionsPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	public static ImageDescriptor getImageDescriptor(String name) {
		String iconPath = "icons/"; //$NON-NLS-1$
		try {
			URL url = new URL(installURL, iconPath + name);
			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			// should not happen
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}
	
	public static String getTempProject() {
		return "spooledFiles"; //$NON-NLS-1$
	}
	
	public static SaveToFileDelegate[] getSaveToFileDelegates() throws Exception {
		if (saveToFileDelegates == null) {
			ArrayList delegates = new ArrayList();
			IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
			IConfigurationElement[] configurationElements = pluginRegistry.getConfigurationElementsFor("com.softlanding.rse.extensions.save"); //$NON-NLS-1$
			for (int i = 0; i < configurationElements.length; i++) {
				IConfigurationElement configurationElement = configurationElements[i];
				SaveToFileDelegate delegate = new SaveToFileDelegate();
				delegate.setFileExtension(configurationElement.getAttribute("file_extension")); //$NON-NLS-1$
				delegate.setName(configurationElement.getAttribute("name")); //$NON-NLS-1$
				delegate.setPrinterDeviceType(configurationElement.getAttribute("prtdevtypefilter")); //$NON-NLS-1$
				ISaveToFile saveToFile = (ISaveToFile)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
				delegate.setSaveToFile(saveToFile);
				delegates.add(delegate);
			}
			saveToFileDelegates = new SaveToFileDelegate[delegates.size()];
			delegates.toArray(saveToFileDelegates);
		}
		return saveToFileDelegates;
	}
	
	public static SaveToFileDelegate[] getSaveToFileDelegates(String prtDevType) throws Exception {
		SaveToFileDelegate[] allDelegates = getSaveToFileDelegates();
		ArrayList delegates = new ArrayList();
		for (int i = 0; i < allDelegates.length; i++) {
			if ((allDelegates[i].getPrinterDeviceType() == null) || (allDelegates[i].getPrinterDeviceType().equals(prtDevType)))
				delegates.add(allDelegates[i]);
		}
		SaveToFileDelegate[] prtDevTypeDelegates = new SaveToFileDelegate[delegates.size()];
		delegates.toArray(prtDevTypeDelegates);
		return prtDevTypeDelegates;
	}	
	
	private void setupAdapters() {
		IAdapterManager manager = Platform.getAdapterManager();
		SpooledFileAdapterFactory spooledFactory = new SpooledFileAdapterFactory();
		manager.registerAdapters(spooledFactory, SpooledFileResource.class);
		QueuedMessageAdapterFactory messagesFactory = new QueuedMessageAdapterFactory();
		manager.registerAdapters(messagesFactory, QueuedMessageResource.class);
	}
	
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(IMAGE_REFRESH, getImageDescriptor(IMAGE_REFRESH));
		reg.put(IMAGE_SPOOLED_FILE, getImageDescriptor(IMAGE_SPOOLED_FILE));
		reg.put(IMAGE_SPOOLED_FILES, getImageDescriptor(IMAGE_SPOOLED_FILES));
		reg.put(IMAGE_SPOOLED_FILE_FILTER, getImageDescriptor(IMAGE_SPOOLED_FILE_FILTER));
		reg.put(IMAGE_MESSAGE, getImageDescriptor(IMAGE_MESSAGE));
		reg.put(IMAGE_MESSAGES, getImageDescriptor(IMAGE_MESSAGES));
		reg.put(IMAGE_MESSAGES_CONNECTED, getImageDescriptor(IMAGE_MESSAGES_CONNECTED));
		reg.put(IMAGE_MESSAGE_FILTER, getImageDescriptor(IMAGE_MESSAGE_FILTER));
		reg.put(IMAGE_INQUIRY, getImageDescriptor(IMAGE_INQUIRY));
		reg.put(IMAGE_ERROR, getImageDescriptor(IMAGE_ERROR));
		reg.put(IMAGE_WARNING, getImageDescriptor(IMAGE_WARNING));
	}

}
