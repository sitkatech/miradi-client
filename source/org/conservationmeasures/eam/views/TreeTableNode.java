/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

public abstract class TreeTableNode
{
	public abstract int getType();
	public abstract String toString();
	public abstract int getChildCount();
	public abstract TreeTableNode getChild(int index);
	public abstract Object getValueAt(int column);
}
