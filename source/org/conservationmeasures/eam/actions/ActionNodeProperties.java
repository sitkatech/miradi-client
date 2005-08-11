/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.NodePropertiesDialog;

public class ActionNodeProperties extends ProjectAction
{
	public ActionNodeProperties(BaseProject projectToUse)
	{
		super(projectToUse, getLabel(), "icons/edit.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Properties...");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		editProperties();
	}

	public void editProperties() throws CommandFailedException 
	{
		EAMGraphCell[] selected = getProject().getOnlySelectedCells();
		if(selected.length != 1)
			return;
		if(!selected[0].isNode())
			return;
		
		Node selectedNode = (Node)selected[0];
		if(selectedNode == null)
			return;
		String title = EAM.text("Title|Node Properties");
		NodePropertiesDialog dlg = new NodePropertiesDialog(EAM.mainWindow, title, selectedNode);
		dlg.setVisible(true);
		if(!dlg.getResult())
			return;

		int id = selectedNode.getId();
		CommandSetNodeText command = new CommandSetNodeText(id, dlg.getText());
		getProject().executeCommand(command);
	}
}
