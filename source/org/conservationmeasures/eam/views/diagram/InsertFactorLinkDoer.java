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
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectChainObject;
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
		DiagramFactor fromDiagramFactor = dialog.getFrom();
		DiagramFactor toDiagramFactor = dialog.getTo();
		
		if(fromDiagramFactor.getDiagramFactorId().equals(toDiagramFactor.getDiagramFactorId()))
		{
			String[] body = {EAM.text("Can't link an item to itself"), };
			EAM.okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
					
		try
		{
			if(model.areLinked(dialog.getFrom().getDiagramFactorId(), dialog.getTo().getDiagramFactorId()))
			{
				String[] body = {EAM.text("Those items are already linked"), };
				EAM.okDialog(EAM.text("Can't Create Link"), body);
				return;
			}
			if (wouldCreateLinkageLoop(model, fromDiagramFactor.getWrappedId(), toDiagramFactor.getWrappedId()))
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
		createModelLinkageAndAddToDiagramUsingCommands(getProject(), fromDiagramFactor.getWrappedId(), toDiagramFactor.getWrappedId());
		getProject().executeCommand(new CommandEndTransaction());
	}
	
	boolean wouldCreateLinkageLoop(DiagramModel dModel, FactorId fromId, FactorId toId)
    {
		Factor fromFactor = dModel.getProject().findNode(fromId);
		ProjectChainObject chainObject = new ProjectChainObject();
		chainObject.buildUpstreamChain(fromFactor);
		Factor[] upstreamFactors = chainObject.getFactorsArray();
		
		for (int i  = 0; i < upstreamFactors.length; i++)
			if (upstreamFactors[i].getId().equals(toId))
				return true;
		
		return false;
    }
	
	public static CommandDiagramAddFactorLink createModelLinkageAndAddToDiagramUsingCommands(Project projectToUse, FactorId from, FactorId to) throws CommandFailedException
	{
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(from, to);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		projectToUse.executeCommand(createModelLinkage);
		
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		DiagramFactorId fromDiagramFactorId = convertWrappedId(projectToUse, from);
		DiagramFactorId toDiagramFactorId = convertWrappedId(projectToUse, to);
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = createDiagramFactorLinkParameter(projectToUse, fromDiagramFactorId, toDiagramFactorId, modelLinkageId);
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
    	projectToUse.executeCommand(createDiagramLinkCommand);
    	
    	BaseId rawId = createDiagramLinkCommand.getCreatedId();
		DiagramFactorLinkId createdDiagramLinkId = new DiagramFactorLinkId(rawId.asInt());
    	CommandDiagramAddFactorLink command = new CommandDiagramAddFactorLink(createdDiagramLinkId);
		projectToUse.executeCommand(command);
		
		return command;
	}

	private static DiagramFactorId convertWrappedId(Project project, FactorId factorId)
	{
		return project.getDiagramModel().getDiagramFactorIdFromWrappedId(factorId);
	}

	private static CreateDiagramFactorLinkParameter createDiagramFactorLinkParameter(Project projectToUse, DiagramFactorId fromId, DiagramFactorId toId, FactorLinkId modelLinkageId)
	{
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(modelLinkageId, fromId, toId);
		
		return diagramLinkExtraInfo;
	}
}
