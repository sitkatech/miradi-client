/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;

import javax.swing.Icon;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.Project;

public abstract class InsertNodeAction extends MainWindowAction
{
	public InsertNodeAction(MainWindow mainWindowToUse, String label, Icon icon)
	{
		super(mainWindowToUse, label, icon);
	}
	
	public void setInvocationPoint(Point location)
	{
		createAt = location;
	}

	abstract public String getInitialText();

	protected void doInsert(CommandInsertNode insertCommand) throws CommandFailedException
	{
		Project project = getMainWindow().getProject();
		project.executeCommand(insertCommand);
		int id = insertCommand.getId();
	
		Command setTextCommand = new CommandSetNodeText(id, getInitialText());
		project.executeCommand(setTextCommand);

		Command moveCommand = new CommandDiagramMove(createAt.x, createAt.y, new int[] {id});
		project.executeCommand(moveCommand);
	}

	Point createAt;
}
