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
import org.conservationmeasures.eam.main.EAM;
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
			EAM.logException(e);
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
			deltaPoint = getMouseLocation(createAt, deltaPoint);
		else if (selectedNodes.length > 0 && !nodeType.isTarget())
			deltaPoint = getLocationSelectedNonTargetNode(DEFAULT_MOVE, deltaPoint, selectedNodes, (int)addedNode.getBounds().getWidth());
		else if (nodeType.isTarget())
			deltaPoint = getTargetLocation(deltaPoint, visibleRectangle, addedNode);
		else
			deltaPoint = getCenterLocation(deltaPoint, visibleRectangle);
		
		Command moveCommand = new CommandDiagramMove(deltaPoint.x, deltaPoint.y, new DiagramNodeId[] {addedNode.getDiagramNodeId()});
		getProject().executeCommand(moveCommand);

		doExtraSetup(id);
		getProject().executeCommand(new CommandEndTransaction());

		forceVisibleInLayerManager();
		getProject().updateVisibilityOfNodes();
		return id;
	}
	
	private Point getCenterLocation(Point deltaPointToUse, Rectangle visibleRectangle)
	{
		Point deltaPoint = new Point(deltaPointToUse);
		int centeredWidth = visibleRectangle.width / 2;
		int centeredHeight = visibleRectangle.height / 2;
		
		deltaPoint.x = visibleRectangle.x + centeredWidth;
		deltaPoint.y = visibleRectangle.y + centeredHeight;
		
		return deltaPoint;
	}
	
	private Point getTargetLocation(Point deltaPointToUse, Rectangle visibleRectangle, DiagramNode addedNode)
	{
		Point deltaPoint = new Point(deltaPointToUse);
		final int TARGET_TOP_LOCATION = 150;
		final int TARGET_BETWEEN_SPACING = 20;
		final int TARGET_RIGHT_SPACING = 10;
		DiagramModel diagramModel = getProject().getDiagramModel();
		DiagramNode[] allTargets = diagramModel.getAllTargetNodes();

		if (allTargets.length == 1)
		{
			int nodeWidth = addedNode.getRectangle().width;
			deltaPoint.x = visibleRectangle.width - TARGET_RIGHT_SPACING - nodeWidth;
			deltaPoint.y = TARGET_TOP_LOCATION;
		}
		else
		{
			int highestY = 0;
			double y;
			for (int i = 0; i < allTargets.length; i++)
			{
				y = allTargets[i].getBounds().getY();
				highestY = (int)Math.max(highestY, y);
			}

			deltaPoint.x = (int)allTargets[0].getBounds().getX();
			deltaPoint.y = highestY + (int)allTargets[0].getBounds().getHeight() + TARGET_BETWEEN_SPACING;
		}
		
		return deltaPoint;
	}
	
	private Point getLocationSelectedNonTargetNode(final int DEFAULT_MOVE, Point deltaPointToUse, DiagramNode[] selectedNodes, int nodeWidth)
	{
		Point deltaPoint = new Point(deltaPointToUse);
		Point nodeLocation = selectedNodes[0].getLocation();
		deltaPoint.x = 0;
		deltaPoint.x = nodeLocation.x - (DEFAULT_MOVE + nodeWidth);
		deltaPoint.x = Math.max(0, deltaPoint.x);
		deltaPoint.y = nodeLocation.y;
		return deltaPoint;
	}
	
	private Point getMouseLocation(Point createAt, Point deltaPointToUse)
	{
		Point deltaPoint = new Point(deltaPointToUse);
		deltaPoint.x = createAt.x;
		deltaPoint.y = createAt.y;
		return deltaPoint;
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
