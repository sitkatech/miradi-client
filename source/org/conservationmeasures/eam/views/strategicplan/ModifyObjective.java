/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.dialogs.ObjectPropertiesDialog;
import org.conservationmeasures.eam.dialogs.ObjectivePropertiesDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.ViewDoer;

public class ModifyObjective extends ViewDoer
{
	public ModifyObjective(StrategicPlanView viewToUse)
	{
		view = viewToUse;
	}
	
	public ObjectiveManagementPanel getObjectivePanel()
	{
		return view.getObjectivePanel();
	}
	
	public boolean isAvailable()
	{
		if(getObjectivePanel() == null)
			return false;
		
		return getObjectivePanel().getSelectedObjective() != null;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Objective objective = getObjectivePanel().getSelectedObjective();
		modify(objective);
	}

	public void modify(Objective objective)
	{
		ObjectPropertiesDialog dlg = new ObjectivePropertiesDialog(getMainWindow(), objective);
		dlg.setVisible(true);
	}

	StrategicPlanView view;

}
