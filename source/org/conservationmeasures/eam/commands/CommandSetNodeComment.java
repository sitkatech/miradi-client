/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandSetNodeComment extends Command
{
	public CommandSetNodeComment(int idToUpdate, String newCommentToUse)
	{
		id = idToUpdate;
		newComment = newCommentToUse;
		previousComment = null;
	}
	
	public CommandSetNodeComment(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		newComment = dataIn.readUTF();
		previousComment = dataIn.readUTF();
	}

	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousComment = doSetComment(target, getNewComment(), getPreviousComment()); 
	}

	public void undo(Project target) throws CommandFailedException
	{
		doSetComment(target, getPreviousComment(), getNewComment());
	}

	private String doSetComment(Project target, String desiredComment, String expectedComment) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(id);
			String currentComment = node.getComment();
			if(expectedComment != null && !currentComment.equals(expectedComment))
				throw new Exception("CommandSetNodeComment expected " + expectedComment + " but was " + currentComment);
			target.setNodeComment(getId(), desiredComment, expectedComment);
			return currentComment;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		dataOut.writeUTF(getNewComment());
		dataOut.writeUTF(getPreviousComment());
	}

	int getId()
	{
		return id;
	}
	
	String getNewComment()
	{
		return newComment;
	}

	public String getPreviousComment()
	{
		return previousComment;
	}
	
	public static final String COMMAND_NAME = "SetNodeComment";
	
	int id;
	String newComment;
	String previousComment;
}
