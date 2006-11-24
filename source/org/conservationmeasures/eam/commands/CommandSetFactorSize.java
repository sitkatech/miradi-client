/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.awt.Dimension;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetFactorSize extends Command
{
	public CommandSetFactorSize(DiagramFactorId idToUpdate, Dimension updatedSize, Dimension previousSizeToUse)
	{
		id = idToUpdate;
		currentSize = updatedSize;
		previousSize = previousSizeToUse;
	}

	public void execute(Project target) throws CommandFailedException
	{
		 previousSize = doSetSize(target, getCurrentSize(), getPreviousSize()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetSize(target, getPreviousSize(), getCurrentSize());
	}
	
	private Dimension doSetSize(Project target, Dimension desiredSize, Dimension expectedSize) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramFactor node = model.getNodeById(getId());
			Dimension currentNodeSize = node.getSize();
			if(expectedSize != null && !currentNodeSize.equals(expectedSize))
				throw new Exception("CommandSetNodeSize expected " + expectedSize + " but was " + currentNodeSize);
			node.setSize(desiredSize);
			Logging.logVerbose("Updating Cell Size from:"+ expectedSize +" to:"+ desiredSize);
			model.updateCell(node);
			return currentNodeSize;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + currentSize + ", " + previousSize;
	}

	public Dimension getCurrentSize()
	{
		return currentSize;
	}
	
	public Dimension getPreviousSize()
	{
		return previousSize;
	}

	DiagramFactorId getId()
	{
		return id;
	}
	
	public String getCommandName() 
	{
		return COMMAND_NAME;
	}
	
	public static final String COMMAND_NAME = "ReSize";

	DiagramFactorId id;
	Dimension currentSize;
	Dimension previousSize;
}