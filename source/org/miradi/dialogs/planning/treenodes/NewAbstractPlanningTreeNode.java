/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning.treenodes;

import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.project.Project;

abstract public class NewAbstractPlanningTreeNode extends TreeTableNode
{
	public NewAbstractPlanningTreeNode(Project projectToUse, TreeTableNode parentNodeToUse)
	{
		project = projectToUse;
		parentNode = parentNodeToUse;
		clearChildren();
	}

	@Override
	public TreeTableNode getParentNode() throws Exception
	{
		return parentNode;
	}
	
	@Override
	public ORef getObjectReference()
	{
		if(getObject() == null)
			return ORef.createInvalidWithType(getType());
		
		return getObject().getRef();
	}
	
	@Override
	public TreeTableNode getChild(int index)
	{
		return children.get(index);
	}

	@Override
	public int getChildCount()
	{
		return children.size();
	}
	
	@Override
	public Object getValueAt(int column)
	{
		return null;
	}
	
	@Override
	public void rebuild() throws Exception
	{
		throw new Exception("Can't call rebuild on " + getClass().getCanonicalName());
	}
	
	@Override
	public String toRawString()
	{
		if (getObject() == null)
			return "";
		
		return getObject().getFullName();
	}

	public String getObjectTypeName()
	{
		if (getObject() != null)
			return getObject().getTypeName();
		
		EAM.logError("getObject() in getObjectTypeName is null");
		return "";
	}

	public void addChild(NewAbstractPlanningTreeNode node)
	{
		ORefSet existingGrandChildRefs = getGrandChildRefs();
		if(!existingGrandChildRefs.contains(node.getObjectReference()))
			children.add(node);
	}
	
	private ORefSet getGrandChildRefs()
	{
		ORefSet grandchildRefs = new ORefSet();
		for(int childIndex = 0; childIndex < getChildCount(); ++childIndex)
		{
			TreeTableNode child = getChild(childIndex);
			for(int grandchildIndex = 0; grandchildIndex < child.getChildCount(); ++grandchildIndex)
			{
				TreeTableNode grandchild = child.getChild(grandchildIndex);
				grandchildRefs.add(grandchild.getObjectReference());
			}
		}
		
		return grandchildRefs;
	}

	public void clearChildren()
	{
		children = new Vector<NewAbstractPlanningTreeNode>();
	}
	
	public Project getProject()
	{
		return project;
	}

	protected Project project;
	private TreeTableNode parentNode;
	protected Vector<NewAbstractPlanningTreeNode> children;
}
