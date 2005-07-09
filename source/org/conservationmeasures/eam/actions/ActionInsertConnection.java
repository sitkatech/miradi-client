/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;

public class ActionInsertConnection extends MainWindowAction
{
	public ActionInsertConnection(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Connection...");
	}

	public void actionPerformed(ActionEvent event)
	{
		ConnectionPropertiesDialog dialog = new ConnectionPropertiesDialog(getMainWindow());
		dialog.show();
		if(!dialog.getResult())
			return;
		
		DiagramModel model = getMainWindow().getProject().getDiagramModel();
		int fromIndex = model.getNodeId(dialog.getFrom());
		int toIndex = model.getNodeId(dialog.getTo());
		CommandLinkNodes command = new CommandLinkNodes(fromIndex, toIndex);
		getMainWindow().getProject().executeCommand(command);
	}

}
