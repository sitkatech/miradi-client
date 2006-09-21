/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

public abstract class MonitoringNode
{
	abstract public int getType();
	abstract public String toString();
	abstract public int getChildCount();
	abstract public MonitoringNode getChild(int index);
	abstract public Object getValueAt(int column);
	
	public static final int COLUMN_ITEM_LABEL = 0;
	public static final int COLUMN_TARGETS = 1;
}
