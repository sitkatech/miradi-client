/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandInsertThreat;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;

public class ActionInsertThreat extends MainWindowAction
{
	public ActionInsertThreat(MainWindow mainWindowToUse, Point location)
	{
		super(mainWindowToUse, getLabel());
		createThreatAt = location;
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Threat");
	}

	public void actionPerformed(ActionEvent event)
	{
		String initialText = EAM.text("Label|New Threat");
		CommandInsertThreat command = new CommandInsertThreat(createThreatAt, initialText);
		getMainWindow().getProject().executeCommand(command);
	}

	Point createThreatAt;
}
