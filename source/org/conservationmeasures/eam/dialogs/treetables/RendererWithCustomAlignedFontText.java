/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.treetables;

import java.awt.Component;

import javax.swing.JTable;

import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewAbstractTableWithColoredColumns;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;

public class RendererWithCustomAlignedFontText extends RendererWithHelperMethods
{
	public RendererWithCustomAlignedFontText(PlanningViewAbstractTableWithColoredColumns tableToUse)
	{
		planningViewTable = tableToUse;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if(!isSelected)
		{
			setForeground(planningViewTable.getForegroundColor(row, column));
			int modelColumn = table.convertColumnIndexToModel(column);
			setBackground(planningViewTable.getColumnBackGroundColor(table.getColumnCount(), modelColumn));
			setFont(planningViewTable.getRowFont(row));
		}
		setHorizontalAlignment(planningViewTable.getColumnAlignment());
		
		if (isSelected)
			setBackground(table.getSelectionBackground());
				
		return component;
	}

	public TreeTableNode getNodeForRow(int row)
	{
		PlanningViewAbstractTreeTableSyncedTableModel model = (PlanningViewAbstractTreeTableSyncedTableModel) planningViewTable.getModel();
		return model.getNodeForRow(row);
	}
	
	protected PlanningViewAbstractTableWithColoredColumns planningViewTable;
}
