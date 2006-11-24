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
import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactor;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetFactorSize;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.FactorLinkDataMap;
import org.conservationmeasures.eam.diagram.cells.FactorDataHelper;
import org.conservationmeasures.eam.diagram.cells.FactorDataMap;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.utils.Logging;
import org.conservationmeasures.eam.views.diagram.InsertFactorLinkDoer;

public class FactorCommandHelper
{
	public FactorCommandHelper(Project projectToUse)
	{
		project = projectToUse;
	}

	public CommandDiagramAddFactor createNode(FactorType nodeType) throws Exception
	{
		CreateFactorParameter extraInfo = new CreateFactorParameter(nodeType);
		CommandCreateObject createModelNode = new CommandCreateObject(ObjectType.MODEL_NODE, extraInfo);
		executeCommand(createModelNode);
		FactorId modelNodeId = new FactorId(createModelNode.getCreatedId().asInt());

		CommandDiagramAddFactor commandInsertNode = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), modelNodeId);
		executeCommand(commandInsertNode);
		Command[] commandsToAddToView = getProject().getCurrentViewData().buildCommandsToAddNode(modelNodeId);
		for(int i = 0; i < commandsToAddToView.length; ++i)
			executeCommand(commandsToAddToView[i]);
		return commandInsertNode;
	}


	public void pasteNodesAndLinksIntoProject(TransferableEamList list, Point startPoint) throws Exception
	{
		executeCommand(new CommandBeginTransaction());
		FactorDataHelper dataHelper = new FactorDataHelper(getDiagramModel().getAllNodes());
		pasteNodesIntoProject(list, startPoint, dataHelper);
		pasteLinksIntoProject(list, dataHelper);
		executeCommand(new CommandEndTransaction());
	}
	
	public void pasteNodesOnlyIntoProject(TransferableEamList list, Point startPoint) throws Exception
	{
		executeCommand(new CommandBeginTransaction());
		FactorDataHelper dataHelper = new FactorDataHelper(getDiagramModel().getAllNodes());
		pasteNodesIntoProject(list, startPoint, dataHelper);
		executeCommand(new CommandEndTransaction());
	}

	private void pasteNodesIntoProject(TransferableEamList list, Point startPoint, FactorDataHelper dataHelper) throws Exception 
	{
		FactorDataMap[] nodes = list.getNodeDataCells();
		for (int i = 0; i < nodes.length; i++) 
		{
			FactorDataMap nodeData = nodes[i];
			DiagramFactorId originalDiagramNodeId = new DiagramFactorId(nodeData.getId(DiagramFactor.TAG_ID).asInt());
			
			FactorType type = FactorDataMap.convertIntToNodeType(nodeData.getInt(DiagramFactor.TAG_NODE_TYPE)); 
			CommandDiagramAddFactor addCommand = createNode(type);
			DiagramFactorId newNodeId = addCommand.getInsertedId();
			dataHelper.setNewId(originalDiagramNodeId, newNodeId);
			dataHelper.setOriginalLocation(originalDiagramNodeId, nodeData.getPoint(DiagramFactor.TAG_LOCATION));
			
			CommandSetObjectData newNodeLabel = createSetLabelCommand(addCommand.getFactorId(), nodeData.getString(DiagramFactor.TAG_VISIBLE_LABEL));
			executeCommand(newNodeLabel);
			Logging.logDebug("Paste Node: " + newNodeId +":" + nodeData.getString(DiagramFactor.TAG_VISIBLE_LABEL));
			
			
		}
		
		for (int i = 0; i < nodes.length; i++) 
		{
			FactorDataMap nodeData = nodes[i];
			DiagramFactorId originalDiagramNodeId = new DiagramFactorId(nodeData.getId(DiagramFactor.TAG_ID).asInt());
			
			Point newNodeLocation = dataHelper.getNewLocation(originalDiagramNodeId, startPoint);
			newNodeLocation = getProject().getSnapped(newNodeLocation);
			DiagramFactorId newNodeId = dataHelper.getNewId(originalDiagramNodeId);
			
			DiagramFactor newNode = getDiagramNodeById(newNodeId);
			Dimension originalDimension = nodeData.getDimension(DiagramFactor.TAG_SIZE);
			CommandSetFactorSize resize = new CommandSetFactorSize(newNodeId, originalDimension, newNode.getSize());
			executeCommand(resize);
			
			DiagramFactorId newDiagramNodeId = getDiagramNodeById(newNodeId).getDiagramNodeId();
			CommandDiagramMove move = new CommandDiagramMove(newNodeLocation.x, newNodeLocation.y, new DiagramFactorId[]{newDiagramNodeId});
			executeCommand(move);
		}
	}

	private DiagramFactor getDiagramNodeById(DiagramFactorId newNodeId) throws Exception
	{
		return getDiagramModel().getNodeById(newNodeId);
	}
	
	private void pasteLinksIntoProject(TransferableEamList list, FactorDataHelper dataHelper) throws Exception 
	{
		FactorLinkDataMap[] links = list.getLinkageDataCells();
		for (int i = 0; i < links.length; i++) 
		{
			FactorLinkDataMap linkageData = links[i];
			
			DiagramFactorId oldFromDiagramId = linkageData.getFromId();
			DiagramFactorId newFromId = dataHelper.getNewId(oldFromDiagramId);
			DiagramFactorId newToId = dataHelper.getNewId(linkageData.getToId());
			if(newFromId.isInvalid() || newToId.isInvalid())
			{
				Logging.logWarning("Unable to Paste Link : from OriginalId:" + linkageData.getFromId() + " to OriginalId:" + linkageData.getToId()+" node deleted?");	
				continue;
			}
			
			DiagramFactor newFromNode = getDiagramNodeById(newFromId);
			DiagramFactor newToNode = getDiagramNodeById(newToId);
			CommandDiagramAddFactorLink addLinkageCommand = InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(project, newFromNode.getWrappedId(), newToNode.getWrappedId());
			Logging.logDebug("Paste Link : " + addLinkageCommand.getFactorLinkId() + " from:" + newFromId + " to:" + newToId);
		}
	}
	
	public static CommandSetObjectData createSetLabelCommand(FactorId id, String newLabel)
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
