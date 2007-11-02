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

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.conservationmeasures.eam.dialogs.planning.PlanningTreeModel;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeTable;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class TreeTableCellRendererWithColor extends DefaultTableCellRenderer
{
	public TreeTableCellRendererWithColor(TreeTableWithIcons treeTableToUse, Project projectToUse)
	{
		treeTable = treeTableToUse;
		project = projectToUse;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		PlanningTreeModel model = (PlanningTreeModel) treeTable.getTreeTableModel();
		int modelColumn = table.convertColumnIndexToModel(column);
		String columnTag = model.getColumnTag(modelColumn);
		Color backgroundColor = getBackgroundColor(columnTag);
		setBackground(backgroundColor);
		if (model.getColumnTag(column).equals(BaseObject.PSEUDO_TAG_BUDGET_TOTAL))
			setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		
		table.setFont(PlanningTreeTable.getSharedTaskFont(((TreeTableNode) treeTable.getObjectForRow(row))));
		setGrid();
		
		if (isSelected)
			setBackground(table.getSelectionBackground());
		
		return component;
	}

	private void setGrid()
	{
		setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.black));
	}
	
	private Color getBackgroundColor(String columnTag)
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

	TreeTableWithIcons treeTable;
	Project project;
}