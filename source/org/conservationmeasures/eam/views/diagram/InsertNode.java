/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.project.NodeCommandHelper;

abstract public class InsertNode extends LocationDoer
{
	abstract public NodeType getTypeToInsert();
	abstract public String getInitialText();
	abstract public void forceVisibleInLayerManager();

	public boolean isAvailable()
	{
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		try
		{
			DiagramNode[] selectedNodes = getProject().getOnlySelectedNodes();
			ModelNodeId id = insertNodeItself();
			if(selectedNodes.length > 0)
				linkToPreviouslySelectedNodes(id, selectedNodes);
			else
				notLinkingToAnyNodes();
			
			launchPropertiesEditor(new DiagramNodeId(id.asInt()));
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	void launchPropertiesEditor(DiagramNodeId id) throws Exception, CommandFailedException
	{
		DiagramNode newNode = getProject().getDiagramModel().getNodeById(id);
		getDiagramView().getPropertiesDoer().doNodeProperties(newNode, null);
	}
	
	ModelNodeId insertNodeItself() throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		NodeType nodeType = getTypeToInsert();
		ModelNodeId id = new NodeCommandHelper(getProject()).createNode(nodeType).getModelNodeId();
		DiagramNode addedNode = getProject().getDiagramModel().getNodeById(id);
		
		CommandSetObjectData setNameCommand = NodeCommandHelper.createSetLabelCommand(id, getInitialText());
		getProject().executeCommand(setNameCommand);
		
		Point createAt = getLocation();
		//Snap to Grid
		int deltaX = createAt.x;
		int deltaY = createAt.y;
		deltaX -= deltaX % getProject().getGridSize(); 
		deltaY -= deltaY % getProject().getGridSize();
		
		Command moveCommand = new CommandDiagramMove(deltaX, deltaY, new DiagramNodeId[] {addedNode.getDiagramNodeId()});
		getProject().executeCommand(moveCommand);
		
		doExtraSetup(id);
		
		getProject().executeCommand(new CommandEndTransaction());
		
		forceVisibleInLayerManager();
		getProject().updateVisibilityOfNodes();
		return id;
	}
	
	void linkToPreviouslySelectedNodes(ModelNodeId newlyInsertedId, DiagramNode[] nodesToLinkTo) throws CommandFailedException
	{
		getProject().executeCommand(new CommandBeginTransaction());
		for(int i = 0; i < nodesToLinkTo.length; ++i)
		{
			ModelNodeId toId = nodesToLinkTo[i].getWrappedId();
			InsertConnection.createModelLinkageAndAddToDiagramUsingCommands(getProject(), newlyInsertedId, toId);
		}
		getProject().executeCommand(new CommandEndTransaction());
	}
	
	void notLinkingToAnyNodes() throws CommandFailedException
	{
		
	}
	
	void doExtraSetup(BaseId id) throws CommandFailedException
	{
		
	}
	
	public DiagramView getDiagramView()
	{
		return (DiagramView)getView();
	}
}
