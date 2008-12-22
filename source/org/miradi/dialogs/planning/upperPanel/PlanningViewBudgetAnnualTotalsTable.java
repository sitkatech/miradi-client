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
package org.miradi.dialogs.planning.upperPanel;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;

import org.miradi.dialogs.tablerenderers.BasicTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.BudgetCostTreeTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;
import org.miradi.project.CurrencyFormat;
import org.miradi.utils.TableWithTreeTableNodes;


public class PlanningViewBudgetAnnualTotalsTable extends TableWithTreeTableNodes
{
	public PlanningViewBudgetAnnualTotalsTable(MainWindow mainWindowToUse, PlanningViewBudgetAnnualTotalTableModel model, FontForObjectTypeProvider fontProvider)
	{
		super(mainWindowToUse, model, UNIQUE_IDENTIFIER);
		CurrencyFormat currencyFormatter = getProject().getCurrencyFormatterWithCommas();
		renderer = new BudgetCostTreeTableCellRendererFactory(model, fontProvider, currencyFormatter);
	}
	
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		renderer.setCellBackgroundColor(getColumnBackGroundColor(column));
		return renderer;	
	}
	
	protected int getColumnWidth(int column)
	{
		return 125;
	}	
	
	public Color getColumnBackGroundColor(int tableColumn)
	{
		int columnCount = getColumnCount();
		final int TOTALS_COLUMN = columnCount - 1;
		if (convertColumnIndexToModel(tableColumn) == TOTALS_COLUMN)
			return AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND;
		
		return AppPreferences.BUDGET_TABLE_BACKGROUND;
	}
	
	public int getColumnAlignment()
	{
		return JLabel.RIGHT;
	}
	
	private BasicTableCellRendererFactory renderer;
	public static final String UNIQUE_IDENTIFIER = "PlanningViewBudgetAnnualTotalsTable";
}
