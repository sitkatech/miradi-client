/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.ActionModifyIndicator;
import org.conservationmeasures.eam.annotations.IndicatorPool;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class IndicatorManagementPanel extends ObjectManagementPanel
{
	public IndicatorManagementPanel(UmbrellaView viewToUse)
	{
		super(viewToUse, new IndicatorTableModel(viewToUse.getProject()), buttonActionClasses);
	}
	
	public Indicator getSelectedIndicator()
	{
		int row = table.getSelectedRow();
		if(row < 0)
			return null;
		
		IndicatorPool pool = getProject().getIndicatorPool();
		BaseId indicatorId = pool.getIds()[row];
		Indicator indicator = pool.find(indicatorId);
		return indicator;
	}
	

	
	static final Class[] buttonActionClasses = {
		ActionCreateIndicator.class, 
		ActionModifyIndicator.class, 
		ActionDeleteIndicator.class, 
		};

}
