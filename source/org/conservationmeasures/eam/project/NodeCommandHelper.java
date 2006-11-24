/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Dimension;
import java.awt.Point;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddLinkage;
import org.conservationmeasures.eam.commands.CommandDiagramAddNode;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetNodeSize;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.LinkageDataMap;
import org.conservationmeasures.eam.diagram.nodes.NodeDataHelper;
import org.conservationmeasures.eam.diagram.nodes.NodeDataMap;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.utils.Logging;
import org.conservationmeasures.eam.views.diagram.InsertConnection;

public class NodeCommandHelper
{
	public NodeCommandHelper(Project projectToUse)
	{
		project = projectToUse;
	}

	public CommandDiagramAddNode createNode(NodeType nodeType) throws Exception
	{
		CreateModelNodeParameter extraInfo = new CreateModelNodeParameter(nodeType);
		CommandCreateObject createModelNode = new CommandCreateObject(ObjectType.MODEL_NODE, extraInfo);
		executeCommand(createModelNode);
		ModelNodeId modelNodeId = new ModelNodeId(createModelNode.getCreatedId().asInt());

		CommandDiagramAddNode commandInsertNode = new CommandDiagramAddNode(new DiagramNodeId(BaseId.INVALID.asInt()), modelNodeId);
		executeCommand(commandInsertNode);
		Command[] commandsToAddToView = getProject().getCurrentViewData().buildCommandsToAddNode(modelNodeId);
		for(int i = 0; i < commandsToAddToView.length; ++i)
			executeCommand(commandsToAddToView[i]);
		return commandInsertNode;
	}


	public void pasteNodesAndLinksIntoProject(TransferableEamList list, Point startPoint) throws Exception
	{
		executeCommand(new CommandBeginTransaction());
		NodeDataHelper dataHelper = new NodeDataHelper(getDiagramModel().getAllNodes());
		pasteNodesIntoProject(list, startPoint, dataHelper);
		pasteLinksIntoProject(list, dataHelper);
		executeCommand(new CommandEndTransaction());
	}
	
	public void pasteNodesOnlyIntoProject(TransferableEamList list, Point startPoint) throws Exception
	{
		executeCommand(new CommandBeginTransaction());
		NodeDataHelper dataHelper = new NodeDataHelper(getDiagramModel().getAllNodes());
		pasteNodesIntoProject(list, startPoint, dataHelper);
		executeCommand(new CommandEndTransaction());
	}

	private void pasteNodesIntoProject(TransferableEamList list, Point startPoint, NodeDataHelper dataHelper) throws Exception 
	{
		NodeDataMap[] nodes = list.getNodeDataCells();
		for (int i = 0; i < nodes.length; i++) 
		{
			NodeDataMap nodeData = nodes[i];
			DiagramNodeId originalDiagramNodeId = new DiagramNodeId(nodeData.getId(DiagramNode.TAG_ID).asInt());
			
			NodeType type = NodeDataMap.convertIntToNodeType(nodeData.getInt(DiagramNode.TAG_NODE_TYPE)); 
			CommandDiagramAddNode addCommand = createNode(type);
			DiagramNodeId newNodeId = addCommand.getInsertedId();
			dataHelper.setNewId(originalDiagramNodeId, newNodeId);
			dataHelper.setOriginalLocation(originalDiagramNodeId, nodeData.getPoint(DiagramNode.TAG_LOCATION));
			
			CommandSetObjectData newNodeLabel = createSetLabelCommand(addCommand.getModelNodeId(), nodeData.getString(DiagramNode.TAG_VISIBLE_LABEL));
			executeCommand(newNodeLabel);
			Logging.logDebug("Paste Node: " + newNodeId +":" + nodeData.getString(DiagramNode.TAG_VISIBLE_LABEL));
			
			
		}
		
		for (int i = 0; i < nodes.length; i++) 
		{
			NodeDataMap nodeData = nodes[i];
			DiagramNodeId originalDiagramNodeId = new DiagramNodeId(nodeData.getId(DiagramNode.TAG_ID).asInt());
			
			Point newNodeLocation = dataHelper.getNewLocation(originalDiagramNodeId, startPoint);
			newNodeLocation = getProject().getSnapped(newNodeLocation);
			DiagramNodeId newNodeId = dataHelper.getNewId(originalDiagramNodeId);
			
			DiagramNode newNode = getDiagramNodeById(newNodeId);
			Dimension originalDimension = nodeData.getDimension(DiagramNode.TAG_SIZE);
			CommandSetNodeSize resize = new CommandSetNodeSize(newNodeId, originalDimension, newNode.getSize());
			executeCommand(resize);
			
			DiagramNodeId newDiagramNodeId = getDiagramNodeById(newNodeId).getDiagramNodeId();
			CommandDiagramMove move = new CommandDiagramMove(newNodeLocation.x, newNodeLocation.y, new DiagramNodeId[]{newDiagramNodeId});
			executeCommand(move);
		}
	}

	private DiagramNode getDiagramNodeById(DiagramNodeId newNodeId) throws Exception
	{
		return getDiagramModel().getNodeById(newNodeId);
	}
	
	private void pasteLinksIntoProject(TransferableEamList list, NodeDataHelper dataHelper) throws Exception 
	{
		LinkageDataMap[] links = list.getLinkageDataCells();
		for (int i = 0; i < links.length; i++) 
		{
			LinkageDataMap linkageData = links[i];
			
			DiagramNodeId oldFromDiagramId = linkageData.getFromId();
			DiagramNodeId newFromId = dataHelper.getNewId(oldFromDiagramId);
			DiagramNodeId newToId = dataHelper.getNewId(linkageData.getToId());
			if(newFromId.isInvalid() || newToId.isInvalid())
			{
				Logging.logWarning("Unable to Paste Link : from OriginalId:" + linkageData.getFromId() + " to OriginalId:" + linkageData.getToId()+" node deleted?");	
				continue;
			}
			
			DiagramNode newFromNode = getDiagramNodeById(newFromId);
			DiagramNode newToNode = getDiagramNodeById(newToId);
			CommandDiagramAddLinkage addLinkageCommand = InsertConnection.createModelLinkageAndAddToDiagramUsingCommands(project, newFromNode.getWrappedId(), newToNode.getWrappedId());
			Logging.logDebug("Paste Link : " + addLinkageCommand.getModelLinkageId() + " from:" + newFromId + " to:" + newToId);
		}
	}
	
	public static CommandSetObjectData createSetLabelCommand(ModelNodeId id, String newLabel)
	{
		int type = ObjectType.MODEL_NODE;
		String tag = Factor.TAG_LABEL;
		CommandSetObjectData cmd = new CommandSetObjectData(type, id, tag, newLabel);
		return cmd;
	}

	private Project getProject()
	{
		return project;
	}
	
	private void executeCommand(Command cmd) throws CommandFailedException
	{
		getProject().executeCommand(cmd);
	}
	
	private DiagramModel getDiagramModel()
	{
		return getProject().getDiagramModel();
	}
	
	Project project;
}
