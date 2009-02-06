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
package org.miradi.dialogs.taggedObjectSet;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;

public class FactorTreeTableNode extends TreeTableNode
{
	public FactorTreeTableNode(Factor factorToUse)
	{
		factor = factorToUse;
	}
	
	@Override
	public TreeTableNode getChild(int index)
	{
		return null;
	}

	@Override
	public int getChildCount()
	{
		return 0;
	}

	@Override
	public BaseObject getObject()
	{
		return factor;
	}

	@Override
	public ORef getObjectReference()
	{
		return factor.getRef();
	}

	@Override
	public Object getValueAt(int column)
	{
		return factor.toString();
	}

	@Override
	public void rebuild() throws Exception
	{
	}

	@Override
	public int compareTo(Object otherObject)
	{
		if (!(otherObject instanceof TreeTableNode))
			return super.compareTo(otherObject);
		
		TreeTableNode otherNode = (TreeTableNode) otherObject;
		return otherNode.getObjectReference().compareTo(getObjectReference());
	}
	
	@Override
	public String toRawString()
	{
		return factor.toString();
	}
	
	private Factor factor; 
}
