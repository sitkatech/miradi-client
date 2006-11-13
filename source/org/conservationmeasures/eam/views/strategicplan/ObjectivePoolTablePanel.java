/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.views.umbrella.LegacyObjectPoolTablePanel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ObjectivePoolTablePanel extends LegacyObjectPoolTablePanel
{
	public ObjectivePoolTablePanel(UmbrellaView viewToUse)
	{
		super(viewToUse, new ObjectivePoolTableModel(viewToUse.getProject()), buttonActionClasses);
		
		setMaxColumnWidthToHeaderWidth(0);
		setColumnVeryWide(2);
	}

	public Objective getSelectedObjective()
	{
		int row = getSelectedRow();
		if(row < 0)
			return null;
		
		BaseId objectiveId = getObjectFromRow(row).getId();
		ObjectivePool pool = getProject().getObjectivePool();
		Objective objective = pool.find(objectiveId);
		return objective;
	}

	static final Class[] buttonActionClasses = {
		};

}
