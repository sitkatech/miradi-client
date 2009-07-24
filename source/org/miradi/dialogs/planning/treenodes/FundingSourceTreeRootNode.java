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
import org.miradi.objects.FundingSource;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

//FIXME urgent - simplify this and ProjectResource tree root node class to pass in type of pool 
public class FundingSourceTreeRootNode extends AbstractPlanningTreeNode
{
	public FundingSourceTreeRootNode(Project projectToUse, CodeList visibleRowsToUse) throws Exception
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
		ORefList fundingSourceRefs = getProject().getPool(FundingSource.getObjectType()).getRefList();
		for (int index = 0; index < fundingSourceRefs.size(); ++index)
		{
			children.add(new BaseObjectTreeNode(getProject(), visibleRows, fundingSourceRefs.get(index)));
		}
	}
}
