/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.IOException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandFailedException;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;


public class BaseProject
{
	public BaseProject()
	{
		diagramModel = new DiagramModel();
		storage = new Storage();
	}

	public DiagramModel getDiagramModel()
	{
		return diagramModel;
	}

	public void executeCommand(Command command) throws CommandFailedException
	{
		command.execute(this);
		recordCommand(command);
	}
	
	public void recordCommand(Command command)
	{
		try
		{
			storage.appendCommand(command);
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
	}

	public int deleteNode(int idToDelete) throws Exception
	{
		DiagramModel model = getDiagramModel();
		Node nodeToDelete = model.getNodeById(idToDelete);
		int nodeType = nodeToDelete.getNodeType();
		model.deleteNode(nodeToDelete);
		return nodeType; 
	}

	public int insertNode(int typeToInsert)
	{
		DiagramModel model = getDiagramModel();
		Node node = model.createNode(typeToInsert);
		int idThatWasInserted = model.getNodeId(node);
		return idThatWasInserted;
	}

	public int insertNodeAtId(int typeToInsert, int id)
	{
		DiagramModel model = getDiagramModel();
		Node node = model.createNodeAtId(typeToInsert, id);
		int idThatWasInserted = model.getNodeId(node);
		return idThatWasInserted;
	}

	public int insertLinkage(int linkFromId, int linkToId) throws Exception
	{
		DiagramModel model = getDiagramModel();
		Linkage linkage = model.createLinkage(Node.INVALID_ID, linkFromId, linkToId);
		int insertedLinkageId = model.getLinkageId(linkage);
		return insertedLinkageId;
	}

	public int insertLinkageAtId(int linkageId, int linkFromId, int linkToId) throws Exception
	{
		DiagramModel model = getDiagramModel();
		Linkage linkage = model.createLinkage(linkageId, linkFromId, linkToId);
		int insertedLinkageId = model.getLinkageId(linkage);
		return insertedLinkageId;
	}

	public void undo() throws CommandFailedException
	{
		int indexToUndo = getIndexToUndo();
		if(indexToUndo < 0)
			return;
		Command commandToUndo = storage.getCommand(indexToUndo);
		commandToUndo.undo(this);
	}
	
	public int getIndexToUndo()
	{
		int undoCount = 0;
		int candidateToUndo = storage.getCommandCount() - 1;
		while(candidateToUndo >= 0)
		{
			Command candidate = storage.getCommand(candidateToUndo);
			if(candidate.isUndo())
			{
				++undoCount;
			}
			else if(undoCount == 0)
			{
				break;
			}
			else
			{
				--undoCount;
			}
			--candidateToUndo;
		}
		
		return candidateToUndo;
	}

	Storage storage;
	DiagramModel diagramModel;
}
