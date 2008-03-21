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
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.miradi.dialogs.tablerenderers.BasicTableCellRenderer;
import org.miradi.dialogs.tablerenderers.BudgetCostTreeTableCellRenderer;
import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRenderer;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.tablerenderers.TableCellRendererForObjects;
import org.miradi.dialogs.treetables.TreeTableWithColumnWidthSaving;
import org.miradi.main.AppPreferences;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.utils.TableWithRowHeightManagement;

public class PlanningTreeTable extends TreeTableWithColumnWidthSaving implements RowColumnBaseObjectProvider, TableWithRowHeightManagement
{
	public PlanningTreeTable(Project projectToUse, PlanningTreeTableModel planningTreeModelToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(projectToUse, planningTreeModelToUse);
		fontProvider = fontProviderToUse;
		setTableColumnRenderer();
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public void rebuildTableCompletely() throws Exception
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
			return new BudgetCostTreeTableCellRenderer(this, getTreeTableAdapter(), fontProvider);
		if(isQuestionColumn(columnTag))
			return new ChoiceItemTableCellRenderer(this, fontProvider);
		return new TableCellRendererForObjects(this, fontProvider);
	}
	
	protected Color getBackgroundColor(String columnTag)
	{
		if (columnTag.equals(BaseObject.PSEUDO_TAG_WHO_TOTAL))
			return AppPreferences.RESOURCE_TABLE_BACKGROUND;
		
		if (columnTag.equals(Indicator.PSEUDO_TAG_METHODS))
			return AppPreferences.INDICATOR_COLOR;
		
		if(columnTag.equals(BaseObject.PSEUDO_TAG_WHEN_TOTAL))
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
		
		if(columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return true;
		
		return false;
	}

	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getNodeForRow(row).getObject();
	}

	public static final String UNIQUE_IDENTIFIER = "PlanningTreeTable";

	private FontForObjectTypeProvider fontProvider;
}
