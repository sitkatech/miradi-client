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

import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;

public class RootProjectNode extends TreeTableNode
{
	public RootProjectNode(Project projectToUse) throws Exception
	{
		project = projectToUse;
		rebuild();
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
	public BaseObject getObject()
	{
		return null;
	}

	@Override
	public ORef getObjectReference()
	{
		return ORef.INVALID;
	}
	
	@Override
	public int getType()
	{
		return ObjectType.FAKE;
	}
	
	@Override
	public Object getValueAt(int column)
	{
		return null;
	}

	@Override
	public void rebuild() throws Exception
	{
		children = new Vector();
		ORefList conceptualModelRefs = getProject().getConceptualModelDiagramPool().getSortedRefList();
		ORefList resultsChainRefs = getProject().getResultsChainDiagramPool().getSortedRefList();
		createAndAddChildren(conceptualModelRefs);
		createAndAddChildren(resultsChainRefs);
	}
	
	private void createAndAddChildren(ORefList diagramObjectRefs) throws Exception
	{
		for (int index = 0; index < diagramObjectRefs.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramObjectRefs.get(index));
			children.add(new DiagramObjectNode(diagramObject));
		}
	}

	@Override
	public String toRawString()
	{
		return project.getFilename();
	}
	
	public Project getProject()
	{
		return project;
	}
	
	private Project project;
	private Vector<TreeTableNode> children;
}
