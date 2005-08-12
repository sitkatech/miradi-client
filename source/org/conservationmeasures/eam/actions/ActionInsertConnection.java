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
import org.conservationmeasures.eam.icons.InsertConnectionIcon;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertConnection extends MainWindowAction
{
	public ActionInsertConnection(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertConnectionIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Connection...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Add a relationship between two nodes");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		ConnectionPropertiesDialog dialog = new ConnectionPropertiesDialog(EAM.mainWindow);
		dialog.setVisible(true);
		if(!dialog.getResult())
			return;
		
		DiagramModel model = getProject().getDiagramModel();
		int fromIndex = dialog.getFrom().getId();
		int toIndex = dialog.getTo().getId();
		
		if(fromIndex == toIndex)
		{
			String[] body = {EAM.text("Can't link a node to itself"), };
			EAM.okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
		
		if(model.hasLinkage(dialog.getFrom(), dialog.getTo()))
		{
			String[] body = {EAM.text("Those nodes are already linked"), };
			EAM.okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
		
		try
		{
			CommandLinkNodes command = new CommandLinkNodes(fromIndex, toIndex);
			getProject().executeCommand(command);
		}
		catch (CommandFailedException e)
		{
		}
	}

	public boolean shouldBeEnabled()
	{
		if(!getProject().isOpen())
			return false;
		
		return (getProject().getDiagramModel().getCellCount() >= 2);
	}
}

