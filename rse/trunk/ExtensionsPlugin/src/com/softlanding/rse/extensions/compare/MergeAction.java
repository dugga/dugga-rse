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
package com.softlanding.rse.extensions.compare;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.FileEditorInput;

import com.ibm.etools.iseries.core.ISeriesSystemPlugin;
import com.ibm.etools.iseries.core.api.ISeriesMember;
import com.ibm.etools.iseries.core.resources.ISeriesEditableSrcPhysicalFileMember;
import com.ibm.etools.iseries.core.ui.actions.isv.ISeriesAbstractQSYSPopupMenuExtensionAction;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class MergeAction extends ISeriesAbstractQSYSPopupMenuExtensionAction {

    public MergeAction() {
        super();
    }

    public void run() {
        ISeriesMember[] members = getSelectedMembers();
        for (int i = 0; i < members.length; i++) {
            final MergeDialog dialog = new MergeDialog(getShell(), members[i]);
            if (dialog.open() == MergeDialog.CANCEL) break;
            BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                public void run() {
                    try {
                        ISeriesMember maintenanceMember = dialog.getMaintenanceConnection().getISeriesMember(getShell(), dialog.getMaintenanceLibrary(), dialog.getMaintenanceFile(), dialog.getMaintenanceMember());
                        if (maintenanceMember == null || !maintenanceMember.exists()) {
                			MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("MergeAction.0"), //$NON-NLS-1$
                					ExtensionsPlugin.getString("MergeAction.1",                           //$NON-NLS-1$
                							new Object[] {dialog.getMaintenanceMember(), dialog.getMaintenanceFile(), dialog.getMaintenanceLibrary()}));  
                            return;
                        }
                        ISeriesMember rootMember = dialog.getRootConnection().getISeriesMember(getShell(), dialog.getRootLibrary(), dialog.getRootFile(), dialog.getRootMember());
                        if (rootMember == null || !rootMember.exists()) {
                			MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("MergeAction.0"), //$NON-NLS-1$
                					ExtensionsPlugin.getString("MergeAction.1",                           //$NON-NLS-1$
                							new Object[] {dialog.getRootMember(), dialog.getRootFile(), dialog.getRootLibrary()}));  
                            return;
                        } 
                        ISeriesEditableSrcPhysicalFileMember left = new ISeriesEditableSrcPhysicalFileMember(dialog.getTargetMember());
                        IEditorPart editor = findMemberInEditor(left);
                        if (editor != null) {
                            IEditorInput editorInput = editor.getEditorInput();
                            MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("MergeAction.0"), ExtensionsPlugin.getResourceString("MergeAction.2")); //$NON-NLS-1$ //$NON-NLS-2$
            				return;
                        }
                        CompareConfiguration cc = new CompareConfiguration();
                		MergeCompareInput fInput = new MergeCompareInput(cc, rootMember, dialog.getTargetMember(), maintenanceMember);
                		fInput.initializeCompareConfiguration();
                		CompareUI.openCompareEditorOnPage(fInput, ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage());
                		fInput.cleanup();
                		fInput = null;
                    } catch (Exception e) { e.printStackTrace(); }
                }               
            });
        }
    }
    
    protected static IEditorPart findMemberInEditor(ISeriesEditableSrcPhysicalFileMember left) {
        IFile member = (IFile)left.getLocalResource();
        IWorkbench workbench= ISeriesSystemPlugin.getDefault().getWorkbench(); 
        IWorkbenchWindow[] windows= workbench.getWorkbenchWindows(); 
        IWorkbenchPage[] pages; 
        IEditorReference[] editorRefs; 
        IEditorPart editor; 
        IEditorInput editorInput; 
        for (int i= 0; i < windows.length; i++) { 
            pages= windows[i].getPages(); 
            for (int x= 0; x < pages.length; x++) { 
                editorRefs= pages[x].getEditorReferences(); 
                for (int refsIdx = 0; refsIdx < editorRefs.length; refsIdx++) { 
                    editor = editorRefs[refsIdx].getEditor(false); 
                    if (editor != null) { 
                        editorInput = editor.getEditorInput(); 
                        if (editorInput instanceof FileEditorInput) { 
                            if (((FileEditorInput) editorInput).getFile().equals(member)) { 
                                return editor; 
                            } 
                        } 
                        if (editorInput instanceof MergeCompareInput) {
                            ISeriesEditableSrcPhysicalFileMember editorMember = ((MergeCompareInput)editorInput).getLeft();
                            if (editorMember.getLocalResource().equals(left.getLocalResource())) return editor;
                        }
                        if (editorInput instanceof MemberCompareInput) {
                            if (((MemberCompareInput)editorInput).getCompareConfiguration().isLeftEditable()) {
                                ISeriesEditableSrcPhysicalFileMember editorMember = ((MemberCompareInput)editorInput).getLeft();
                                if (editorMember.getLocalResource().equals(left.getLocalResource())) return editor;                                
                            }
                        }
                    } 
                }                                                 
            } 
        } 
        return null;         
    } 

}
