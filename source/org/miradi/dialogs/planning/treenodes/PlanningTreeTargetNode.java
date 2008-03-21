/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningTreeTargetNode extends AbstractPlanningTreeNode
{
	public PlanningTreeTargetNode(Project projectToUse, DiagramObject diagramToUse, ORef targetRef, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		diagramObject = diagramToUse;
		target = (Target)project.findObject(targetRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		DiagramObject diagram = diagramObject;
		createAndAddChildren(target.getOwnedObjects(Goal.getObjectType()), diagram);

		addMissingUpstreamDirectThreats(diagram);
		addMissingUpstreamThreatReductionResults(diagram);
		addMissingUpstreamObjectives(diagram);
		addMissingUpstreamNonDraftStrategies(diagram);
		addMissingUpstreamIndicators(diagram);
	}
	
	int[] getNodeSortOrder()
	{
		return new int[] {
				Goal.getObjectType(),
				Strategy.getObjectType(),
				Indicator.getObjectType(),
				Cause.getObjectType(),
				ThreatReductionResult.getObjectType(),
				Objective.getObjectType(),
				Task.getObjectType(),
				Measurement.getObjectType(),
			};
	}

	public BaseObject getObject()
	{
		return target;
	}

	DiagramObject diagramObject;
	Target target;
}
