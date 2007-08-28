/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;

public abstract class TreeTableNode
{
	public abstract int getType();
	abstract public ORef getObjectReference();
	public abstract String toString();
	public abstract int getChildCount();
	public abstract TreeTableNode getChild(int index);
	public abstract Object getValueAt(int column);
	public abstract BaseObject getObject();
	abstract public void rebuild() throws Exception;
	public TreeTableNode getParentNode() throws Exception 
	{
		throw new Exception("getParent not implemented yet for nodes of type " + getType() + " in node:" + getClass());
	};
	
	public int getIndex(TreeTableNode child)
	{
		for(int i = 0; i < getChildCount(); ++i)
			if(child.equals(getChild(i)))
				return i;
		
		return -1;
	}
	
	public boolean isAlwaysExpanded()
	{
		return false;
	}
}
