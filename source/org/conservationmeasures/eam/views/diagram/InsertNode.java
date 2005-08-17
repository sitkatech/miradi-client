/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;

abstract public class InsertNode extends LocationDoer
{
	public InsertNode(BaseProject project, Point invocationPoint)
	{
		super(project, invocationPoint);
	}
	
	abstract public int getTypeToInsert();
	abstract public String getInitialText();

	public boolean isAvailable()
	{
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		doInsert(new CommandInsertNode(getTypeToInsert()));
	}

	protected void doInsert(CommandInsertNode insertCommand) throws CommandFailedException
	{
		getProject().executeCommand(new CommandBeginTransaction());
		
		getProject().executeCommand(insertCommand);
		int id = insertCommand.getId();
	
		Command setTextCommand = new CommandSetNodeText(id, getInitialText());
		getProject().executeCommand(setTextCommand);

		Point createAt = getLocation();
		Command moveCommand = new CommandDiagramMove(createAt.x, createAt.y, new int[] {id});
		getProject().executeCommand(moveCommand);

		getProject().executeCommand(new CommandEndTransaction());
	}
}
