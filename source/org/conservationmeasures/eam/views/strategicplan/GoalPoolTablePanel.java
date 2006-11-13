package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.views.umbrella.LegacyObjectPoolTablePanel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class GoalPoolTablePanel extends LegacyObjectPoolTablePanel
{
	public GoalPoolTablePanel(UmbrellaView viewToUse)
	{
		super(viewToUse, new GoalPoolTableModel(viewToUse.getProject()), buttonActionClasses);
		
		setMaxColumnWidthToHeaderWidth(0);
		setColumnVeryWide(2);

	}
	
	public Goal getSelectedGoal()
	{
		int row = getSelectedRow();
		if(row < 0)
			return null;
		
		BaseId id = getObjectFromRow(row).getId();
		GoalPool pool = getProject().getGoalPool();
		Goal goal = pool.find(id);
		return goal;
	}

	static final Class[] buttonActionClasses = {
		};


}
