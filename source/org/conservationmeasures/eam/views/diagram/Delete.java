/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteLinkage;
import org.conservationmeasures.eam.commands.CommandDeleteNode;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Delete extends ProjectDoer
{
	public Delete()
	{
		super();
	}
	
	public Delete(BaseProject project)
	{
		setProject(project);
	}
	
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;

		EAMGraphCell[] selected = getProject().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		EAMGraphCell[] selectedRelatedCells = getProject().getSelectedAndRelatedCells();
		getProject().executeCommand(new CommandBeginTransaction());
		for(int i=0; i < selectedRelatedCells.length; ++i)
		{
			EAMGraphCell cell = selectedRelatedCells[i];
			if(cell.isLinkage())
				deleteLinkage((Linkage)cell);	
		}
		
		for(int i=0; i < selectedRelatedCells.length; ++i)
		{
			EAMGraphCell cell = selectedRelatedCells[i];
			if(cell.isNode())
				deleteNode((Node)cell);
		}
		getProject().executeCommand(new CommandEndTransaction());
	}

	private void deleteLinkage(Linkage linkageToDelete) throws CommandFailedException
	{
		int id = linkageToDelete.getId();
		CommandDeleteLinkage command = new CommandDeleteLinkage(id);
		getProject().executeCommand(command);
	}

	private void deleteNode(Node nodeToDelete) throws CommandFailedException
	{
		int id = nodeToDelete.getId();

		Point location = nodeToDelete.getLocation();
		CommandDiagramMove moveToZeroZero = new CommandDiagramMove(-location.x, -location.y, new int[] {id});
		CommandSetNodeText clearText = new CommandSetNodeText(id, "");
		CommandDeleteNode command = new CommandDeleteNode(id);
		
		getProject().executeCommand(moveToZeroZero);
		getProject().executeCommand(clearText);
		getProject().executeCommand(command);
	}

}
