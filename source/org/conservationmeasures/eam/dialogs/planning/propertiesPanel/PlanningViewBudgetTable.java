/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;

import org.conservationmeasures.eam.dialogs.tablerenderers.BasicTableCellRenderer;
import org.conservationmeasures.eam.dialogs.tablerenderers.DefaultFontProvider;
import org.conservationmeasures.eam.dialogs.tablerenderers.NumericTableCellRenderer;
import org.conservationmeasures.eam.main.AppPreferences;


public class PlanningViewBudgetTable extends PlanningViewAbstractTableWithPreferredScrollableViewportSize
{
	public PlanningViewBudgetTable(PlanningViewBudgetTableModel modelToUse)
	{
		super(modelToUse);
		setBackground(getColumnBackGroundColor(0));
		renderer = new NumericTableCellRenderer(modelToUse, new DefaultFontProvider());
	}

	public TableCellRenderer getCellRenderer(int row, int column)
	{
		renderer.setCellBackgroundColor(getColumnBackGroundColor(column));
		return renderer;	
	}
	
	public Color getColumnBackGroundColor(int column)
	{
		return AppPreferences.BUDGET_TABLE_BACKGROUND;
	}

	protected int getColumnWidth(int column)
	{
		return 125;
	}
	
	public int getColumnAlignment()
	{
		return JLabel.RIGHT;
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewBudgetTable";

	private BasicTableCellRenderer renderer;
}
