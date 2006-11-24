/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Rectangle;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetFactorSize;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorCluster;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.FactorCluster;

public class FactorMoveHandler
{
	public FactorMoveHandler(Project projectToUse)
	{
		project = projectToUse;
	}

	public void nodesWereMovedOrResized(int deltaX, int deltaY, DiagramFactorId[] ids) throws CommandFailedException
	{
		DiagramModel model = getProject().getDiagramModel();
		model.factorsWereMoved(ids);

		Vector commandsToRecord = new Vector();
		Vector commandsToExecute = new Vector();
		Vector movedNodes = new Vector();
		for(int i = 0 ; i < ids.length; ++i)
		{
			try 
			{
				DiagramFactor node = model.getDiagramFactorById(ids[i]);
				if(node.getParent() != null)
					commandsToExecute.addAll(buildDetachFromClusterCommand(node));
				if(node.getParent() == null)
					commandsToExecute.addAll(buildAttachToClusterCommand(node));
				
				if(node.hasMoved())
					movedNodes.add(node);
				
				if(node.sizeHasChanged())
					commandsToRecord.add(buildResizeCommand(node));
			} 
			catch (Exception e) 
			{
				EAM.logException(e);
			}
		}
		
		if(movedNodes.size() > 0)
		{
			DiagramFactorId[] idsActuallyMoved = new DiagramFactorId[movedNodes.size()];
			for(int i = 0; i < movedNodes.size(); ++i)
			{
				DiagramFactor node = (DiagramFactor)movedNodes.get(i);
				idsActuallyMoved[i] = node.getDiagramFactorId();
			}
			
			commandsToRecord.add(new CommandDiagramMove(deltaX, deltaY, idsActuallyMoved));
		}
		
		if(commandsToRecord.size() > 0 || commandsToExecute.size() > 0)
		{
			getProject().recordCommand(new CommandBeginTransaction());
			for(int i=0; i < commandsToRecord.size(); ++i)
				getProject().recordCommand((Command)commandsToRecord.get(i));
			for(int i=0; i < commandsToExecute.size(); ++i)
				getProject().executeCommand((Command)commandsToExecute.get(i));
			getProject().recordCommand(new CommandEndTransaction());
		}
		
		/*
		 * NOTE: The following chunk of code works around a weird bug deep in jgraph
		 * If you click on a cluster, then click on a member, then drag the member out,
		 * part of jgraph still thinks the cluster has a member that is selected.
		 * So when you drag the node back in, it doesn't become a member because jgraph 
		 * won't return the cluster, because it thinks the cluster has something selected.
		 * The workaround is to re-select what is selected, so the cached values inside 
		 * jgraph get reset to their proper values.
		 */
		MainWindow mainWindow = EAM.mainWindow;
		if(mainWindow != null)
		{
			DiagramComponent diagram = mainWindow.getDiagramComponent();
			diagram.setSelectionCells(diagram.getSelectionCells());
		}

	}
	
	private List buildAttachToClusterCommand(DiagramFactor node) throws Exception
	{
		Vector result = new Vector();
		if(node.isFactorCluster())
			return result;
		
		DiagramFactorCluster cluster = getFirstClusterThatContains(node.getRectangle());
		if(cluster == null)
			return result;
		
		// FIXME: It looks wrong to mix commands with a non-command call like addNodeToCluster()
		getProject().addNodeToCluster(cluster, node);
		CommandSetObjectData cmd = CommandSetObjectData.createAppendIdCommand(cluster.getUnderlyingObject(), 
				FactorCluster.TAG_MEMBER_IDS, node.getDiagramFactorId());
		result.add(cmd);
		return result;
	}
	
	private List buildDetachFromClusterCommand(DiagramFactor node) throws ParseException
	{
		Vector result = new Vector();
		DiagramFactorCluster cluster = (DiagramFactorCluster)node.getParent();
		if(cluster.getRectangle().contains(node.getRectangle()))
			return result;
		
		// FIXME: It looks wrong to mix commands with a non-command call like removeNodeFromCluster()
		getProject().removeNodeFromCluster(cluster, node);
		CommandSetObjectData cmd = CommandSetObjectData.createRemoveIdCommand(cluster.getUnderlyingObject(), 
				FactorCluster.TAG_MEMBER_IDS, node.getDiagramFactorId());
		result.add(cmd);
		return result;
	}
	
	private DiagramFactorCluster getFirstClusterThatContains(Rectangle candidateRect) throws Exception
	{
		DiagramModel model = getProject().getDiagramModel();
		Vector allNodes = model.getAllDiagramFactors();
		for(int i = 0; i < allNodes.size(); ++i)
		{
			DiagramFactor node = (DiagramFactor)allNodes.get(i);
			DiagramFactor possibleCluster = model.getDiagramFactorByWrappedId(node.getWrappedId());
			if(!possibleCluster.isFactorCluster())
				continue;
			
			if(possibleCluster.getRectangle().contains(candidateRect))
				return (DiagramFactorCluster)possibleCluster;
		}
		
		return null;
	}
	
	private Command buildResizeCommand(DiagramFactor node)
	{
		return new CommandSetFactorSize(node.getDiagramFactorId(), node.getSize(), node.getPreviousSize());
	}
	

	Project getProject()
	{
		return project;
	}
	
	Project project;
}
