/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.views.TreeTableNode;

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
	
	public TreeTableNode getParent()
	{
		return (TreeTableNode)path.getLastPathComponent();
	}
	
	public ORef getProposedParentORef()
	{
		return getParent().getObjectReference();
	}
	
	public int getIndex()
	{
		return index;
	}
	
	TreePath path;
	int index;
}
