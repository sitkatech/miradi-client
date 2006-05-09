/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.views.strategicplan.StratPlanObject;

public class ActivityInsertionPoint
{
	public ActivityInsertionPoint(TreePath pathToParent, int childIndexToInsertAt)
	{
		path = pathToParent;
		index = childIndexToInsertAt;
	}
	
	public TreePath getPath()
	{
		return path;
	}
	
	public StratPlanObject getParent()
	{
		return (StratPlanObject)path.getLastPathComponent();
	}
	
	public int getInterventionId()
	{
		return getParent().getId();
	}
	
	public int getIndex()
	{
		return index;
	}
	
	TreePath path;
	int index;
}
