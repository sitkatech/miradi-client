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
package org.miradi.views.planning;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.project.ProjectForTesting;

abstract public class TestPlanningTree extends EAMTestCase
{
	public TestPlanningTree(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		setupFactors();
	}

	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}
	
	private void setupFactors() throws Exception
	{
		projectMetadata = project.getMetadata();
		diagramStrategy1 = project.createDiagramFactorAndAddToDiagram(Strategy.getObjectType());		
		diagramStrategy2 = project.createDiagramFactorAndAddToDiagram(Strategy.getObjectType());		
		diagramCause = project.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		diagramTarget = project.createDiagramFactorAndAddToDiagram(Target.getObjectType());
		
		stratToCauseLinkId = project.createDiagramLinkAndAddToDiagram(diagramStrategy1, diagramCause).getObjectId();		
		causeToTargetLinkId = project.createDiagramLinkAndAddToDiagram(diagramCause, diagramTarget).getObjectId();
		
		objectiveId = project.addItemToObjectiveList(diagramCause.getWrappedORef(), Cause.TAG_OBJECTIVE_IDS);
		indicatorId = project.addItemToIndicatorList(diagramCause.getWrappedORef(), Cause.TAG_INDICATOR_IDS);
		goalId = project.addItemToGoalList(diagramTarget.getWrappedORef(), Target.TAG_GOAL_IDS);
		taskId = project.addItemToIndicatorList(indicatorId, Task.getObjectType(), Indicator.TAG_METHOD_IDS);
		activityId = project.addActivityToStrateyList(diagramStrategy1.getWrappedORef(), Strategy.TAG_ACTIVITY_IDS);
		subtaskId = project.addSubtaskToActivity(getTask().getRef(), Task.TAG_SUBTASK_IDS);
		
		IdList activityIds = new IdList(Task.getObjectType(), new BaseId[] {activityId});
		project.setObjectData(diagramStrategy2.getWrappedORef(), Strategy.TAG_ACTIVITY_IDS, activityIds.toString());
	}
	
	public Goal getGoal()
	{
		return (Goal) project.findObject(new ORef(Goal.getObjectType(), goalId));
	}
	
	public Objective getObjective()
	{
		return (Objective) project.findObject(new ORef(Objective.getObjectType(), objectiveId));
	}
	
	public Strategy getStrategy()
	{
		return (Strategy) project.findObject(diagramStrategy1.getWrappedORef());
	}
	
	public Strategy getStrategy2()
	{
		return (Strategy) project.findObject(diagramStrategy2.getWrappedORef());
	}
	
	public Indicator getIndicator()
	{
		return (Indicator) project.findObject(new ORef(Indicator.getObjectType(), indicatorId));
	}
	
	public Task getTask()
	{
		return (Task) project.findObject(new ORef(Task.getObjectType(), taskId));
	}
	
	public Task getSubtask()
	{
		return (Task) project.findObject(new ORef(Task.getObjectType(), subtaskId));
	}
	
	public Target getTarget()
	{
		return (Target) project.findObject(diagramTarget.getWrappedORef());
	}
	
	public Cause getThreat()
	{
		return (Cause) project.findObject(diagramCause.getWrappedORef());
	}
	
	public ProjectMetadata getProjectMetadata()
	{
		return projectMetadata;
	}
	
	ProjectForTesting project;
	ProjectMetadata projectMetadata;
	DiagramFactor diagramStrategy1;		
	DiagramFactor diagramStrategy2;		
	DiagramFactor diagramCause;
	DiagramFactor diagramTarget;
	
	BaseId stratToCauseLinkId;
	BaseId causeToTargetLinkId;
	
	BaseId indicatorId;
	BaseId objectiveId;
	BaseId goalId;
	BaseId taskId;
	BaseId activityId;
	BaseId subtaskId;
}
