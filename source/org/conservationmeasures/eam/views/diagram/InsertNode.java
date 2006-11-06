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
		Point deltaPoint = new Point(DEFAULT_MOVE, DEFAULT_MOVE);
		
		DiagramComponent diagramComponent = getMainWindow().getDiagramComponent();
		Rectangle visibleRectangle = diagramComponent.getVisibleRect();

		DiagramNode[] selectedNodes = getProject().getOnlySelectedNodes();

		getProject().executeCommand(new CommandBeginTransaction());
		NodeType nodeType = getTypeToInsert();
		ModelNodeId id = new NodeCommandHelper(getProject()).createNode(nodeType).getModelNodeId();
		DiagramNode addedNode = getProject().getDiagramModel().getNodeById(id);

		CommandSetObjectData setNameCommand = NodeCommandHelper.createSetLabelCommand(id, getInitialText());
		getProject().executeCommand(setNameCommand);

		if (createAt != null)
			getMouseLocation(createAt, deltaPoint);
		else if (selectedNodes.length > 0 && !nodeType.isTarget())
			getLocationSelectedNoneTargetNode(DEFAULT_MOVE, deltaPoint, selectedNodes);
		else if (nodeType.isTarget())
			getTargetLocation(deltaPoint, visibleRectangle, addedNode);
		else
			getCenterLocation(deltaPoint, visibleRectangle);
		
		Command moveCommand = new CommandDiagramMove(deltaPoint.x, deltaPoint.y, new DiagramNodeId[] {addedNode.getDiagramNodeId()});
		getProject().executeCommand(moveCommand);

		doExtraSetup(id);
		getProject().executeCommand(new CommandEndTransaction());

		forceVisibleInLayerManager();
		getProject().updateVisibilityOfNodes();
		return id;
	}
	
	private void getCenterLocation(Point deltaPoint, Rectangle visibleRectangle)
	{
		int centeredWidth = visibleRectangle.width / 2;
		int centeredHeight = visibleRectangle.height / 2;
		
		deltaPoint.x = visibleRectangle.x + centeredWidth;
		deltaPoint.y = visibleRectangle.y + centeredHeight;
	}
	
	private void getTargetLocation(Point deltaPoint, Rectangle visibleRectangle, DiagramNode addedNode)
	{
		final int TARGET_SPACING = 20;
		final int RIGHT_SPACING = 10;
		DiagramModel diagramModel = getProject().getDiagramModel();
		DiagramNode[] allTargets = diagramModel.getAllTargetNodes();

		if (allTargets.length == 1)
		{
			int nodeWidth = addedNode.getRectangle().width + RIGHT_SPACING;
			deltaPoint.x = visibleRectangle.width - nodeWidth;
			deltaPoint.y = visibleRectangle.height / 3;
		}
		else
		{
			int highestY = 0;
			double y;
			for (int i = 0; i < allTargets.length; i++)
			{
				y = allTargets[i].getBounds().getY();
				if (y > highestY)
					highestY = (int)y;
			}

			deltaPoint.x = (int)allTargets[0].getBounds().getX();
			deltaPoint.y = highestY + (int)allTargets[0].getBounds().getHeight() + TARGET_SPACING;
		}
	}
	
	private void getLocationSelectedNoneTargetNode(final int DEFAULT_MOVE, Point deltaPoint, DiagramNode[] selectedNodes)
	{
		Point nodeLocation = selectedNodes[0].getLocation();
		deltaPoint.x = 0;
		double selectedNodeWidth = selectedNodes[0].getBounds().getWidth();
		if (nodeLocation.x - (DEFAULT_MOVE + selectedNodeWidth) > 0)
			deltaPoint.x = nodeLocation.x - DEFAULT_MOVE;
		
		deltaPoint.y = nodeLocation.y;
	}
	
	private void getMouseLocation(Point createAt, Point deltaPoint)
	{
		deltaPoint.x = createAt.x;
		deltaPoint.y = createAt.y;
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
