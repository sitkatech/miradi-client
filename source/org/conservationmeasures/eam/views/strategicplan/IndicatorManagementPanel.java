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
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Indicator;

public class IndicatorManagementPanel extends ObjectManagementPanel
{
	public IndicatorManagementPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, columnTags, mainWindowToUse.getProject().getIndicatorPool(), buttonActionClasses);
	}
	
	public Indicator getSelectedIndicator()
	{
		int row = table.getSelectedRow();
		if(row < 0)
			return null;
		
		IndicatorPool pool = getProject().getIndicatorPool();
		int indicatorId = pool.getIds()[row];
		Indicator indicator = pool.find(indicatorId);
		return indicator;
	}
	
	static final String[] columnTags = {"Identifier", "Label", };
	static final Class[] buttonActionClasses = {
		ActionCreateIndicator.class, 
		ActionModifyIndicator.class, 
		ActionDeleteIndicator.class, 
		};

}
