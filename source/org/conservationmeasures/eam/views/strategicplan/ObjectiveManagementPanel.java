/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.ActionModifyObjective;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ObjectivePool;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ObjectiveManagementPanel extends ObjectManagementPanel
{
	public ObjectiveManagementPanel(UmbrellaView viewToUse)
	{
		super(viewToUse, new ObjectiveTableModel(viewToUse.getProject()), buttonActionClasses);
	}
	
	public Objective getSelectedObjective()
	{
		int row = table.getSelectedRow();
		if(row < 0)
			return null;
		
		ObjectivePool pool = getProject().getObjectivePool();
		int objectiveId = pool.getIds()[row];
		Objective objective = pool.find(objectiveId);
		return objective;
	}

	static final Class[] buttonActionClasses = {
		ActionCreateObjective.class, 
		ActionModifyObjective.class, 
		ActionDeleteObjective.class, 
		};

}
