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
import javax.swing.table.TableModel;

abstract public class PlanningViewAbstractRightAlignedTable extends	PlanningViewAbstractTableWithSizedColumns
{
	public PlanningViewAbstractRightAlignedTable(TableModel modelToUse)
	{
		super(modelToUse);
		setTableColumnRenderer();
	}
	
	private void setTableColumnRenderer()
	{
		int columnCount = getColumnModel().getColumnCount();
		for (int col = 0; col < columnCount; ++col)
		{	
			TableColumn tableColumn = getColumnModel().getColumn(col);
			tableColumn.setCellRenderer(new CustomRenderer());
		}
	}
	
	public abstract Color getColumnBackGroundColor(int columnCount, int column);
	
	public class CustomRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			int modelColumn = table.convertColumnIndexToModel(column);
			setBackground(getColumnBackGroundColor(table.getColumnCount(), modelColumn));
			setHorizontalAlignment(JLabel.RIGHT);
			
			if (isSelected)
				setBackground(table.getSelectionBackground());
					
			return component;
		}
	}
}
