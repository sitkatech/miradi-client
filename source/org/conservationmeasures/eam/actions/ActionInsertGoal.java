/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandInsertGoal;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;
import org.conservationmeasures.eam.main.Project;

public class ActionInsertGoal extends MainWindowAction
{
	public ActionInsertGoal(MainWindow mainWindowToUse, Point location)
	{
		super(mainWindowToUse, getLabel());
		createGoalAt = location;
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Goal");
	}

	public void actionPerformed(ActionEvent event)
	{
		String initialText = EAM.text("Label|New Goal");
		Command insertCommand = new CommandInsertGoal(initialText);
		Project project = getMainWindow().getProject();
		Node insertedNode = (Node)project.executeCommand(insertCommand);
		int[] ids = {project.getDiagramModel().getNodeId(insertedNode)};
		Command moveCommand = new CommandDiagramMove(createGoalAt.x, createGoalAt.y, ids);
		project.executeCommand(moveCommand);
	}

	Point createGoalAt;
}
