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

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.project.Project;

public class NewPlanningTaskNode extends PlanningTreeBaseObjectNode
{
	public NewPlanningTaskNode(Project projectToUse, ORef contextNodeRefToUse, TreeTableNode parentNodeToUse, ORef objectRef) throws Exception
	{
		super(projectToUse, parentNodeToUse, objectRef);

		contextNodeRef = contextNodeRefToUse;
		contextNodeRefs = new ORefSet();
		contextNodeRefs.add(contextNodeRef);
	}
	
	@Override
	public int getProportionShares()
	{
		return contextNodeRefs.size();
	}
	
	@Override
	public void addProportionShares(TreeTableNode rawNode)
	{
		NewPlanningTaskNode taskNode = (NewPlanningTaskNode) rawNode;
		contextNodeRefs.addAll(taskNode.contextNodeRefs);
	}
	
	@Override
	public ORef getContextRef()
	{
		// TODO: Should probably just use the only element of cNR
		return contextNodeRef;
	}

	private ORefSet contextNodeRefs;
	private ORef contextNodeRef;
	
}
