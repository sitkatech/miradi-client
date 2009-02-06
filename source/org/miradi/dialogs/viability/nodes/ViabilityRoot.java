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
package org.miradi.dialogs.viability.nodes;

import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class ViabilityRoot extends TreeTableNode
{
	public ViabilityRoot(Project projectToUse) throws Exception
	{
		project = projectToUse;
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return null;
	}

	public TreeTableNode getChild(int index)
	{
		return (TreeTableNode)children.get(index);
	}

	public int getChildCount()
	{
		return children.size();
	}

	public ORef getObjectReference()
	{
		return ORef.INVALID;
	}
	
	public int getType()
	{
		return ObjectType.FAKE;
	}
	
	public boolean isAlwaysExpanded()
	{
		return true;
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	@Override
	public String toRawString()
	{
		return "";
	}
	
	public BaseId getId()
	{
		return null;
	}
	public void rebuild() throws Exception
	{
		Vector vector = new Vector();
		vector.add(new ViabilityProjectNode(project));
		children = vector;
	}
	
	Vector children;
	Project project;

}