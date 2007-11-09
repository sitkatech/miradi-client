/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.conservationmeasures.eam.dialogs.treetables.RendererWithCustomAlignedFontText;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithIcons;
import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;

abstract public class PlanningViewAbstractTableWithColoredColumns extends TableWithColumnWidthSaver
{
	public PlanningViewAbstractTableWithColoredColumns(TableModel modelToUse)
	{
		super(modelToUse);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setTableColumnRenderer();
	}
		
	private void setTableColumnRenderer()
	{
		int columnCount = getColumnModel().getColumnCount();
		for (int col = 0; col < columnCount; ++col)
		{	
			TableColumn tableColumn = getColumnModel().getColumn(col);
			tableColumn.setCellRenderer(new RendererWithCustomAlignedFontText(this));
		}
	}
	
	protected int getColumnWidth(int column)
	{
		return getColumnHeaderWidth(column);
	}
	
	public Color getColumnBackGroundColor(int columnCount, int modelColumn)
	{
		return getBackground();
	}
	
	public Font getRowFont(int row)
	{
		return TreeTableWithIcons.Renderer.getPlainFont();
	}
	
	public Color getForegroundColor(int row, int column)
	{
		if(isCellEditable(row, column))
			return Color.BLUE.darker();
		return Color.BLACK;
	}
	
	public int getColumnAlignment()
	{
		return JLabel.LEFT;
	}
}
