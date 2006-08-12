/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.annotations.Goal;
import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetTargetGoal;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.martus.util.TestCaseEnhanced;

public class TestSetTargetGoal extends TestCaseEnhanced 
{
	public TestSetTargetGoal(String name)
	{
		super(name);
	}

	public void testSetTargetGoal() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		CommandInsertNode insertCommand = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		insertCommand.execute(project);
		BaseId id = insertCommand.getId();

		BaseId goalId = project.getGoalPool().getIds()[1]; 
		Goal goal = project.getGoalPool().find(goalId);
		GoalIds goals = new GoalIds();
		goals.addId(goalId);
		Command setGoalCommand = new CommandSetTargetGoal(id, goals);
		setGoalCommand.execute(project);

		DiagramNode found = model.getNodeById(id);
		GoalIds foundgoal = found.getGoals();
		assertEquals("wrong goal?", goal.getId(), foundgoal.getId(0));
		
		project.close();
	}

}
