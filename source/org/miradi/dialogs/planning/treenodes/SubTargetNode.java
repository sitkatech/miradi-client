/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.SubTarget;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class SubTargetNode extends AbstractPlanningTreeNode
{
	public SubTargetNode(Project projectToUse, ORef ref, CodeList visibleRowsToUse)
	{
		super(projectToUse, visibleRowsToUse);
		
		subTarget = SubTarget.find(projectToUse, ref);
	}

	@Override
	public BaseObject getObject()
	{
		return subTarget;
	}
	
	@Override
	boolean shouldSortChildren()
	{
		return false;
	}
	
	@Override
	public String toRawString()
	{
		return subTarget.toString();
	}

	private SubTarget subTarget;
}
