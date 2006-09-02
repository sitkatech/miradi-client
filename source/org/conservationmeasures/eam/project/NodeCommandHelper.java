/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Point;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.commands.CommandSetNodeName;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.LinkageDataMap;
import org.conservationmeasures.eam.diagram.nodes.NodeDataHelper;
import org.conservationmeasures.eam.diagram.nodes.NodeDataMap;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.utils.Logging;

public class NodeCommandHelper
{
	public NodeCommandHelper(Project projectToUse)
	{
		project = projectToUse;
	}

	public ModelNodeId createNode(NodeType nodeType) throws Exception
	{
		CommandInsertNode commandInsertNode = new CommandInsertNode(nodeType);
		executeCommand(commandInsertNode);
		ModelNodeId id = commandInsertNode.getId();
		Command[] commandsToAddToView = getProject().getCurrentViewData().buildCommandsToAddNode(id);
		for(int i = 0; i < commandsToAddToView.length; ++i)
			executeCommand(commandsToAddToView[i]);
		return id;
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
			BaseId originalNodeId = nodeData.getId(DiagramNode.TAG_ID);
			
			NodeType type = NodeDataMap.convertIntToNodeType(nodeData.getInt(DiagramNode.TAG_NODE_TYPE)); 
			ModelNodeId newNodeId = createNode(type);
			dataHelper.setNewId(originalNodeId, newNodeId);
			dataHelper.setOriginalLocation(originalNodeId, nodeData.getPoint(DiagramNode.TAG_LOCATION));
			
			CommandSetNodeName newNodeLabel = new CommandSetNodeName(newNodeId, nodeData.getString(DiagramNode.TAG_VISIBLE_LABEL));
			executeCommand(newNodeLabel);
			Logging.logDebug("Paste Node: " + newNodeId +":" + nodeData.getString(DiagramNode.TAG_VISIBLE_LABEL));
		}
		
		for (int i = 0; i < nodes.length; i++) 
		{
			NodeDataMap nodeData = nodes[i];
			Point newNodeLocation = dataHelper.getNewLocation(nodeData.getId(DiagramNode.TAG_ID), startPoint);
			newNodeLocation = getProject().getSnapped(newNodeLocation);
			BaseId newNodeId = dataHelper.getNewId(nodeData.getId(DiagramNode.TAG_ID));
			CommandDiagramMove move = new CommandDiagramMove(newNodeLocation.x, newNodeLocation.y, new BaseId[]{newNodeId});
			executeCommand(move);
		}
	}
	
	private void pasteLinksIntoProject(TransferableEamList list, NodeDataHelper dataHelper) throws CommandFailedException 
	{
		LinkageDataMap[] links = list.getLinkageDataCells();
		for (int i = 0; i < links.length; i++) 
		{
			LinkageDataMap linkageData = links[i];
			
			BaseId newFromId = dataHelper.getNewId(linkageData.getFromId());
			BaseId newToId = dataHelper.getNewId(linkageData.getToId());
			if(newFromId.isInvalid() || newToId.isInvalid())
			{
				Logging.logWarning("Unable to Paste Link : from OriginalId:" + linkageData.getFromId() + " to OriginalId:" + linkageData.getToId()+" node deleted?");	
				continue;
			}
			CommandLinkNodes link = new CommandLinkNodes(new ModelNodeId(newFromId.asInt()), new ModelNodeId(newToId.asInt()));
			executeCommand(link);
			Logging.logDebug("Paste Link : " + link.getLinkageId() + " from:" + link.getFromId() + " to:" + link.getToId());
		}
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
