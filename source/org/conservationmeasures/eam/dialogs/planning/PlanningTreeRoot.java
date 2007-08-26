/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.views.TreeTableNode;;

public class PlanningTreeRoot extends TreeTableNode
{
	public PlanningTreeRoot()
	{
	}
	
	public TreeTableNode getChild(int index)
	{
		return null;
	}

	public int getChildCount()
	{
		return 0;
	}

	public BaseObject getObject()
	{
		return null;
	}

	public ORef getObjectReference()
	{
		return null;
	}

	public int getType()
	{
		return 0;
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public void rebuild() throws Exception
	{
	}

	public String toString()
	{
		return null;
	}
}
