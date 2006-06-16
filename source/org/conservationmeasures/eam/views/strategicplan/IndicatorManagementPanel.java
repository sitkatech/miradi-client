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
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class IndicatorManagementPanel extends ObjectManagementPanel
{
	public IndicatorManagementPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, new IndicatorTableModel(mainWindowToUse.getProject()), buttonActionClasses);
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
		IndicatorTableModel(Project projectToUse)
		{
			super(projectToUse.getIndicatorPool(), indicatorColumnTags);
			project = projectToUse;
		}

		public Object getValueAt(int rowIndex, int columnIndex)
		{
			if(indicatorColumnTags[columnIndex].equals(COLUMN_FACTORS))
			{
				int indicatorId = pool.getIds()[rowIndex];
				ConceptualModelNode[] modelNodes =  project.findNodesThatUseThisIndicator(indicatorId);
				
				return getNodeLabelsAsString(modelNodes);
			}
			
			return super.getValueAt(rowIndex, columnIndex);
		}
		
		String getNodeLabelsAsString(ConceptualModelNode[] modelNodes)
		{
			StringBuffer result = new StringBuffer();
			for(int i = 0; i < modelNodes.length; ++i)
			{
				if(i > 0)
					result.append(", ");
				result.append(modelNodes[i].getLabel());
			}
			
			return result.toString();
		}

		public static final String COLUMN_FACTORS = "Factor(s)";
		
		static final String[] indicatorColumnTags = {
			Indicator.TAG_SHORT_LABEL, 
			Indicator.TAG_LABEL,
			Indicator.TAG_METHOD,
			COLUMN_FACTORS,
			};

		Project project;
	}
	
	static final Class[] buttonActionClasses = {
		ActionCreateIndicator.class, 
		ActionModifyIndicator.class, 
		ActionDeleteIndicator.class, 
		};

}
