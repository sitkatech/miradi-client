/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ProjectDoer;

public class InsertFactorLinkDoer extends ProjectDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		return (getProject().getDiagramModel().getFactorCount() >= 2);
	}

	public void doIt() throws CommandFailedException
	{
		ConnectionPropertiesDialog dialog = new ConnectionPropertiesDialog(EAM.mainWindow);
		dialog.setVisible(true);
		if(!dialog.getResult())
			return;
		
		DiagramModel model = getProject().getDiagramModel();
		FactorId fromId = dialog.getFrom().getWrappedId();
		FactorId toId = dialog.getTo().getWrappedId();
		
		if(fromId.equals(toId))
		{
			String[] body = {EAM.text("Can't link an item to itself"), };
			EAM.okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
					
		try
		{
			if(model.areLinked(dialog.getFrom(), dialog.getTo()))
			{
				String[] body = {EAM.text("Those items are already linked"), };
				EAM.okDialog(EAM.text("Can't Create Link"), body);
				return;
			}
			if (wouldCreateLinkageLoop(model, fromId, toId))
			{
				String[] body = {EAM.text("Cannot create that link because it would cause a loop."), };
				EAM.okDialog(EAM.text("Error"), body);
				return;
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		
		getProject().executeCommand(new CommandBeginTransaction());
		createModelLinkageAndAddToDiagramUsingCommands(getProject(), fromId, toId);
		getProject().executeCommand(new CommandEndTransaction());
	}
	
	boolean wouldCreateLinkageLoop(DiagramModel dModel, FactorId fromId, FactorId toId)
    {
		Factor fromFactor = dModel.getFactorPool().find(fromId);
		Factor[] upstreamFactors = dModel.getAllUpstreamNodes(fromFactor).toNodeArray();
		
		for (int i  = 0; i < upstreamFactors.length; i++)
			if (upstreamFactors[i].getId().equals(toId))
				return true;
		
		return false;
    }
	
	public static CommandDiagramAddFactorLink createModelLinkageAndAddToDiagramUsingCommands(Project projectToUse, FactorId fromId, FactorId toId) throws CommandFailedException
	{
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(fromId, toId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		projectToUse.executeCommand(createModelLinkage);
		
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = createDiagramFactorLinkParameter(projectToUse, fromId, toId, modelLinkageId);
		
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
    	projectToUse.executeCommand(createDiagramLinkCommand);
    	
    	BaseId rawId = createDiagramLinkCommand.getCreatedId();
		DiagramFactorLinkId createdDiagramLinkId = new DiagramFactorLinkId(rawId.asInt());
    	CommandDiagramAddFactorLink command = new CommandDiagramAddFactorLink(createdDiagramLinkId);
		projectToUse.executeCommand(command);
		
		return command;
	}

	private static CreateDiagramFactorLinkParameter createDiagramFactorLinkParameter(Project projectToUse, FactorId fromId, FactorId toId, FactorLinkId modelLinkageId)
	{
		DiagramFactorId fromDiagramFactorId = projectToUse.getDiagramModel().getDiagramFactorByWrappedId(fromId).getDiagramFactorId();
		DiagramFactorId toDiagramFactorId = projectToUse.getDiagramModel().getDiagramFactorByWrappedId(toId).getDiagramFactorId();
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(modelLinkageId, fromDiagramFactorId, toDiagramFactorId);
		return diagramLinkExtraInfo;
	}
}
