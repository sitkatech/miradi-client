/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandDeleteLinkage;
import org.conservationmeasures.eam.commands.CommandDeleteNode;
import org.conservationmeasures.eam.commands.CommandFailedException;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;
import org.conservationmeasures.eam.main.Project;

public class ActionDelete extends MainWindowAction
{
	public ActionDelete(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/delete.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Delete");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		Object[] selectedCells = getMainWindow().getDiagramComponent().getSelectionCells();
		if(selectedCells.length != 1)
		{
			String[] body = {EAM.text("Must select exactly one node or linkage to delete"),};
			getMainWindow().okDialog(EAM.text("Can't Delete"), body);
			return;
		}
		
		EAMGraphCell cell = (EAMGraphCell)selectedCells[0];
		if(cell.isLinkage())
			deleteLinkage((Linkage)cell);
		else if(cell.isNode())
			deleteNode((Node)cell);
	}
	
	private void deleteLinkage(Linkage linkageToDelete) throws CommandFailedException
	{
		Project project = getMainWindow().getProject();
		int id = project.getDiagramModel().getLinkageId(linkageToDelete);
		CommandDeleteLinkage command = new CommandDeleteLinkage(id);
		project.executeCommand(command);
	}

	private void deleteNode(Node nodeToDelete) throws CommandFailedException
	{
		Project project = getMainWindow().getProject();
		int id = project.getDiagramModel().getNodeId(nodeToDelete);
		CommandDeleteNode command = new CommandDeleteNode(id);
		project.executeCommand(command);
	}

}
