/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
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
		ModelNodeId fromIndex = dialog.getFrom().getWrappedId();
		ModelNodeId toIndex = dialog.getTo().getWrappedId();
		
		if(fromIndex.equals(toIndex))
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
			if (wouldCreateLinkageLoop(model, fromIndex, toIndex)){
				String[] body = {EAM.text("Can't Create Link. New Link Causes Loop"), };
				EAM.okDialog(EAM.text("Can't Create Link"), body);
				return;
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		
		CommandLinkNodes command = new CommandLinkNodes(fromIndex, toIndex);
		getProject().executeCommand(command);
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

}
