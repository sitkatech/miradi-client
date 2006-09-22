package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class GoalManagementPanel extends ObjectManagementPanel
{
	public GoalManagementPanel(UmbrellaView viewToUse)
	{
		super(viewToUse, new GoalTableModel(viewToUse.getProject()), buttonActionClasses);
	}
	
	static final Class[] buttonActionClasses = {
		};


}
