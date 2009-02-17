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
package com.softlanding.rse.extensions.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

public class EditingPerspectiveFactory implements IPerspectiveFactory {
	public final static String EDITING_PERSPECTIVE_ID = "com.softlanding.rse.extensions.perspective.EditingPerspective"; //$NON-NLS-1$

	public EditingPerspectiveFactory() {
		super();
	}

	public void createInitialLayout(IPageLayout layout)  {
		layout.setEditorAreaVisible(true);
		String editorArea = layout.getEditorArea();
		layout.addFastView("org.eclipse.ui.views.ContentOutline"); //$NON-NLS-1$
		layout.addFastView("org.eclipse.rse.ui.view.systemView"); //$NON-NLS-1$
		layout.addFastView("com.ibm.etools.iseries.rse.ui.ObjectTableView", (float) 0.60); //$NON-NLS-1$
		layout.addFastView("com.ibm.etools.iseries.rse.ui.view.cmdlog", (float) 0.60); //$NON-NLS-1$
		layout.addFastView("com.ibm.etools.iseries.rse.ui.ErrorListView", (float) 0.60); //$NON-NLS-1$
		layout.addFastView("com.ibm.etools.systems.as400.debug.ui.sbreak.view.PhantomServiceEntryPointTableView", (float) 0.60); //$NON-NLS-1$
		IPlaceholderFolderLayout bottom = layout.createPlaceholderFolder("editing.bottom", IPageLayout.BOTTOM,  (float) 0.60, editorArea); //$NON-NLS-1$
		bottom.addPlaceholder("com.ibm.etools.iseries.edit.views.iserieseditorrpgindentview"); //$NON-NLS-1$
		bottom.addPlaceholder("com.ibm.etools.iseries.edit.views.iserieseditorpromptpagebookview"); //$NON-NLS-1$
		bottom.addPlaceholder("org.eclipse.rse.ui.view.SystemSearchView"); //$NON-NLS-1$
		layout.addShowViewShortcut("org.eclipse.ui.views.ContentOutline"); //$NON-NLS-1$
		layout.addShowViewShortcut("org.eclipse.rse.ui.view.systemView"); //$NON-NLS-1$
		layout.addShowViewShortcut("com.ibm.etools.iseries.rse.ui.ObjectTableView"); //$NON-NLS-1$
		layout.addShowViewShortcut("com.ibm.etools.iseries.rse.ui.ErrorListView"); //$NON-NLS-1$
		layout.addShowViewShortcut("com.ibm.etools.iseries.edit.views.iserieseditorrpgindentview"); //$NON-NLS-1$
		layout.addShowViewShortcut("com.ibm.etools.iseries.rse.ui.view.cmdlog"); //$NON-NLS-1$
		layout.addShowViewShortcut("com.ibm.etools.iseries.edit.views.iserieseditorpromptpagebookview"); //$NON-NLS-1$
		layout.addNewWizardShortcut("com.ibm.etools.iseries.rse.ui.wizard.remote.newlibrary"); //$NON-NLS-1$
		layout.addNewWizardShortcut("com.ibm.etools.iseries.rse.ui.wizard.remote.newsrcpf"); //$NON-NLS-1$
		layout.addNewWizardShortcut("com.ibm.etools.iseries.rse.ui.wizard.remote.newmember"); //$NON-NLS-1$
		layout.addNewWizardShortcut("com.ibm.etools.iseries.rse.ui.wizard.remote.newmsgf"); //$NON-NLS-1$
		layout.addNewWizardShortcut("com.ibm.etools.iseries.rse.ui.wizard.remote.newdtaq"); //$NON-NLS-1$
		layout.addNewWizardShortcut("com.ibm.etools.iseries.rse.ui.wizard.remote.newdtaara"); //$NON-NLS-1$
	}
}
