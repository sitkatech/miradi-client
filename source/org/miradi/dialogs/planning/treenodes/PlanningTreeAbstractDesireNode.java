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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Objective;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

abstract public class PlanningTreeAbstractDesireNode extends AbstractPlanningTreeNode
{

	public PlanningTreeAbstractDesireNode(Project projectToUse, DiagramObject diagramObjectToUse, CodeList visibleRowsToUse, ORef desireRef)
	{
		super(projectToUse, visibleRowsToUse);
		
		desire = Desire.findDesire(projectToUse, desireRef);
		diagramObject = diagramObjectToUse;
	}
	
	public void rebuild() throws Exception
	{
		ORefList relevantStrategyAndActivityRefs = desire.getRelevantStrategyAndActivityRefs();
		createAndAddChildren(relevantStrategyAndActivityRefs, getDiagramObject());
		
		ORefList relevantIndicatorRefs = desire.getRelevantIndicatorRefList();
		createAndAddChildren(relevantIndicatorRefs, getDiagramObject());
	}

	@Override
	protected void pruneUnwantedLayers(CodeList objectTypesToShow)
	{
		if(!objectTypesToShow.contains(Objective.OBJECT_NAME))
			children = getChildrenOnSameDiagram();

		super.pruneUnwantedLayers(objectTypesToShow);
	}
	
	private Vector<AbstractPlanningTreeNode> getChildrenOnSameDiagram()
	{
		Vector<AbstractPlanningTreeNode> newChildren = new Vector<AbstractPlanningTreeNode>();
		for(AbstractPlanningTreeNode node : children)
		{
			BaseObject object = node.getObject();
			if(object == null)
				continue;
			Factor factor = object.getDirectOrIndirectOwningFactor();
			if(getDiagramObject().containsWrappedFactorRef(factor.getRef()))
				newChildren.add(node);
		}
		
		return newChildren;
	}
	
	protected DiagramObject getDiagramObject()
	{
		return diagramObject;
	}
	
	private DiagramObject diagramObject;
	private Desire desire;
}
