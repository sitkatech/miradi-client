/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

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

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		ConnectionPropertiesDialog dialog = new ConnectionPropertiesDialog(getMainWindow());
		dialog.setVisible(true);
		if(!dialog.getResult())
			return;
		
		DiagramModel model = getMainWindow().getProject().getDiagramModel();
		int fromIndex = model.getNodeId(dialog.getFrom());
		int toIndex = model.getNodeId(dialog.getTo());
		
		if(fromIndex == toIndex)
		{
			String[] body = {EAM.text("Can't link a node to itself"), };
			getMainWindow().okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
		
		if(model.hasLinkage(dialog.getFrom(), dialog.getTo()))
		{
			String[] body = {EAM.text("Those nodes are already linked"), };
			getMainWindow().okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
		
		try
		{
			CommandLinkNodes command = new CommandLinkNodes(fromIndex, toIndex);
			getMainWindow().getProject().executeCommand(command);
		}
		catch (CommandFailedException e)
		{
		}
	}

	public boolean shouldBeEnabled()
	{
		return getMainWindow().isProjectOpen();
	}
	
}
