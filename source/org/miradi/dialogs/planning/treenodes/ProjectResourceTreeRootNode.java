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

package org.miradi.dialogs.planning.treenodes;

import java.util.Vector;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class ProjectResourceTreeRootNode extends AbstractPlanningTreeNode
{
	public ProjectResourceTreeRootNode(Project projectToUse, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		
		rebuild();
	}

	@Override
	public ORef getObjectReference()
	{
		return ORef.INVALID;
	}
	
	@Override
	public int getType()
	{
		return ProjectMetadata.getObjectType();
	}
	
	@Override
	public BaseObject getObject()
	{
		return null;
	}
	
	@Override
	public void rebuild() throws Exception
	{
		children = new Vector();
		ProjectResource[] projectResources = getProject().getResourcePool().getAllProjectResources();
		for (int index = 0; index < projectResources.length; ++index)
		{
			ProjectResourceTreeProjectResourceNode node = new ProjectResourceTreeProjectResourceNode(getProject(), visibleRows, projectResources[index]);
			children.add(node);
		}
	}
}
