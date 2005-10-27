package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.diagram.ProjectScopeBox;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;

public class CommandSetProjectVision extends Command
{
	public CommandSetProjectVision(String visionToUse)
	{
		vision = visionToUse;
		previousVision = "";
	}
	
	public CommandSetProjectVision(DataInputStream dataIn) throws IOException
	{
		vision = dataIn.readUTF();
		previousVision = dataIn.readUTF();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		ProjectScopeBox scope = target.getDiagramModel().getProjectScopeBox();
		previousVision = scope.getVision();
		scope.setVision(getVisionText());
	}

	public void undo(Project target) throws CommandFailedException
	{
		ProjectScopeBox scope = target.getDiagramModel().getProjectScopeBox();
		scope.setVision(getPreviousVisionText());
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getVisionText());
		dataOut.writeUTF(getPreviousVisionText());
	}
	
	public String getVisionText()
	{
		return vision;
	}
	
	public String getPreviousVisionText()
	{
		return previousVision;
	}
	
	public static final String COMMAND_NAME = "SetProjectVision";
	
	String vision;
	String previousVision;
}
