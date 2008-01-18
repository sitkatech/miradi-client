/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;

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
