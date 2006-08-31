/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteLinkage;
import org.conservationmeasures.eam.commands.CommandDeleteNode;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.DiagramCluster;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelCluster;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Delete extends ProjectDoer
{
	public Delete()
	{
		super();
	}
	
	public Delete(Project project)
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
		try
		{
			for(int i=0; i < selectedRelatedCells.length; ++i)
			{
				EAMGraphCell cell = selectedRelatedCells[i];
				if(cell.isLinkage())
					deleteLinkage((DiagramLinkage)cell);	
			}
			
			for(int i=0; i < selectedRelatedCells.length; ++i)
			{
				EAMGraphCell cell = selectedRelatedCells[i];
				if(cell.isNode())
					deleteNode((DiagramNode)cell);
			}
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void deleteLinkage(DiagramLinkage linkageToDelete) throws CommandFailedException
	{
		BaseId id = linkageToDelete.getId();
		CommandDeleteLinkage command = new CommandDeleteLinkage(id);
		getProject().executeCommand(command);
	}

	// TODO: This method should be inside Project and should have unit tests
	private void deleteNode(DiagramNode nodeToDelete) throws Exception
	{
		BaseId id = nodeToDelete.getDiagramNodeId();

		Command[] commandsToRemoveFromView = getProject().getCurrentViewData().buildCommandsToRemoveNode(id);
		for(int i = 0; i < commandsToRemoveFromView.length; ++i)
			getProject().executeCommand(commandsToRemoveFromView[i]);
		
		DiagramCluster cluster = (DiagramCluster)nodeToDelete.getParent();
		if(cluster != null)
		{
			CommandSetObjectData removeFromCluster = CommandSetObjectData.createRemoveIdCommand(
					cluster.getUnderlyingObject(),
					ConceptualModelCluster.TAG_MEMBER_IDS, 
					id);
			getProject().executeCommand(removeFromCluster);
		}
		
		Command[] commandsToClear = nodeToDelete.buildCommandsToClear();
		for(int i = 0; i < commandsToClear.length; ++i)
			getProject().executeCommand(commandsToClear[i]);
		
		getProject().executeCommand(new CommandDeleteNode(id));
	}

}
