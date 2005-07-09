/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandInsertGoal;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;

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
		Command command = new CommandInsertGoal(createGoalAt, initialText);
		getMainWindow().getProject().executeCommand(command);
	}

	Point createGoalAt;
}
