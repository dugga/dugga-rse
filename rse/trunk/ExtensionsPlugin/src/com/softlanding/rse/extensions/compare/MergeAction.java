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
package com.softlanding.rse.extensions.compare;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rse.ui.RSEUIPlugin;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.ibm.etools.iseries.rse.ui.actions.popupmenu.ISeriesAbstractQSYSPopupMenuAction;
import com.ibm.etools.iseries.rse.ui.resources.QSYSEditableRemoteSourceFileMember;
import com.ibm.etools.iseries.services.qsys.api.IQSYSMember;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class MergeAction extends ISeriesAbstractQSYSPopupMenuAction {

    public MergeAction() {
        super();
    }

    public void run() {
        IQSYSMember[] members = getSelectedMembers();
        for (int i = 0; i < members.length; i++) {
            final MergeDialog dialog = new MergeDialog(getShell(), members[i]);
            if (dialog.open() == MergeDialog.CANCEL) break;
            BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                public void run() {
                    try {
                        IQSYSMember maintenanceMember = dialog.getMaintenanceConnection().getMember(
                        		dialog.getMaintenanceLibrary(), 
                        		dialog.getMaintenanceFile(), 
                        		dialog.getMaintenanceMember(),
                        		null);
                        if (maintenanceMember == null || !maintenanceMember.exists()) {
                			MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("MergeAction.0"), //$NON-NLS-1$
                					ExtensionsPlugin.getString("MergeAction.1",                           //$NON-NLS-1$
                							new Object[] {dialog.getMaintenanceMember(), dialog.getMaintenanceFile(), dialog.getMaintenanceLibrary()}));  
                            return;
                        }
                        IQSYSMember rootMember = dialog.getRootConnection().getMember(
                        		dialog.getRootLibrary(), 
                        		dialog.getRootFile(), 
                        		dialog.getRootMember(),
                        		null);
                        if (rootMember == null || !rootMember.exists()) {
                			MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("MergeAction.0"), //$NON-NLS-1$
                					ExtensionsPlugin.getString("MergeAction.1",                           //$NON-NLS-1$
                							new Object[] {dialog.getRootMember(), dialog.getRootFile(), dialog.getRootLibrary()}));  
                            return;
                        } 
                        QSYSEditableRemoteSourceFileMember left = new QSYSEditableRemoteSourceFileMember(dialog.getTargetMember());
                        IEditorPart editor = findMemberInEditor(left);
                        if (editor != null) {
                            IEditorInput editorInput = editor.getEditorInput();
                            MessageDialog.openError(Display.getCurrent().getActiveShell(), ExtensionsPlugin.getResourceString("MergeAction.0"), ExtensionsPlugin.getResourceString("MergeAction.2")); //$NON-NLS-1$ //$NON-NLS-2$
            				return;
                        }
                        CompareConfiguration cc = new CompareConfiguration();
                		final MergeCompareInput fInput = new MergeCompareInput(cc, rootMember, dialog.getTargetMember(), maintenanceMember);
                		fInput.initializeCompareConfiguration();
                		ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(new IPartListener() {
                            public void partClosed(IWorkbenchPart part) {
                                if (part instanceof EditorPart) {
                                    EditorPart editorPart = (EditorPart)part;
                                    if (editorPart.getEditorInput() == fInput) {
                                        fInput.removeIgnoreFile();
                                        ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(this);
                                    }
                                }
                            }                            
                		    public void partActivated(IWorkbenchPart part) {}
                            public void partBroughtToTop(IWorkbenchPart part) {}
                            public void partDeactivated(IWorkbenchPart part) {}
                            public void partOpened(IWorkbenchPart part) {}
                		});
                		CompareUI.openCompareEditorOnPage(fInput, ExtensionsPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage());
                		if (!editorOpen(fInput)) fInput.removeIgnoreFile();                		
                		fInput.cleanup();
                    } catch (Exception e) { e.printStackTrace(); }
                }               
            });
        }
    }
    
    private boolean editorOpen(MergeCompareInput input) {
		IWorkbench workbench= PlatformUI.getWorkbench();
		IWorkbenchWindow[] windows= workbench.getWorkbenchWindows();
		for (int i= 0; i < windows.length; i++) {
		    IWorkbenchPage[] pages= windows[i].getPages();
		    for (int x= 0; x < pages.length; x++) {
		        IEditorPart[] editors = pages[x].getEditors();
		        for (int z= 0; z < editors.length; z++) {
		            if (editors[z].getEditorInput() == input) return true;
		        }
		    }
		}
        return false;
    }    
    
    protected static IEditorPart findMemberInEditor(QSYSEditableRemoteSourceFileMember left) {
        IFile member = (IFile)left.getLocalResource();
        IWorkbench workbench= RSEUIPlugin.getDefault().getWorkbench(); 
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
                            QSYSEditableRemoteSourceFileMember editorMember = ((MergeCompareInput)editorInput).getLeft();
                            if (editorMember.getLocalResource().equals(left.getLocalResource())) return editor;
                        }
                        if (editorInput instanceof MemberCompareInput) {
                            if (((MemberCompareInput)editorInput).getCompareConfiguration().isLeftEditable()) {
                                QSYSEditableRemoteSourceFileMember editorMember = ((MemberCompareInput)editorInput).getLeft();
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
