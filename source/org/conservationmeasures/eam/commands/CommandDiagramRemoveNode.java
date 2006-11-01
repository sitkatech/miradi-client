/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class CommandDiagramRemoveNode extends Command
{
	public CommandDiagramRemoveNode(DiagramNodeId idToDelete)
	{
		diagramNodeId = idToDelete;
		modelNodeId = new ModelNodeId(BaseId.INVALID.asInt());
	}

	public String toString()
	{
		return getCommandName() + ":" + getDiagramNodeId();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			modelNodeId = target.removeNodeFromDiagram(diagramNodeId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void undo(Project target) throws CommandFailedException
	{
		try
		{
			target.addNodeToDiagram(modelNodeId, diagramNodeId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public DiagramNodeId getDiagramNodeId()
	{
		return diagramNodeId;
	}
	
	public ModelNodeId getModelNodeId()
	{
		return modelNodeId;
	}


	
	
	
	public static ModelNodeId createNode(Project target, NodeType nodeType, BaseId id) throws Exception
	{
		CreateModelNodeParameter parameter = new CreateModelNodeParameter(nodeType);
		ModelNodeId nodeId = (ModelNodeId)target.createObject(ObjectType.MODEL_NODE, id, parameter);
		target.addNodeToDiagram(nodeId);
		return nodeId;
	}

	public static NodeType deleteNode(Project target, BaseId idToDelete) throws Exception, IOException, ParseException
	{
		NodeType type = target.removeNodeFromDiagram(idToDelete);
		target.deleteObject(ObjectType.MODEL_NODE, new ModelNodeId(idToDelete.asInt()));
		return type;
	}
	

	public static final String COMMAND_NAME = "DiagramRemoveNode";

	DiagramNodeId diagramNodeId;
	ModelNodeId modelNodeId;
}
