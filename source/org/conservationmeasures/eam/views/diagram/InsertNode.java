/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.awt.Rectangle;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
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

	private ModelNodeId insertNodeItself() throws Exception
	{
		Point createAt = getLocation();
		final int DEFAULT_MOVE = 150;
		final int TARGET_SPACING = 20;
		final int RIGHT_SPACING = 10;
		
		int deltaX = DEFAULT_MOVE;
		int deltaY = DEFAULT_MOVE;
		DiagramComponent diagramComponent = getMainWindow().getDiagramComponent();
		Rectangle rect = diagramComponent.getVisibleRect();

		DiagramNode[] selectedNodes = getProject().getOnlySelectedNodes();

		getProject().executeCommand(new CommandBeginTransaction());
		NodeType nodeType = getTypeToInsert();
		ModelNodeId id = new NodeCommandHelper(getProject()).createNode(nodeType).getModelNodeId();
		DiagramNode addedNode = getProject().getDiagramModel().getNodeById(id);

		CommandSetObjectData setNameCommand = NodeCommandHelper.createSetLabelCommand(id, getInitialText());
		getProject().executeCommand(setNameCommand);

		if (createAt != null)
		{
			//Snap to Grid
			deltaX = createAt.x;
			deltaY = createAt.y;
		}
		else if (selectedNodes.length > 0 && !nodeType.isTarget())
		{
			Point nodeLocation = selectedNodes[0].getLocation();
			deltaX = 0;
			double selectedNodeWidth = selectedNodes[0].getBounds().getWidth();
			if (nodeLocation.x - (DEFAULT_MOVE - selectedNodeWidth) > 0)
				deltaX = nodeLocation.x - DEFAULT_MOVE;

			deltaY = nodeLocation.y;
		}
		else if (nodeType.isTarget())
		{
			DiagramModel diagramModel = getProject().getDiagramModel();
			DiagramNode[] allTargets = diagramModel.getAllTargetNodes();

			if (allTargets.length - 1 > 0)
			{
				int highestY = 0;
				double y;
				for (int i = 0; i < allTargets.length; i++)
				{
					y = allTargets[i].getBounds().getY();
					if (y > highestY)
						highestY = (int)y;
				}

				deltaX = (int)allTargets[0].getBounds().getX();
				deltaY = highestY + (int)allTargets[0].getBounds().getHeight() + TARGET_SPACING;
			}
			else
			{
				int nodeWidth = addedNode.getRectangle().width + RIGHT_SPACING;
				deltaX = rect.width - nodeWidth;
				deltaY = rect.height / 3;
			}
		}
		else
		{
			int centeredWidth = rect.width / 2;
			int centeredHeight = rect.height / 2;
			deltaX = rect.x + centeredWidth;
			deltaY = rect.y + centeredHeight;
		}

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
