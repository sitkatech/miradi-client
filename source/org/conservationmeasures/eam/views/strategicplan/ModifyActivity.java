/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.ProjectDoer;

public class ModifyActivity extends ProjectDoer
{
	public ModifyActivity(StrategicPlanView viewToUse)
	{
		view = viewToUse;
	}
	
	public boolean isAvailable()
	{
		StratPlanObject selected = view.getSelectedObject();
		if(selected == null)
			return false;
		return (selected.getType() == ObjectType.TASK);
	}

	public void doIt() throws CommandFailedException
	{
		StratPlanActivity selected = (StratPlanActivity)view.getSelectedObject();
		modify(selected.getActivity());
	}

	public void modify(Task activity)
	{
		ObjectPropertiesDialog dlg = new TaskPropertiesDialog(getMainWindow(), activity, new String[] {"Label"});
		dlg.setVisible(true);
	}
	
	MainWindow getMainWindow()
	{
		return view.getMainWindow();
	}

	
	StrategicPlanView view;
}
