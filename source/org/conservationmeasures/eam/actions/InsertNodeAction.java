/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import javax.swing.Icon;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.MainWindow;

public abstract class InsertNodeAction extends LocationAction
{
	public InsertNodeAction(MainWindow mainWindow, String label, Icon icon)
	{
		super(mainWindow, label, icon);
	}
	
	abstract public String getInitialText();

	protected void doInsert(CommandInsertNode insertCommand) throws CommandFailedException
	{
		getProject().executeCommand(insertCommand);
		int id = insertCommand.getId();
	
		Command setTextCommand = new CommandSetNodeText(id, getInitialText());
		getProject().executeCommand(setTextCommand);

		Command moveCommand = new CommandDiagramMove(createAt.x, createAt.y, new int[] {id});
		getProject().executeCommand(moveCommand);
	}
}
