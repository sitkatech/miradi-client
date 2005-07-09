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
import org.conservationmeasures.eam.commands.CommandInsertThreat;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;
import org.conservationmeasures.eam.main.Project;

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
		CommandInsertThreat insertCommand = new CommandInsertThreat();
		Project project = getMainWindow().getProject();
		Node insertedNode = (Node)project.executeCommand(insertCommand);
		int id = project.getDiagramModel().getNodeId(insertedNode);

		Command moveCommand = new CommandDiagramMove(createThreatAt.x, createThreatAt.y, new int[] {id});
		project.executeCommand(moveCommand);

		String initialText = EAM.text("Label|New Threat");
		Command setTextCommand = new CommandSetNodeText(id, initialText);
		project.executeCommand(setTextCommand);
	}

	Point createThreatAt;
}
