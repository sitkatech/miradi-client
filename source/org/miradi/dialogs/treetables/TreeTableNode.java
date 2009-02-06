/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import java.util.Arrays;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.utils.HtmlUtilities;

public abstract class TreeTableNode implements Comparable
{
	abstract public ORef getObjectReference();
	public abstract int getChildCount();
	public abstract TreeTableNode getChild(int index);
	public abstract Object getValueAt(int column);
	public abstract BaseObject getObject();
	abstract public void rebuild() throws Exception;

	public String toString()
	{
		String value = toRawString();
		value = "<html>" + HtmlUtilities.plainStringWithNewlinesToHtml(value);
		return value;
	}

	public String toRawString()
	{
		return null;
	}
	
	public int getType()
	{
		if (getObject() == null)
		{
			EAM.logWarning("Object is null in getType, returning Fake as object type.");
			return ObjectType.FAKE;
		}
		
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
	
	public int getProportionShares()
	{
		return 1;
	}
	
	public int getTotalShareCount()
	{
		if(getObject() == null)
			return 1;
		return getObject().getTotalShareCount();
	}

	public boolean areBudgetValuesAllocated()
	{
		for(int i = 0; i < getChildCount(); ++i)
			if(getChild(i).areBudgetValuesAllocated())
				return true;
		
		if(getProportionShares() < getTotalShareCount())
			return true;
		
		return false;
	}
	
	public void sortChildren(TreeTableNode[] nodes)
	{
		Arrays.sort(nodes);
	}
	
	public int compareTo(Object otherObject)
	{
		return toString().compareTo(otherObject.toString());
	}
}
