/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;

import org.miradi.dialogs.tablerenderers.BasicTableCellRenderer;
import org.miradi.dialogs.tablerenderers.BudgetCostTreeTableCellRenderer;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.dialogs.treetables.TreeTableWithIcons;
import org.miradi.main.AppPreferences;
import org.miradi.utils.TableWithTreeTableNodes;


public class PlanningViewBudgetAnnualTotalsTable extends TableWithTreeTableNodes
{
	public PlanningViewBudgetAnnualTotalsTable(PlanningViewBudgetAnnualTotalTableModel model, FontForObjectTypeProvider fontProvider)
	{
		super(model);
		renderer = new BudgetCostTreeTableCellRenderer(model, getSyncedModel().getTreeTableModelAdapter(), fontProvider);
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
	
	public Font getRowFont(int row)
	{
		PlanningViewBudgetAnnualTotalTableModel model = (PlanningViewBudgetAnnualTotalTableModel) getModel();
		TreeTableNode node = model.getNodeForRow(row);
		return TreeTableWithIcons.getSharedTaskFont(node);
	}
	
	public int getColumnAlignment()
	{
		return JLabel.RIGHT;
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	private BasicTableCellRenderer renderer;
	public static final String UNIQUE_IDENTIFIER = "PlanningViewBudgetAnnualTotalsTable";
}
