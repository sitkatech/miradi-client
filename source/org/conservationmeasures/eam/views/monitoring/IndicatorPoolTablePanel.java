/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectpools.IndicatorPool;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.views.umbrella.ObjectPoolTablePanel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class IndicatorPoolTablePanel extends ObjectPoolTablePanel
{
	public IndicatorPoolTablePanel(UmbrellaView viewToUse)
	{
		super(viewToUse, new IndicatorPoolTableModel(viewToUse.getProject()), buttonActionClasses);
		
		setMaxColumnWidthToHeaderWidth(0);
		
	}
	
	public Indicator getSelectedIndicator()
	{
		int row = getTable().getSelectedRow();
		if(row < 0)
			return null;
		
		BaseId indicatorId = getObjectFromRow(row).getId();
		IndicatorPool pool = getProject().getIndicatorPool();
		Indicator indicator = pool.find(indicatorId);
		return indicator;
	}
	

	
	static final Class[] buttonActionClasses = {
		};

}
