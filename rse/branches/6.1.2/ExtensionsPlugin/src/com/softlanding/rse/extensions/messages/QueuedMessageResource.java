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
package com.softlanding.rse.extensions.messages;

import com.ibm.as400.access.QueuedMessage;
import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.impl.AbstractResource;


public class QueuedMessageResource extends AbstractResource {
	private QueuedMessage queuedMessage;

	public QueuedMessageResource(SubSystem subSystem) {
		super(subSystem);
	}

	public QueuedMessageResource() {
		super();
	}

	public QueuedMessage getQueuedMessage() {
		return queuedMessage;
	}

	public void setQueuedMessage(QueuedMessage message) {
		queuedMessage = message;
	}

}
