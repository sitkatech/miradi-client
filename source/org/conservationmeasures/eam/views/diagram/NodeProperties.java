/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetIndicator;
import org.conservationmeasures.eam.commands.CommandSetNodeObjectives;
import org.conservationmeasures.eam.commands.CommandSetNodePriority;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.NodePropertiesDialog;
import org.conservationmeasures.eam.views.ProjectDoer;

public class NodeProperties extends ProjectDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		EAMGraphCell[] selected = getProject().getOnlySelectedCells();
		if(selected.length != 1)
			return false;
		if(!selected[0].isNode())
			return false;
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		EAMGraphCell[] selected = getProject().getOnlySelectedCells();
		DiagramNode selectedNode = (DiagramNode)selected[0];
		if(selectedNode == null)
			return;
		String title = EAM.text("Title|Node Properties");
		NodePropertiesDialog dlg = new NodePropertiesDialog(EAM.mainWindow, title, selectedNode);
		dlg.setVisible(true);
		if(!dlg.getResult())
			return;

		int id = selectedNode.getId();
		getProject().executeCommand(new CommandBeginTransaction());
		getProject().executeCommand(new CommandSetNodeText(id, dlg.getText()));
		getProject().executeCommand(new CommandSetIndicator(id, dlg.getIndicator()));
		if(selectedNode.canHavePriority())
			getProject().executeCommand(new CommandSetNodePriority(id, dlg.getPriority()));
		if(selectedNode.canHaveObjectives())
			getProject().executeCommand(new CommandSetNodeObjectives(id, dlg.getObjectives()));
		getProject().executeCommand(new CommandEndTransaction());
	}

}
