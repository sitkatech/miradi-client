/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class PlanningViewTableWithSizedColumns extends PlanningViewAbstractTable
{
	public PlanningViewTableWithSizedColumns(TableModel modelToUse)
	{
		super(modelToUse);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		addSizedColumnEditorsAndRenderers(modelToUse);
	}
		
	private void addSizedColumnEditorsAndRenderers(TableModel modelToUse)
	{
		for (int i = 0; i < modelToUse.getColumnCount(); ++i)
		{
			setColumnWidthToHeaderWidth(i);
		}
	}
}
