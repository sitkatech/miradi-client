/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;


import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;

import org.miradi.dialogs.tablerenderers.BasicTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.DefaultFontProvider;
import org.miradi.dialogs.tablerenderers.NumericTableCellRendererFactory;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;

public class PlanningViewBudgetTotalsTable extends PlanningViewAbstractTableWithPreferredScrollableViewportSize
{
	public PlanningViewBudgetTotalsTable(MainWindow mainWindowToUse, PlanningViewBudgetTotalsTableModel model)
	{
		super(mainWindowToUse, model, UNIQUE_IDENTIFIER);
		setBackground(getColumnBackGroundColor(0));
		renderer = new NumericTableCellRendererFactory(model, new DefaultFontProvider(getMainWindow()));
	}

	public TableCellRenderer getCellRenderer(int row, int column)
	{
		renderer.setCellBackgroundColor(getColumnBackGroundColor(column));
		return renderer;	
	}
	
	public Color getColumnBackGroundColor(int column)
	{
		return AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND;
	}

	int getPreferredScrollableViewportWidth() throws Exception
	{
		return getSavedColumnWidth(0);
	}
	
	protected int getColumnWidth(int column)
	{
		return 125;
	}	
	
	public int getColumnAlignment()
	{
		return JLabel.RIGHT;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewBudgetTotalsTable";

	private BasicTableCellRendererFactory renderer;
}