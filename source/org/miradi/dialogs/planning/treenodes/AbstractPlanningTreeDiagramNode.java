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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Objective;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;


public abstract class AbstractPlanningTreeDiagramNode extends AbstractPlanningTreeNode
{
	public AbstractPlanningTreeDiagramNode(Project projectToUse, CodeList visibleRowsToUse)
	{
		super(projectToUse, visibleRowsToUse);
	}

	@Override
	public void rebuild() throws Exception
	{
		ORefList diagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor)project.findObject(diagramFactorRefs.get(i));
			Factor factor = diagramFactor.getWrappedFactor();
			int type = factor.getType();
			if(factor.isTarget() || factor.isDirectThreat() || factor.isContributingFactor() ||  
					type == ThreatReductionResult.getObjectType() || 
					type == IntermediateResult.getObjectType())
			{
				createAndAddChild(diagramFactor.getWrappedORef(), diagramObject);
			}
		}
		
		Factor[] allWrappedFactors = diagramObject.getAllWrappedFactors();

		// NOTE: No need to search for Goals because they can only be inside Targets
		addMissingChildren(diagramObject.getAllObjectiveRefs(), diagramObject);
		addMissingChildren(extractNonDraftStrategyRefs(allWrappedFactors), diagramObject);
		addMissingChildren(extractIndicatorRefs(allWrappedFactors), diagramObject);
	}

	@Override
	public BaseObject getObject()
	{
		return diagramObject;
	}

	boolean isGoalOnThisPage(DiagramObject page, ORef refToAdd)
	{
		if(refToAdd.getObjectType() != Goal.getObjectType())
			return false;
		
		return page.getAllGoalRefs().contains(refToAdd);
	}
	
	boolean isObjectiveOnThisPage(DiagramObject page, ORef refToAdd)
	{
		if(refToAdd.getObjectType() != Objective.getObjectType())
			return false;
		
		return page.getAllObjectiveRefs().contains(refToAdd);
	}

	protected DiagramObject diagramObject;
}
