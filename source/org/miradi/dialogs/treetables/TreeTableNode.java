/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.treetables;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;

public abstract class TreeTableNode implements Comparable
{
	abstract public ORef getObjectReference();
	public abstract String toString();
	public abstract int getChildCount();
	public abstract TreeTableNode getChild(int index);
	public abstract Object getValueAt(int column);
	public abstract BaseObject getObject();
	abstract public void rebuild() throws Exception;

	public int getType()
	{
		return getObjectReference().getObjectType();
	}

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
	
	public int compareTo(Object otherObject)
	{
		return toString().compareTo(otherObject.toString());
	}
}
