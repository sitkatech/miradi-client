/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.ProjectDoer;

public class InsertConnection extends ProjectDoer
{
	public InsertConnection(BaseProject project)
	{
		super(project);
	}

	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		return (getProject().getDiagramModel().getCellCount() >= 2);
	}

	public void doIt() throws CommandFailedException
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

}
