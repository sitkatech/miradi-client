/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.ActionModifyObjective;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.umbrella.ObjectManagementPanel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ObjectivePoolTablePanel extends ObjectManagementPanel
{
	public ObjectivePoolTablePanel(UmbrellaView viewToUse)
	{
		super(viewToUse, new ObjectivePoolTableModel(viewToUse.getProject()), buttonActionClasses);
		
		addDoubleClickAction(ActionModifyObjective.class);
		setMaxColumnWidthToHeaderWidth(0);
		setColumnVeryWide(2);
	}

	public Objective getSelectedObjective()
	{
		int row = getSelectedRow();
		if(row < 0)
			return null;
		
		ObjectivePool pool = getProject().getObjectivePool();
		BaseId objectiveId = pool.getIds()[row];
		Objective objective = pool.find(objectiveId);
		return objective;
	}

	static final Class[] buttonActionClasses = {
		ActionCreateObjective.class, 
		ActionModifyObjective.class, 
		ActionDeleteObjective.class, 
		};

}
