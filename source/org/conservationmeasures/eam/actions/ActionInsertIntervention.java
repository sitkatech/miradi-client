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
import org.conservationmeasures.eam.commands.CommandInsertIntervention;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;
import org.conservationmeasures.eam.main.Project;

public class ActionInsertIntervention extends MainWindowAction
{

	public ActionInsertIntervention(MainWindow mainWindowToUse, Point location)
	{
		super(mainWindowToUse, getLabel());
		createAt = location;
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Intervention");
	}

	public void actionPerformed(ActionEvent arg0)
	{
		CommandInsertIntervention insertCommand = new CommandInsertIntervention();
		Project project = getMainWindow().getProject();
		Node insertedNode = (Node)project.executeCommand(insertCommand);
		int id = project.getDiagramModel().getNodeId(insertedNode);

		Command moveCommand = new CommandDiagramMove(createAt.x, createAt.y, new int[] {id});
		project.executeCommand(moveCommand);

		String initialText = EAM.text("Label|New Intervention");
		Command setTextCommand = new CommandSetNodeText(id, initialText);
		project.executeCommand(setTextCommand);
	}

	Point createAt;
}
