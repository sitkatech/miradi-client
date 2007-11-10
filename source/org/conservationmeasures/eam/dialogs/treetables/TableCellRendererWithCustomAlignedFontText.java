/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.treetables;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewAbstractTableWithColoredColumns;

public class TableCellRendererWithCustomAlignedFontText extends RendererWithHelperMethods
{
	public TableCellRendererWithCustomAlignedFontText(PlanningViewAbstractTableWithColoredColumns tableToUse, int alignmentToUse, Color backgroundColorToUse)
	{
		planningViewTable = tableToUse;
		alignment = alignmentToUse;
		backgroundColor = backgroundColorToUse;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if(!isSelected)
		{
			setForeground(getForegroundColor(table, row, column));
			setBackground(backgroundColor);
			setFont(planningViewTable.getRowFont(row));
		}
		setHorizontalAlignment(alignment);
		
		if (isSelected)
			setBackground(table.getSelectionBackground());
				
		return component;
	}

	public Color getForegroundColor(JTable table, int row, int column)
	{
		if(table.isCellEditable(row, column))
			return Color.BLUE.darker();
		return Color.BLACK;
	}
	
	protected PlanningViewAbstractTableWithColoredColumns planningViewTable;
	private int alignment;
	private Color backgroundColor;
}
