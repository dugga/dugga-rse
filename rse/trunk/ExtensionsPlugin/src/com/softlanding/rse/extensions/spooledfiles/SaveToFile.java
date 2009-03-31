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
package com.softlanding.rse.extensions.spooledfiles;

import java.io.IOException;
import java.io.OutputStream;

import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.PrintObjectTransformedInputStream;
import com.ibm.as400.access.PrintParameterList;
import com.ibm.as400.access.RequestNotSupportedException;
import com.ibm.as400.access.SpooledFile;

public class SaveToFile {

	public final static String PLAIN_TEXT = "/QSYS.LIB/QWPDEFAULT.WSCST"; //$NON-NLS-1$
	public final static String TIFF_G4 = "/QSYS.LIB/QWPTIFFG4.WSCST"; //$NON-NLS-1$

	public static void saveAsText(SpooledFile splf, OutputStream out) throws AS400Exception, AS400SecurityException, ErrorCompletingRequestException, IOException, InterruptedException, RequestNotSupportedException {
		SaveToFile.saveAs(splf, out, SaveToFile.PLAIN_TEXT);
	}

	public static void saveAsTiff(SpooledFile splf, OutputStream out) throws AS400Exception, AS400SecurityException, ErrorCompletingRequestException, IOException, InterruptedException, RequestNotSupportedException {
		SaveToFile.saveAs(splf, out, SaveToFile.TIFF_G4);
	}

	public static void saveAs(SpooledFile splf, OutputStream out, String type) throws RequestNotSupportedException, InterruptedException, AS400Exception, IOException, ErrorCompletingRequestException, AS400SecurityException {
		PrintObjectTransformedInputStream in = null;
		try {
			PrintParameterList printParms = new PrintParameterList();
			printParms.setParameter(
				PrintObject.ATTR_WORKSTATION_CUST_OBJECT,
				type);
			printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST"); //$NON-NLS-1$
			// get the text (via a transformed input stream) from the spooled file
			in = splf.getTransformedInputStream(printParms);

			byte[] buffer = new byte[8 * 1024];
			int count = 0;
			do {
				out.write(buffer, 0, count);
				count = in.read(buffer, 0, buffer.length);
			} while (count != -1);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
