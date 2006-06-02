/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.ActionModifyObjective;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ObjectivePool;

public class ObjectiveManagementPanel extends ObjectManagementPanel
{
	public ObjectiveManagementPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, columnTags, mainWindowToUse.getProject().getObjectivePool(), buttonActionClasses);
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
	
	static final String[] columnTags = {
		Objective.TAG_SHORT_LABEL, 
		Objective.TAG_LABEL,
		Objective.TAG_FULL_TEXT,
		};
	static final Class[] buttonActionClasses = {
		ActionCreateObjective.class, 
		ActionModifyObjective.class, 
		ActionDeleteObjective.class, 
		};

}
