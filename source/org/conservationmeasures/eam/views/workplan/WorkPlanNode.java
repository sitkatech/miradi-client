/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;


public abstract class WorkPlanNode
{
	abstract public int getType();
	abstract public String toString();
	abstract public int getChildCount();
	abstract public Object getChild(int index);
	abstract public Object getValueAt(int column);
}
