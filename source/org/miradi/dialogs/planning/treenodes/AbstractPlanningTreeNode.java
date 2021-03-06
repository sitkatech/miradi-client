/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.utils.CodeList;

abstract public class AbstractPlanningTreeNode extends TreeTableNode
{
	public AbstractPlanningTreeNode(Project projectToUse, TreeTableNode parentNodeToUse)
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
	
	//TODO New node types do not rebuild themselves and there fore this method has 
	//no implementation and is only here for a transitional period.  
	//Once Old node hierarchy has been removed, this method needs to go away.
	@Override
	public void rebuild() throws Exception
	{
		//NOTE, this method is called, but should not do anything.  TreeRebuilder is responsible
		//for rebuilding new node types.
	}
	
	//TODO Remove method as soon as old planning style nodes are removed.
	@Override
	public void setVisibleRowCodes(CodeList visibleRowsToUse)
	{
		//NOTE, this method is called, but should not do anything.  This method is
		//only here during a transition between new and old node types
	}
	
	@Override
	public String getNodeLabel()
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

	public void addChild(AbstractPlanningTreeNode node)
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
		children = new Vector<AbstractPlanningTreeNode>();
	}
	
	public Vector<AbstractPlanningTreeNode> getRawChildrenByReference()
	{
		return children;
	}
	
	public void setRawChildren(Vector<AbstractPlanningTreeNode> newChildren)
	{
		children = newChildren;
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof AbstractPlanningTreeNode))
			return false;
		
		AbstractPlanningTreeNode other = (AbstractPlanningTreeNode)rawOther;
		
		return getObject().equals(other.getObject());
	}
	
	@Override
	public int hashCode()
	{
		return getObject().hashCode();
	}

	public Project getProject()
	{
		return project;
	}

	protected Project project;
	private TreeTableNode parentNode;
	protected Vector<AbstractPlanningTreeNode> children;
}
