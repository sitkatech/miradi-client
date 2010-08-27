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

import java.util.Vector;

import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeObjectiveNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeTaskNode;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.Cause;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.utils.CodeList;

public class TestAbstractPlanningTreeNode extends TestCaseWithProject
{
	public TestAbstractPlanningTreeNode(String name)
	{
		super(name);
	}
	
	public void testMergeChildIntoList() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Task activity = getProject().createTask(strategy);
		ORefList relevantActivityRefs = new ORefList(activity);
		RelevancyOverrideSet relevantActivities = new RelevancyOverrideSet();
		for (int index = 0; index < relevantActivityRefs.size(); ++index)
		{
			relevantActivities.add(new RelevancyOverride(relevantActivityRefs.get(index), true));
		}
		
		Cause factor = getProject().createCause();
		Objective objective = getProject().createObjective(factor);
		getProject().fillObjectUsingCommand(objective, Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevantActivities.toString());
		
		PlanningTreeObjectiveNode objectiveNode = new PlanningTreeObjectiveNode(getProject(), getProject().getTestingDiagramObject(), objective.getRef(), new CodeList());	
		assertEquals("Wrong objectice children count?", 1, objectiveNode.getChildCount());
		
		Vector<AbstractPlanningTreeNode> destination = new Vector<AbstractPlanningTreeNode>();
		destination.add(objectiveNode);
		
		PlanningTreeTaskNode activityNode = new PlanningTreeTaskNode(getProject(), strategy.getRef(), activity.getRef(), new CodeList());
		AbstractPlanningTreeNode.mergeChildIntoList(destination, activityNode);
		
		assertEquals("Activity node should not have been added since its a child of the objective?", 1, destination.size());
	}
}
