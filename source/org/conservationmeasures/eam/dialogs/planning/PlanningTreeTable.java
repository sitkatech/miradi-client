/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableCellRendererWithColor;
import org.conservationmeasures.eam.project.Project;
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
			tableColumn.setCellRenderer(new TreeTableCellRendererWithColor(this, getProject()));
		}
	}
	
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	public static final String UNIQUE_IDENTIFIER = "PlanningTreeTable";
}
