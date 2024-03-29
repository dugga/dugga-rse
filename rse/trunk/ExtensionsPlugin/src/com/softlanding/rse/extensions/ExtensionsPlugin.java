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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.rse.ui.SystemBasePlugin;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;

import com.softlanding.rse.extensions.messages.QueuedMessageAdapterFactory;
import com.softlanding.rse.extensions.messages.QueuedMessageResource;
import com.softlanding.rse.extensions.spooledfiles.ISaveToFile;
import com.softlanding.rse.extensions.spooledfiles.SaveToFileDelegate;
import com.softlanding.rse.extensions.subsystems.spooledfiles.SpooledFileAdapterFactory;
import com.softlanding.rse.extensions.subsystems.spooledfiles.SpooledFileResource;
import com.softlanding.rse.extensions.subsystems.spooledfiles.SpooledFileSubSystemConfigurationAdapterFactory;
import com.softlanding.rse.extensions.messages.QueuedMessageSubSystemConfigurationAdapterFactory;

/**
 * The main plugin class to be used in the desktop.
 */
public class ExtensionsPlugin extends SystemBasePlugin {
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
	private static final String ID = "com.softlanding.rse.extensions";
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
	
	@Override
	public ImageDescriptor getImageDescriptor(String name) {
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
		
		SpooledFileSubSystemConfigurationAdapterFactory spooledFileSubSystemConfigurationAdapterFactory = new SpooledFileSubSystemConfigurationAdapterFactory();
		spooledFileSubSystemConfigurationAdapterFactory.registerWithManager(manager);
        QueuedMessageSubSystemConfigurationAdapterFactory queuedMessageSubSystemConfigurationAdapterFactory = new QueuedMessageSubSystemConfigurationAdapterFactory();
        queuedMessageSubSystemConfigurationAdapterFactory.registerWithManager(manager);

	}
	
	protected void initializeImageRegistry() {
		String path = getIconPath();
		putImageInRegistry("IMAGE_REFRESH", path + IMAGE_REFRESH);
		putImageInRegistry("IMAGE_SPOOLED_FILE", path + IMAGE_SPOOLED_FILE);
		putImageInRegistry("IMAGE_SPOOLED_FILES", path + IMAGE_SPOOLED_FILES);
		putImageInRegistry("IMAGE_SPOOLED_FILE_FILTER", path + IMAGE_SPOOLED_FILE_FILTER);
		putImageInRegistry("IMAGE_MESSAGE", path + IMAGE_MESSAGE);
		putImageInRegistry("IMAGE_MESSAGES", path + IMAGE_MESSAGES);
		putImageInRegistry("IMAGE_MESSAGES_CONNECTED", path + IMAGE_MESSAGES_CONNECTED);
		putImageInRegistry("IMAGE_MESSAGE_FILTER", path + IMAGE_MESSAGE_FILTER);
		putImageInRegistry("IMAGE_INQUIRY", path + IMAGE_INQUIRY);
		putImageInRegistry("IMAGE_ERROR", path + IMAGE_ERROR);
		putImageInRegistry("IMAGE_WARNING", path + IMAGE_WARNING);
	}
	/**
	 * Convenience method for logging statuses to the plugin log
	 * 
	 * @param status  the status to log
	 */
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}
	
	public static void log(String msg) {
		getDefault().getLog().log(new Status(IStatus.INFO, ExtensionsPlugin.ID, 0, msg, null));
	}	

	public static void log(Exception e) {
		String msg = e.getMessage();
		if (msg == null || msg.trim().length() == 0)
			msg = ExtensionsPlugin.getResourceString("pluginError.internal");
		ExtensionsPlugin.log(msg,e);
	}

	public static void log(String msg, Exception e) {
		getDefault().getLog().log(new Status(IStatus.ERROR, ExtensionsPlugin.ID, 0, msg, e)); //$NON-NLS-1$
        MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("pluginError"), msg + ExtensionsPlugin.getResourceString("pluginError.log")); //$NON-NLS-1$
	}

}