/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.dialogs.treetables.BasicTableCellRenderer;
import org.conservationmeasures.eam.dialogs.treetables.ChoiceItemTableCellRenderer;
import org.conservationmeasures.eam.dialogs.treetables.NumericTableCellRenderer;
import org.conservationmeasures.eam.dialogs.treetables.RowBaseObjectProvider;
import org.conservationmeasures.eam.dialogs.treetables.TableCellRendererForObjects;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithColumnWidthSaving;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeTable extends TreeTableWithColumnWidthSaving implements RowBaseObjectProvider
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
			String columnTag = getColumnTag(i);
			BasicTableCellRenderer renderer = createRendererForColumn(columnTag);
			renderer.setCellBackgroundColor(getBackgroundColor(columnTag));

			TableColumn tableColumn = getColumnModel().getColumn(i);
			tableColumn.setCellRenderer(renderer);
		}
	}

	private BasicTableCellRenderer createRendererForColumn(String columnTag)
	{
		if(columnTag.equals(Task.PSEUDO_TAG_BUDGET_TOTAL))
			return new NumericTableCellRenderer(this);
		if(isQuestionColumn(columnTag))
			return new ChoiceItemTableCellRenderer(this);
		return new TableCellRendererForObjects(this);
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
	
	public boolean isQuestionColumn(String columnTag)
	{
		if(columnTag.equals(Strategy.PSEUDO_TAG_RATING_SUMMARY))
			return true;
		
		if(columnTag.equals(Indicator.TAG_PRIORITY))
			return true;
		
		if(columnTag.equals(Indicator.TAG_STATUS))
			return true;
		
		return false;
	}

	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	public BaseObject getBaseObjectForRow(int row)
	{
		return getNodeForRow(row).getObject();
	}

	public static final String UNIQUE_IDENTIFIER = "PlanningTreeTable";

}
