/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatTargetTableModel;
import org.miradi.dialogs.threatrating.upperPanel.MainThreatTableModel;
import org.miradi.main.EAM;
import org.miradi.objects.Factor;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.SortDirectionQuestion;

public class MultiTableRowSortController
{
	public MultiTableRowSortController(Project projectToUse)
	{
		project = projectToUse;
		tablesToSort = new Vector();
	}
	
	public void addTableToSort(TableWithRowHeightSaver tableToSort) throws Exception
	{
		JTableHeader columnHeader = tableToSort.getTableHeader();
		ColumnSortListener sortListener = new ColumnSortListener();
		columnHeader.addMouseListener(sortListener);
		tablesToSort.add(tableToSort);
		
		sortNewlyAddedTable(tableToSort);
	}

	private void sortNewlyAddedTable(TableWithRowHeightSaver tableToSort) throws Exception
	{
		MainThreatTableModel model = getCastedModel(tableToSort);
		int columnToSort = findColumnToSortBy(model);
		if (columnToSort >= 0)
		{
			sortTable(tableToSort, columnToSort);
			if (shouldReverseSort(model))
				sortTable(tableToSort, columnToSort);
		}
	}

	private boolean shouldReverseSort(MainThreatTableModel model) throws Exception
	{
		TableSettings tableSettings = findOrCreateTableSettings(model);
		String columnSortDirectionCode = tableSettings.getData(TableSettings.TAG_COLUMN_SORT_DIRECTION);
		
		return columnSortDirectionCode.equals(SortDirectionQuestion.DESCENDING_SORT_CODE);
	}

	private int findColumnToSortBy(MainThreatTableModel model) throws Exception
	{
		TableSettings tableSettings = findOrCreateTableSettings(model);
		String columnSortTag = tableSettings.getData(TableSettings.TAG_COLUMN_SORT_TAG);
		for (int column = 0; column < model.getColumnCount(); ++column)
		{
			String columnTag = model.getColumnGroupCode(column);
			if (columnTag.equals(columnSortTag))
				return column;
		}
		
		return -1;
	}

	private void sortTable(JTable tableToSort, final int sortByTableColumn)
	{
		MainThreatTableModel model = getCastedModel(tableToSort);
		int modelColumn = tableToSort.convertColumnIndexToModel(sortByTableColumn);
		Factor[] sortedThreats = model.getThreatsSortedBy(modelColumn);
		for (int index = 0; index < tablesToSort.size(); ++index)
		{
			TableWithRowHeightSaver table = tablesToSort.get(index);
			AbstractThreatTargetTableModel modelToSetThreats = (AbstractThreatTargetTableModel)table.getModel();
			modelToSetThreats.setThreats(sortedThreats);
			table.updateAutomaticRowHeights();
			table.revalidate();
			table.repaint();
		}
	}
	
	private TableSettings findOrCreateTableSettings(MainThreatTableModel model)	throws Exception
	{
		String uniqueTableIdentifier = model.getUniqueTableModelIdentifier();
		return TableSettings.findOrCreate(getProject(), uniqueTableIdentifier);
	}	
	
	private void saveColumnSortTag(TableSettings tableSettings, String columnSortTag) throws Exception
	{
		saveUsingCommand(tableSettings, TableSettings.TAG_COLUMN_SORT_TAG, columnSortTag);
	}
	
	private void saveColumnSortDirection(TableSettings tableSettings, String columnSortDirection) throws Exception
	{
		saveUsingCommand(tableSettings, TableSettings.TAG_COLUMN_SORT_DIRECTION, columnSortDirection);	
	}

	private void saveUsingCommand(TableSettings tableSettings, String tag, String value) throws Exception
	{
		CommandSetObjectData setColumnWidths = new CommandSetObjectData(tableSettings.getRef(), tag, value);
		getProject().executeCommand(setColumnWidths);
	}
	
	private void clearAllTableSettings() throws Exception
	{
		for(TableWithRowHeightSaver table : tablesToSort)
		{
			MainThreatTableModel model = getCastedModel(table);
			TableSettings tableSettings = findOrCreateTableSettings(model);
			saveColumnSortTag(tableSettings, "");
			saveColumnSortDirection(tableSettings, "");
		}
	}
	
	private MainThreatTableModel getCastedModel(JTable tableToUse)
	{
		return (MainThreatTableModel)tableToUse.getModel();
	}

	private Project getProject()
	{
		return project;
	}
	
	class ColumnSortListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent event) 
		{
			try
			{
				JTableHeader tableHeader = ((JTableHeader)event.getSource());
				int clickedTableColumn = tableHeader.columnAtPoint(event.getPoint());
				if (clickedTableColumn >= 0)
					sortByTableColumn(tableHeader.getTable(), clickedTableColumn);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error Occurred During Sorting"));
			}
		}

		private void sortByTableColumn(JTable tableClickedOn, int sortByTableColumn) throws Exception
		{
			MainThreatTableModel model = getCastedModel(tableClickedOn);
			TableSettings tableSettings = findOrCreateTableSettings(model);
			String columnSortTag = model.getColumnGroupCode(sortByTableColumn);
			String currentSortDirection = getSortDirectionCode(tableSettings, columnSortTag);
			
			clearAllTableSettings();
			
			saveColumnSortTag(tableSettings, columnSortTag);
			saveColumnSortDirection(tableSettings, currentSortDirection);
			sortTable(tableClickedOn, sortByTableColumn);
		}

		private String getSortDirectionCode(TableSettings tableSettings, String columnSortTag)
		{
			String existingSortDirection = tableSettings.getData(TableSettings.TAG_COLUMN_SORT_DIRECTION);
			String existingSortTag = tableSettings.getData(TableSettings.TAG_COLUMN_SORT_TAG);
			if (existingSortTag.equals(columnSortTag))
				return SortDirectionQuestion.getReversedSortDirectionCode(existingSortDirection);
			
			return SortDirectionQuestion.ASCENDING_SORT_CODE;
		}
	}
	
	private Vector<TableWithRowHeightSaver> tablesToSort;
	private Project project;
}
