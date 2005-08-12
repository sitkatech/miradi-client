/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.ProjectDoer;

public class InsertGoal extends ProjectDoer
{
	public InsertGoal(BaseProject project, Point invocationPoint)
	{
		super(project);
		createAt = invocationPoint;
	}

	public boolean isAvailable()
	{
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		doInsert(new CommandInsertNode(Node.TYPE_GOAL));
	}

	protected void doInsert(CommandInsertNode insertCommand) throws CommandFailedException
	{
		getProject().executeCommand(insertCommand);
		int id = insertCommand.getId();
	
		Command setTextCommand = new CommandSetNodeText(id, getInitialText());
		getProject().executeCommand(setTextCommand);

		Command moveCommand = new CommandDiagramMove(createAt.x, createAt.y, new int[] {id});
		getProject().executeCommand(moveCommand);
	}
	
	public String getInitialText()
	{
		return EAM.text("Label|New Goal");
	}

	Point createAt;
}
