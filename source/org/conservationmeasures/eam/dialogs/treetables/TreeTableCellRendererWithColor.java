/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
/**
 * 
 */
package org.conservationmeasures.eam.dialogs.treetables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.conservationmeasures.eam.dialogs.planning.PlanningTreeTable;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithIcons.Renderer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.Task;

public class TreeTableCellRendererWithColor extends RendererWithHelperMethods
{
	public TreeTableCellRendererWithColor(TreeTableWithIcons treeTableToUse)
	{
		treeTable = treeTableToUse;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		GenericTreeTableModel model = treeTable.getTreeTableModel();
		int modelColumn = table.convertColumnIndexToModel(column);
		String columnTag = model.getColumnTag(modelColumn);
		Color backgroundColor = getBackgroundColor(columnTag);
		setBackground(backgroundColor);
		if (model.getColumnTag(column).equals(BaseObject.PSEUDO_TAG_BUDGET_TOTAL))
			setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		
		addAstrickToTaskRows(getNodeForRow(row), label);		
		
		setFont(getRowFont(row));
		setGrid();
		if (isSelected)
			setBackground(table.getSelectionBackground());
		
		return label;
	}

	protected Font getRowFont(int row)
	{
		TreeTableNode objectForRow = getNodeForRow(row);
		if (objectForRow.getType() == Target.getObjectType() || objectForRow.getType() == ObjectType.FAKE)
			return Renderer.getBoldFont();
		
		return PlanningTreeTable.getSharedTaskFont((objectForRow));
	}

	private void setGrid()
	{
		setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.black));
	}
	
	protected Color getBackgroundColor(String columnTag)
	{
		if (columnTag.equals(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML))
			return AppPreferences.RESOURCE_TABLE_BACKGROUND;
		
		if (columnTag.equals(Indicator.PSEUDO_TAG_METHODS))
			return AppPreferences.INDICATOR_COLOR;
		
		if(columnTag.equals(Task.PSEUDO_TAG_COMBINED_EFFORT_DATES))
			return AppPreferences.WORKPLAN_TABLE_BACKGROUND;
		
		if(columnTag.equals(Task.PSEUDO_TAG_BUDGET_TOTAL))
			return AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND;
			
		return Color.white;
	}

	public TreeTableNode getNodeForRow(int row)
	{
		return treeTable.getNodeForRow(row);
	}

	protected TreeTableWithIcons treeTable;
}