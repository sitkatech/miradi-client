/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;


abstract public class StratPlanObject
{
	abstract public Object getValueAt(int column);
	abstract public int getChildCount();
	abstract public Object getChild(int index);
	abstract public int getType();
	abstract public int getId();
	
	abstract public String toString();
	
	abstract public boolean canInsertActivityHere();
	abstract public void rebuild();
}

