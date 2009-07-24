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

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class ResourceTreeRootNode extends AbstractPlanningTreeNode
{
	public ResourceTreeRootNode(Project projectToUse, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		
		rebuild();
	}
	
	@Override
	public BaseObject getObject()
	{
		return getProject().getMetadata();
	}
	
	@Override
	public void rebuild() throws Exception
	{
		children = new Vector();
		ORefList projectResourceRefs = getProject().getResourcePool().getRefList();
		for (int index = 0; index < projectResourceRefs.size(); ++index)
		{
			children.add(new BaseObjectTreeNode(getProject(), visibleRows, projectResourceRefs.get(index)));
		}
	}
}
