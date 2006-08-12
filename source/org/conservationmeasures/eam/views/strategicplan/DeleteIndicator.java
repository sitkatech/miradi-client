/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetIndicator;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.views.ViewDoer;

public class DeleteIndicator extends ViewDoer
{
	public IndicatorManagementPanel getIndicatorPanel()
	{
		StrategicPlanView view = (StrategicPlanView)getView();
		return view.getIndicatorManagementPanel();
	}
	
	public boolean isAvailable()
	{
		if(getIndicatorPanel() == null)
			return false;
		
		return getIndicatorPanel().getSelectedIndicator() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Indicator indicator = getIndicatorPanel().getSelectedIndicator();
		
		BaseId idToRemove = indicator.getId();
		ConceptualModelNodeSet nodesThatUseThisIndicator = getProject().findNodesThatUseThisIndicator(idToRemove);
		if(nodesThatUseThisIndicator.size() > 0)
		{
			String[] dialogText = {
					"This Indicator is assigned to one or more Factors.", 
					"Are you sure you want to delete it?", 
					};
			String[] buttons = {"Yes", "No", };
			if(!EAM.confirmDialog("Delete Objective", dialogText, buttons))
				return;
		}
		
		Command[] removeFromNodes = createCommandsToRemoveIndicatorsFromNodes(idToRemove, nodesThatUseThisIndicator);
		
		getProject().executeCommand(new CommandBeginTransaction());
		
		for(int i = 0; i < removeFromNodes.length; ++i)
		{
			getProject().executeCommand(removeFromNodes[i]);
		}
		int type = indicator.getType();
		BaseId id = idToRemove;
		getProject().executeCommand(new CommandSetObjectData(type, id, EAMBaseObject.TAG_LABEL, EAMBaseObject.DEFAULT_LABEL));
		getProject().executeCommand(new CommandSetObjectData(type, id, Indicator.TAG_SHORT_LABEL, ""));
		getProject().executeCommand(new CommandSetObjectData(type, id, Indicator.TAG_METHOD, ""));
		getProject().executeCommand(new CommandSetObjectData(type, id, Indicator.TAG_RESOURCE_IDS, new IdList().toString()));
		getProject().executeCommand(new CommandDeleteObject(type, id));
		
		getProject().executeCommand(new CommandEndTransaction());
	}

	
	private Command[] createCommandsToRemoveIndicatorsFromNodes(BaseId idToRemove, ConceptualModelNodeSet nodesThatUseThisIndicator) throws CommandFailedException
	{
		ConceptualModelNode[] nodes = nodesThatUseThisIndicator.toNodeArray();
		Command[] removeFromNodes = new Command[nodes.length];
		try
		{
			for(int i = 0; i < removeFromNodes.length; ++i)
			{
				ConceptualModelNode node = nodes[i];
				removeFromNodes[i] = new CommandSetIndicator(node.getId(), new BaseId());
			}
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		return removeFromNodes;
	}

}
