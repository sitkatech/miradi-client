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

import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class PlanningTreeTable extends TreeTableWithStateSaving
{
	public PlanningTreeTable(Project projectToUse, PlanningTreeModel planningTreeModelToUse)
	{
		super(projectToUse, planningTreeModelToUse);
		rebuild();
	}

	public void rebuild()
	{
		setTableColumnRenderer();
	}
	
	private void setTableColumnRenderer()
	{
		final int STARTING_TABLE_COLUMN = 1;
		int columnCount = getColumnModel().getColumnCount();
		for (int i  = STARTING_TABLE_COLUMN; i < columnCount; ++i)
		{	
			TableColumn tableColumn = getColumnModel().getColumn(i);
			tableColumn.setCellRenderer(new CustomRenderer(tree));
		}
	}
	
	protected static class CustomRenderer extends DefaultTableCellRenderer
	{
		public CustomRenderer(TreeTableCellRenderer treeToUse)
		{
			tree = treeToUse;
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			PlanningTreeModel model = (PlanningTreeModel) tree.getModel();
			String columnTag = model.getColumnTag(column);
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
				return new Color(200, 200, 255);
			
			if (columnTag.equals(Indicator.PSEUDO_TAG_METHODS))
				return new Color(200, 170, 250);
				
			return Color.white;
		}

		TreeTableCellRenderer tree;
	}
}
