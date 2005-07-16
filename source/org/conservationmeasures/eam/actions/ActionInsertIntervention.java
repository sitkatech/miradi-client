/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandFailedException;
import org.conservationmeasures.eam.commands.CommandInsertIntervention;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertIntervention extends InsertNodeAction
{
	public ActionInsertIntervention(MainWindow mainWindowToUse, Point location)
	{
		super(mainWindowToUse, getLabel(), location);
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Intervention");
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Intervention");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		doInsert(new CommandInsertIntervention());
	}
}
