/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddLinkage;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.ModelLinkageId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ProjectDoer;

public class InsertConnection extends ProjectDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		return (getProject().getDiagramModel().getNodeCount() >= 2);
	}

	public void doIt() throws CommandFailedException
	{
		ConnectionPropertiesDialog dialog = new ConnectionPropertiesDialog(EAM.mainWindow);
		dialog.setVisible(true);
		if(!dialog.getResult())
			return;
		
		DiagramModel model = getProject().getDiagramModel();
		ModelNodeId fromId = dialog.getFrom().getWrappedId();
		ModelNodeId toId = dialog.getTo().getWrappedId();
		
		if(fromId.equals(toId))
		{
			String[] body = {EAM.text("Can't link an item to itself"), };
			EAM.okDialog(EAM.text("Can't Create Link"), body);
			return;
		}
					
		try
		{
			if(model.hasLinkage(dialog.getFrom(), dialog.getTo()))
			{
				String[] body = {EAM.text("Those items are already linked"), };
				EAM.okDialog(EAM.text("Can't Create Link"), body);
				return;
			}
			if (wouldCreateLinkageLoop(model, fromId, toId))
			{
				String[] body = {EAM.text("Cannot create that connection because it would cause a loop."), };
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
	
	boolean wouldCreateLinkageLoop(DiagramModel dModel, ModelNodeId fromNodeId, ModelNodeId toNodeId)
    {
		ConceptualModelNode cmFromNode = dModel.getNodePool().find(fromNodeId);
		ConceptualModelNode[] cmUpstreamNodes = dModel.getAllUpstreamNodes(cmFromNode).toNodeArray();
		
		for (int i  = 0; i < cmUpstreamNodes.length; i++)
			if (cmUpstreamNodes[i].getId().equals(toNodeId))
				return true;
		
		return false;
    }
	
	public static ModelLinkageId createModelLinkageAndAddToDiagramUsingCommands(Project projectToUse, ModelNodeId fromId, ModelNodeId toId) throws CommandFailedException
	{
		CreateModelLinkageParameter extraInfo = new CreateModelLinkageParameter(fromId, toId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.MODEL_LINKAGE, extraInfo);
		projectToUse.executeCommand(createModelLinkage);
		ModelLinkageId modelLinkageId = (ModelLinkageId)createModelLinkage.getCreatedId();
		CommandDiagramAddLinkage command = new CommandDiagramAddLinkage(modelLinkageId);
		projectToUse.executeCommand(command);
		return modelLinkageId;
	}

}
