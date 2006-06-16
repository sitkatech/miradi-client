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
		super(mainWindowToUse, new IndicatorTableModel(mainWindowToUse.getProject().getIndicatorPool()), buttonActionClasses);
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
	
	static class IndicatorTableModel extends ObjectManagerTableModel
	{
		IndicatorTableModel(IndicatorPool pool)
		{
			super(pool, indicatorColumnTags);
		}

		public Object getValueAt(int rowIndex, int columnIndex)
		{
			if(indicatorColumnTags[columnIndex].equals(COLUMN_FACTORS))
				return "Hello";
			
			return super.getValueAt(rowIndex, columnIndex);
		}

		public static final String COLUMN_FACTORS = "Factors";
		
		static final String[] indicatorColumnTags = {
			Indicator.TAG_SHORT_LABEL, 
			Indicator.TAG_LABEL,
			Indicator.TAG_METHOD,
			COLUMN_FACTORS,
			};

	}
	
	static final Class[] buttonActionClasses = {
		ActionCreateIndicator.class, 
		ActionModifyIndicator.class, 
		ActionDeleteIndicator.class, 
		};

}
