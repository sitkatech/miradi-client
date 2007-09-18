/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.objects.Desire;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class PlanningTreeTable extends TreeTableWithStateSaving
{
	public PlanningTreeTable(Project projectToUse, PlanningTreeModel planningTreeModelToUse)
	{
		super(projectToUse, planningTreeModelToUse);
		setTableColumnRenderer();
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	public void rebuildTableCompletely()
	{
		super.rebuildTableCompletely();
		setTableColumnRenderer();
		setColumnWidths();
	}

	private void setColumnWidths()
	{
		PlanningTreeModel model = (PlanningTreeModel) getTree().getModel();
		int columnCount = getColumnModel().getColumnCount();
		for (int i = 0; i < columnCount; ++i)
		{
			int realColumn = convertColumnIndexToModel(i);
			String columnTag = model.getColumnTag(realColumn);
			int columnWidth = getColumnWidth(realColumn, columnTag);
			setColumnWidth(realColumn, columnWidth);
		}
	}
	
	private int getColumnWidth(int columnIndex, String columnTag)
	{
		if (columnTag.equals("Item") || columnTag.equals(Indicator.PSEUDO_TAG_METHODS) || columnTag.equals(Desire.TAG_FULL_TEXT))		
			return 200;
			
		if (columnTag.equals(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML) || columnTag.equals(Task.PSEUDO_TAG_COMBINED_EFFORT_DATES))
			return 100;
		
		return getColumnHeaderWidth(columnIndex);
	}

	private void setTableColumnRenderer()
	{
		final int STARTING_TABLE_COLUMN = 1;
		int columnCount = getColumnModel().getColumnCount();
		for (int i  = STARTING_TABLE_COLUMN; i < columnCount; ++i)
		{	
			TableColumn tableColumn = getColumnModel().getColumn(i);
			tableColumn.setCellRenderer(new CustomRenderer(tree, getProject()));
		}
	}
	
	protected static class CustomRenderer extends DefaultTableCellRenderer
	{
		public CustomRenderer(TreeTableCellRenderer treeToUse, Project projectToUse)
		{
			tree = treeToUse;
			project = projectToUse;
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			PlanningTreeModel model = (PlanningTreeModel) tree.getModel();
			int modelColumn = table.convertColumnIndexToModel(column);
			String columnTag = model.getColumnTag(modelColumn);
			Color backgroundColor = getBackgroundColor(columnTag);
			setBackground(backgroundColor);
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
			
			if(columnTag.equals(Task.PSEUDO_TAG_TASK_TOTAL))
				return AppPreferences.BUDGET_TOTAL_TABLE_BACKGROUND;
				
			return Color.white;
		}

		TreeTableCellRenderer tree;
		Project project;
	}
}
