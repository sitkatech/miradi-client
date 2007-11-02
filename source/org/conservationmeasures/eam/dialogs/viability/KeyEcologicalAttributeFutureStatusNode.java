/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;

public class KeyEcologicalAttributeFutureStatusNode extends TreeTableNode
{
	public KeyEcologicalAttributeFutureStatusNode(TreeTableNode parentNodeToUse) throws Exception
	{
		parentNode = parentNodeToUse;
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return parentNode.getObject();
	}

	public ORef getObjectReference()
	{
		return parentNode.getObjectReference();
	}
	
	public int getType()
	{
		return Goal.getObjectType();
	}

	public String toString()
	{
		return parentNode.getObject().getData(Indicator.TAG_FUTURE_STATUS_DATE);
	}

	public int getChildCount()
	{
		return 0;
	}

	public TreeTableNode getChild(int index)
	{
		return null;
	}
	
	public TreeTableNode getParentNode()
	{
		return parentNode;
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	public void rebuild() throws Exception
	{
	}
	
	public static final String[] COLUMN_TAGS = {
		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY, 
		Goal.TAG_EMPTY,
		
		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY,
		
		Goal.TAG_EMPTY,
	};
	
	private TreeTableNode parentNode;
}
