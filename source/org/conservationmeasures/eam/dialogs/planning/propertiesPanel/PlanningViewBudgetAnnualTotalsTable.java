/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.main.AppPreferences;

public class PlanningViewBudgetAnnualTotalsTable extends PlanningViewTableWithSizedColumns
{
	public PlanningViewBudgetAnnualTotalsTable(PlanningViewBudgetAnnualTotalTableModel model)
	{
		super(model);
		setTableColumnRenderer();
	}
	
	private void setTableColumnRenderer()
	{
		int columnCount = getColumnModel().getColumnCount();
		for (int col = 0; col < columnCount; ++col)
		{	
			TableColumn tableColumn = getColumnModel().getColumn(col);
			tableColumn.setCellRenderer(new CustomRenderer());
			setColumnWidth(col, 125);
		}
	}
	
	public static class CustomRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			int modelColumn = table.convertColumnIndexToModel(column);
			setBackground(getBackgroundColor(table.getColumnCount(), modelColumn));
			setHorizontalAlignment(JLabel.RIGHT);
			
			if (isSelected)
				setBackground(table.getSelectionBackground());
					
			return component;
		}

		private Color getBackgroundColor(int columnCount, int column)
		{
			final int TOTALS_COLUMN = columnCount - 1;
			if (column == TOTALS_COLUMN)
				return AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND;
			
			return AppPreferences.BUDGET_TABLE_BACKGROUND;
		}
	}	
}
