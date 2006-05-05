/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.ProjectDoer;

public class InsertActivity extends ProjectDoer
{
	public InsertActivity(StrategicPlanView viewToUse)
	{
		view = viewToUse;
	}
	
	public boolean isAvailable()
	{
		StratPlanObject selected = view.getSelectedObject();
		if(selected == null)
			return false;
		return selected.canInsertActivityHere();
	}

	public void doIt() throws CommandFailedException
	{
		StratPlanObject selected = view.getSelectedObject();
//		ActivityInsertionPoint insertAt = selected.getActivityInsertionPoint();
//		int interventionId = insertAt.getInterventionId();
//		int childIndex = insertAt.getIndex();
//		ConceptualModelNode intervention = getProject().getNodePool().find(interventionId);
//		CommandCreateObject create = new CommandCreateObject(ObjectType.TASK);
//		getProject().executeCommand(create);
//		CommandSetObjectData addChild = CommandSetObjectData.createInsertIdCommand(intervention, 
//				ConceptualModelIntervention.TAG_ACTIVITY_IDS, create.getCreatedId(), childIndex);
//		getProject().executeCommand(addChild);
		
		EAM.logWarning(selected.toString());
	}

	StrategicPlanView view;
}
