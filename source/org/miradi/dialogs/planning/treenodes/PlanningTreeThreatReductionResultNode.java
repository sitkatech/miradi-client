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
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningTreeThreatReductionResultNode extends AbstractPlanningTreeNode
{
	public PlanningTreeThreatReductionResultNode(Project projectToUse, DiagramObject diagramToUse, ORef threatReductionResultRef, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		diagramObject = diagramToUse;
		threatReductionResult = (ThreatReductionResult)project.findObject(threatReductionResultRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		DiagramObject diagram = diagramObject;
		
		createAndAddChildren(threatReductionResult.getObjectiveRefs(), diagram);
		addMissingChildren(threatReductionResult.getIndicatorRefs(), diagram);
	}

	protected int[] getNodeSortOrder()
	{
		return new int[] {
				Strategy.getObjectType(),
				Indicator.getObjectType(),
				Objective.getObjectType(),
				IntermediateResult.getObjectType(),
				Task.getObjectType(),
				Measurement.getObjectType(),
				Assignment.getObjectType(),
			};
	}

	public BaseObject getObject()
	{
		return threatReductionResult;
	}

	private DiagramObject diagramObject;
	private ThreatReductionResult threatReductionResult;
}
