/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetTargetGoal;
import org.conservationmeasures.eam.diagram.DiagramModel;
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
		int id = insertCommand.getId();

		Goal goal = new Goal("goal1");
		Goals goals = new Goals();
		goals.add(goal);
		Command setGoalCommand = new CommandSetTargetGoal(id, goals);
		setGoalCommand.execute(project);

		DiagramNode found = model.getNodeById(id);
		Goals foundgoal = found.getGoals();
		assertEquals("wrong goal?", goal, foundgoal.get(0));
		
		project.close();
	}

}
