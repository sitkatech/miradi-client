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

abstract public class PlanningViewAbstractTableWithSizedColumns extends PlanningViewAbstractTable
{
	public PlanningViewAbstractTableWithSizedColumns(TableModel modelToUse)
	{
		super(modelToUse);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setAppropriateColumnWidths(modelToUse);
		setTableColumnRenderer();

	}
		
	private void setAppropriateColumnWidths(TableModel modelToUse)
	{
		for (int column = 0; column < modelToUse.getColumnCount(); ++column)
		{
			setColumnWidth(column, getColumnWidth(column));
		}
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
	
	protected int getColumnWidth(int column)
	{
		return getColumnHeaderWidth(column);
	}
	
	protected Color getColumnBackGroundColor(int columnCount, int modelColumn)
	{
		return getBackground();
	}
	
	protected Color getForegroundColor(int row, int column)
	{
		if(isCellEditable(row, column))
			return Color.BLUE.darker();
		return Color.BLACK;
	}
	
	protected int getColumnAlignment()
	{
		return JLabel.LEFT;
	}

	public class CustomRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setForeground(getForegroundColor(row, column));
			int modelColumn = table.convertColumnIndexToModel(column);
			setBackground(getColumnBackGroundColor(table.getColumnCount(), modelColumn));
			setHorizontalAlignment(getColumnAlignment());
			
			if (isSelected)
				setBackground(table.getSelectionBackground());
					
			return component;
		}

	}
}
