/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertGoal extends InsertNodeAction
{
	public ActionInsertGoal(MainWindow mainWindowToUse, Point location)
	{
		super(mainWindowToUse, getLabel(), location);
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Goal");
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Goal");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		doInsert(new CommandInsertNode(Node.TYPE_GOAL));
	}

}
