/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
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
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.MODEL_LINKAGE, extraInfo);
		projectToUse.executeCommand(createModelLinkage);
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		CommandDiagramAddFactorLink command = new CommandDiagramAddFactorLink(modelLinkageId);
		projectToUse.executeCommand(command);
		return command;
	}

}
