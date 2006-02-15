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

import java.io.*;
import java.util.*;

import org.eclipse.compare.*;
import org.eclipse.compare.structuremergeviewer.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.util.*;
import org.eclipse.swt.graphics.*;

public class MemberNode extends BufferedContent
			implements IStructureComparator, ITypedElement, IEditableContent, IModificationDate {
			
	private IResource fResource;
	private ArrayList fChildren;
	private int startingColumn = 1;
	private File tempFile;

	public MemberNode(IResource resource) {
		fResource= resource;
		Assert.isNotNull(resource);
	}
	
	public MemberNode(IResource resource, int startingColumn) {
		this(resource);
		this.startingColumn = startingColumn;
	}

	public IResource getResource() {
		return fResource;
	}

	public long getModificationDate() {
		IPath path= fResource.getLocation();
		File file= path.toFile();
		return file.lastModified();
	}

	public String getName() {
		if (fResource != null)
			return fResource.getName();
		return null;
	}
		
	public String getType() {
		if (fResource instanceof IContainer)
			return ITypedElement.FOLDER_TYPE;
		if (fResource != null) {
			String s= fResource.getFileExtension();
			if (s != null)
				return s;
		}
		return ITypedElement.UNKNOWN_TYPE;
	}

	public Image getImage() {
		return CompareUI.getImage(fResource);
	}

	public boolean equals(Object other) {
		if (other instanceof ITypedElement) {
			String otherName= ((ITypedElement)other).getName();
			return getName().equals(otherName);
		}
		return super.equals(other);
	}

	public int hashCode() {
		return getName().hashCode();
	}

	public Object[] getChildren() {
		if (fChildren == null) {
			fChildren= new ArrayList();
			if (fResource instanceof IContainer) {
				try {
					IResource members[]= ((IContainer)fResource).members();
					for (int i= 0; i < members.length; i++) {
						IStructureComparator child= createChild(members[i]);
						if (child != null)
							fChildren.add(child);
					}
				} catch (CoreException ex) {
				}
			}
		}
		return fChildren.toArray();
	}

	protected IStructureComparator createChild(IResource child) {
		return new ResourceNode(child);
	}

	protected InputStream createStream() throws CoreException {
		if (fResource instanceof IStorage) {
			if (startingColumn == 1)
				return new BufferedInputStream(((IStorage)fResource).getContents());
			return getMemberStream();
		}
		return null;
	}
	
	private InputStream getMemberStream() throws CoreException {
		try {
			File file = fResource.getLocation().toFile();
			tempFile = new File(file.getPath() + "_compare"); //$NON-NLS-1$
			BufferedReader in =
				new BufferedReader(
					new FileReader(file));
			PrintWriter out =
				new PrintWriter(
					new BufferedWriter(
						new FileWriter(tempFile)));
			String s;
			while ((s = in.readLine()) != null) {
				out.println(s.substring(startingColumn));
			}
			in.close();
			out.close();
			return new BufferedInputStream(new FileInputStream(tempFile));
		} catch (Exception e) {
		}
		return null;
	}

	public boolean isEditable() {
		return true;
	}

	public ITypedElement replace(ITypedElement child, ITypedElement other) {
		return child;
	}

	public File getTempFile() {
		return tempFile;
	}
	
	public void commit(IProgressMonitor pm) throws Exception {
		IResource resource= getResource();
		if (resource instanceof IFile) {
		    IFile file= (IFile) resource;
			byte[] bytes= getContent();	
			ByteArrayInputStream is = null;
			try {
			    
			    // Add sequence numbers.
			    String contents = new String(bytes);	
				StringBuffer updatedContents = new StringBuffer();
				BufferedReader in =
				    new BufferedReader(
				            new StringReader(contents));
				String s;
				int seq = 0;
				while ((s = in.readLine()) != null) {
				    if (seq < 990000) seq = seq + 100;
				    else if (seq < 999999) seq++;
				    String sequence = Integer.toString(seq);
				    while (sequence.length() < 6) sequence = "0" + sequence; //$NON-NLS-1$
				    updatedContents.append(sequence + "000000" + s + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				in.close();
				is= new ByteArrayInputStream(updatedContents.toString().getBytes());
			    
				if (file.exists())
					file.setContents(is, false, true, pm);
				else
					file.create(is, false, pm);
			} finally {
				if (is != null)
					try {
						is.close();
					} catch(IOException ex) {
						// Silently ignored
					}
			}
			file.refreshLocal(IFile.DEPTH_INFINITE, pm);
		}	    
	}

}

