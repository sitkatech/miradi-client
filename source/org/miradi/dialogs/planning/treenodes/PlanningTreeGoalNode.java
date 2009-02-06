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
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningTreeGoalNode extends PlanningTreeAbstractDesireNode
{
	public PlanningTreeGoalNode(Project projectToUse, DiagramObject diagramToUse, ORef goalRef, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, diagramToUse, visibleRowsToUse, goalRef);
		goal = (Goal)project.findObject(goalRef);
		rebuild();
	}
	
	protected int[] getNodeSortOrder()
	{
		return new int[] {
				Strategy.getObjectType(),
				Indicator.getObjectType(),
				Cause.getObjectType(),
				IntermediateResult.getObjectType(),
				ThreatReductionResult.getObjectType(),
				Objective.getObjectType(),
				Task.getObjectType(),
				Measurement.getObjectType(),
			};
	}

	public BaseObject getObject()
	{
		return goal;
	}
	
	private Goal goal;
}
