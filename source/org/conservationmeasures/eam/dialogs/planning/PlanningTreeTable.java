/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.TreeTableWithColumnWidthSaving;

public class PlanningTreeTable extends TreeTableWithColumnWidthSaving
{
	public PlanningTreeTable(Project projectToUse, PlanningTreeModel planningTreeModelToUse)
	{
		super(projectToUse, planningTreeModelToUse);
		setTableColumnRenderer();
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public void rebuildTableCompletely()
	{
		super.rebuildTableCompletely();
		setTableColumnRenderer();
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
	
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	protected class CustomRenderer extends DefaultTableCellRenderer
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
			if (model.getColumnTag(column).equals(BaseObject.PSEUDO_TAG_BUDGET_TOTAL))
				setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
			
			setFont(getSharedTaskFont(((TreeTableNode) getObjectForRow(row))));
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

		TreeTableCellRenderer tree;
		Project project;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningTreeTable";
}
