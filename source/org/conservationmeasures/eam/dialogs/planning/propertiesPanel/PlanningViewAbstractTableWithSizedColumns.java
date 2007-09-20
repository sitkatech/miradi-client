/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import javax.swing.JTable;
import javax.swing.table.TableModel;

abstract public class PlanningViewAbstractTableWithSizedColumns extends PlanningViewAbstractTable
{
	public PlanningViewAbstractTableWithSizedColumns(TableModel modelToUse)
	{
		super(modelToUse);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setAppropriateColumnWidths(modelToUse);
	}
		
	private void setAppropriateColumnWidths(TableModel modelToUse)
	{
		for (int column = 0; column < modelToUse.getColumnCount(); ++column)
		{
			setColumnWidth(column, getColumnWidth(column));
		}
	}
	
	abstract protected int getColumnWidth(int column);
}
