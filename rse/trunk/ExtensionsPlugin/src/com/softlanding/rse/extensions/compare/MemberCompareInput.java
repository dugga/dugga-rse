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

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.IContentChangeListener;
import org.eclipse.compare.IContentChangeNotifier;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.DiffTreeViewer;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IStructureComparator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IFileEditorInput;

import com.ibm.etools.iseries.rse.ui.resources.QSYSEditableRemoteSourceFileMember;
import com.ibm.etools.iseries.rse.ui.resources.QSYSTempFileListener;
import com.ibm.etools.iseries.services.qsys.api.IQSYSMember;
import com.softlanding.rse.extensions.ExtensionsPlugin;

public class MemberCompareInput extends CompareEditorInput implements IFileEditorInput {
	private boolean fThreeWay = false;
	private Object fRoot;
	private IStructureComparator fAncestor;
	private MemberNode fLeft;
	private MemberNode fRight;
	private IQSYSMember leftMember;
	private IQSYSMember rightMember;
	private IResource fLeftResource;
	private IResource fRightResource;
	private DiffTreeViewer fDiffViewer;
	private boolean neverSaved = true;
	private boolean isSaving = false;
	private QSYSEditableRemoteSourceFileMember left;

	public MemberCompareInput(CompareConfiguration config, IQSYSMember leftMember, IQSYSMember rightMember) {
		super(config);
		this.leftMember = leftMember;
		this.rightMember = rightMember;
	}
	
	protected Object prepareInput(IProgressMonitor monitor)
		throws InvocationTargetException, InterruptedException {
			try {
				left = new QSYSEditableRemoteSourceFileMember(leftMember);
				QSYSEditableRemoteSourceFileMember right = new QSYSEditableRemoteSourceFileMember(rightMember);
				left.download(monitor);
				
				if (getCompareConfiguration().isLeftEditable())
				    left.openStream();
				
				right.download(monitor);
				fLeftResource = left.getLocalResource();
				fRightResource = right.getLocalResource();
				fLeftResource.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				fRightResource.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				monitor.beginTask(ExtensionsPlugin.getResourceString("MemberCompareInput.0"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
				String leftLabel = fLeftResource.getName();
				String rightLabel = fRightResource.getName();
				setTitle(ExtensionsPlugin.getResourceString("MemberCompareInput.1") + leftMember.getAbsoluteName() + ExtensionsPlugin.getResourceString("MemberCompareInput.2") + rightMember.getAbsoluteName()); //$NON-NLS-1$ //$NON-NLS-2$
				MemberDifferencer d = new MemberDifferencer(12) {
	                protected Object visit(Object data, int result, Object ancestor, Object left, Object right) {
	                    return new MyDiffNode((IDiffContainer) data, result, (ITypedElement)ancestor, (ITypedElement)left, (ITypedElement)right);
	                }				    
				};
				fAncestor = null;
				fLeft = getStructure(fLeftResource);
				fRight = getStructure(fRightResource);
	            fLeft.addContentChangeListener( new IContentChangeListener() {            
	                public void contentChanged(IContentChangeNotifier source) {
	                    if (!isSaving) {
	                        try {                    
	                            saveChanges(new NullProgressMonitor());
	                        } catch (CoreException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }            
	            });
				fRoot = d.findDifferences(fThreeWay, monitor, null, fAncestor, fLeft, fRight);
				QSYSTempFileListener.getListener().addIgnoreFile((IFile)fLeftResource);
				return fRoot;
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				monitor.done();
			}
		return null;
	}
	
	public void initializeCompareConfiguration() {
		CompareConfiguration cc= getCompareConfiguration();
		if (fLeftResource != null) {
			cc.setLeftLabel(buildLabel(fLeftResource));
			//cc.setLeftImage(CompareUIPlugin.getImage(fLeftResource));
		}
		if (fRightResource != null) {
			cc.setRightLabel(buildLabel(fRightResource));
			//cc.setRightImage(CompareUIPlugin.getImage(fRightResource));
		}
		cc.setProperty(CompareConfiguration.IGNORE_WHITESPACE, new Boolean(true));
	}
	
	public void cleanup() {
		if (fLeft != null) {
			File leftTemp = fLeft.getTempFile();
			if (leftTemp != null) {
				try {
					leftTemp.delete();
				} catch (Exception e) {
				}
			}
		}
		if (fRight != null) {
			File rightTemp = fRight.getTempFile();
			if (rightTemp != null) {
				try {
					rightTemp.delete();
				} catch (Exception e) {
				}
			}
		}
	}
	
	private MemberNode getStructure(IResource input) {
		MemberNode memberNode = new MemberNode(input, 12);
		return memberNode;
	}
	
	private String buildLabel(IResource r) {
		String n = r.getFullPath().toString();
		if (n.charAt(0) == IPath.SEPARATOR)
			return n.substring(1);
		return n;
	}
	
    public void saveChanges(IProgressMonitor pm) throws CoreException {
        isSaving = true;
        super.saveChanges(pm);
        try {
            fLeft.commit(pm);
        } catch (Exception e) { }
        neverSaved = false;
        try {
            left.upload(pm);
        } catch (Exception e) {
        	ExtensionsPlugin.log(e);
        }
        ((MyDiffNode)fRoot).fireChange();
        isSaving = false;
    }
    
    public void removeIgnoreFile() {
        QSYSTempFileListener.getListener().removeIgnoreFile((IFile)fLeftResource);
        try {
            left.closeStream();
        } catch (Exception e) {
        	ExtensionsPlugin.log(e);
        }
    }
    
    public boolean isSaveNeeded() {
        if (!getCompareConfiguration().isLeftEditable()) return false;
        return super.isSaveNeeded();
    }
    
    public QSYSEditableRemoteSourceFileMember getLeft() {
        return left;
    }
    
    public static class MyDiffNode extends DiffNode {
        public MyDiffNode(IDiffContainer parent, int kind, ITypedElement ancestor, ITypedElement left, ITypedElement right) {
            super(parent, kind, ancestor, left, right);
        }
         
        public void fireChange() {
            super.fireChange();
        }
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IFileEditorInput#getFile()
	 */
	public IFile getFile() {
		return left.getLocalResource();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStorageEditorInput#getStorage()
	 */
	public IStorage getStorage() throws CoreException {
		return left.getLocalResource();
	}

}
