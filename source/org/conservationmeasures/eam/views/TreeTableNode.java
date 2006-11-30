/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;

public abstract class TreeTableNode
{
	public abstract int getType();
	abstract public ORef getObjectReference();
	public abstract String toString();
	public abstract int getChildCount();
	public abstract TreeTableNode getChild(int index);
	public abstract Object getValueAt(int column);
	public abstract EAMObject getObject();
}
