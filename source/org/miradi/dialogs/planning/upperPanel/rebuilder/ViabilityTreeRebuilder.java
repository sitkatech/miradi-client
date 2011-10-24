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

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import java.util.Vector;

import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.objects.Goal;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class ViabilityTreeRebuilder extends NormalTreeRebuilder
{
	public ViabilityTreeRebuilder(Project projectToUse,	PlanningTreeRowColumnProvider rowColumnProviderToUse)
	{
		super(projectToUse, rowColumnProviderToUse);
	}

	@Override
	protected void addChildrenOfNodeToList(Vector<AbstractPlanningTreeNode> destination, AbstractPlanningTreeNode otherNode)
	{
	}
	
	@Override
	protected boolean isVisible(CodeList objectTypesToShow, AbstractPlanningTreeNode child)
	{
		if (child.getObjectReference().isInvalid() && Goal.is(child.getType()))
			return true;
		
		if (Goal.is(child.getObject()))
			return false;
		
		return objectTypesToShow.contains(child.getObjectTypeName());
	}
}
