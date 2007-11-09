/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeTaskNode;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithIcons;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.objects.Task;


public class PlanningViewBudgetAnnualTotalsTable extends PlanningViewFullSizeTable
{
	public PlanningViewBudgetAnnualTotalsTable(PlanningViewBudgetAnnualTotalTableModel model)
	{
		super(model);	
	}
	
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		return new AstrickRenderer();	
	}
	
	protected int getColumnWidth(int column)
	{
		return 125;
	}	
	
	public Color getColumnBackGroundColor(int columnCount, int column)
	{
		final int TOTALS_COLUMN = columnCount - 1;
		if (column == TOTALS_COLUMN)
			return AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND;
		
		return AppPreferences.BUDGET_TABLE_BACKGROUND;
	}
	
	protected Font getRowFont(int row)
	{
		PlanningViewBudgetAnnualTotalTableModel model = (PlanningViewBudgetAnnualTotalTableModel) getModel();
		TreeTableNode node = (TreeTableNode) model.getNodeForRow(row);
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
	
	public class AstrickRenderer extends CustomRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (label.getText().length() == 0)
				return label;

			PlanningViewAbstractTreeTableSyncedTableModel model = (PlanningViewAbstractTreeTableSyncedTableModel) getModel();
			TreeTableNode node = (TreeTableNode) model.getNodeForRow(row);
			if (node.getType() != Task.getObjectType())
				return label; 
						
			PlanningTreeTaskNode taskNode = (PlanningTreeTaskNode) node;
			double nodeCostAlloctionProportion = taskNode.getCostAllocationProportion();
			if (nodeCostAlloctionProportion < 1.0)
				label.setText(label.getText() + "*");

			return label;
		}
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewBudgetAnnualTotalsTable";
}
