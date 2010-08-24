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
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;

public class DiagramObjectNode extends TreeTableNode
{
	public DiagramObjectNode(DiagramObject currentDiagramObjectToUse) throws Exception
	{
		currentDiagramObject = currentDiagramObjectToUse;
		rebuild();
	}
	
	@Override
	public TreeTableNode getChild(int index)
	{
		return children[index];
	}

	@Override
	public int getChildCount()
	{
		return children.length;
	}

	@Override
	public BaseObject getObject()
	{
		return currentDiagramObject;
	}

	@Override
	public ORef getObjectReference()
	{
		return getObject().getRef();
	}

	@Override
	public Object getValueAt(int column)
	{
		return currentDiagramObject.toString();
	}

	@Override
	public void rebuild() throws Exception
	{
		Factor[] allDiagramObjectFactors = currentDiagramObject.getAllWrappedFactors();
		children = new TreeTableNode[allDiagramObjectFactors.length];
		for (int index = 0; index < allDiagramObjectFactors.length; ++index)
		{
			FactorTreeTableNode factorNode = new FactorTreeTableNode(allDiagramObjectFactors[index]);
			children[index] = factorNode;
		}
		
		sortChildren(children);
	}
	
	@Override
	public String toRawString()
	{
		return currentDiagramObject.toString();
	}
	
	private DiagramObject currentDiagramObject;
	private TreeTableNode[] children;
}
