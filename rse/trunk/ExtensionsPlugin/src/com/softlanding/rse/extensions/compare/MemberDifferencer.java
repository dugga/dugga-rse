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

import java.io.*;

import org.eclipse.compare.structuremergeviewer.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

public class MemberDifferencer extends Differencer {
	private int startingColumn = 1;

	public MemberDifferencer() {
		super();
	}
	
	public MemberDifferencer(int startingColumn) {
		this();
		this.startingColumn = startingColumn;
	}
	
	protected boolean contentsEqual(Object input1, Object input2) {
		
		if (input1 == input2)
			return true;
			
		InputStream is1= getStream(input1);
		InputStream is2= getStream(input2);
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		
		if (is1 == null && is2 == null)	// no byte contents
			return true;
		
		try {
			if (is1 == null || is2 == null)	// only one has contents
				return false;

			br1 = new BufferedReader(
				new InputStreamReader(is1));
			
			br2 = new BufferedReader(
				new InputStreamReader(is2));
					
			while (true) {
				String s1 = br1.readLine();
				String s2 = br2.readLine();
				if (s1 == null && s2 == null)
					return true;
				if (s1 == null && s2 != null)
					break;
				if (s2 == null && s1 != null)
					break;
				if (!s1.substring(startingColumn - 1).trim().equalsIgnoreCase(s2.substring(startingColumn - 1).trim()))
					break;
				
			}
		} catch (IOException ex) {
		} finally {
			if (is1 != null) {
				try {
					is1.close();
					br1.close();
				} catch(IOException ex) {
				}
			}
			if (is2 != null) {
				try {
					is2.close();
					br2.close();
				} catch(IOException ex) {
				}
			}
		}
		return false;
	}
	
	private InputStream getStream(Object o) {
		IResource resource = ((MemberNode)o).getResource();
		IPath path= resource.getLocation();
		File file= path.toFile();
		try {
			return new DataInputStream(
				new BufferedInputStream(
					new FileInputStream(file)));
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

}
