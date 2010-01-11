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
import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatPerRowTableModel;
import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatTargetTableModel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objects.Factor;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.questions.SortDirectionQuestion;

public class MultiTableRowSortController implements CommandExecutedListener
{
	public MultiTableRowSortController(Project projectToUse)
	{
		project = projectToUse;
		tablesToSort = new Vector();
		
		project.addCommandExecutedListener(this);
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	public void addTableToSort(TableWithRowHeightSaver tableToSort) throws Exception
	{
		JTableHeader columnHeader = tableToSort.getTableHeader();
		ColumnSortListener sortListener = new ColumnSortListener();
		columnHeader.addMouseListener(sortListener);
		tablesToSort.add(tableToSort);
		
		sortNewlyAddedTable(tableToSort);
	}

	private void sortNewlyAddedTable(JTable tableToSort) throws Exception
	{
		AbstractThreatPerRowTableModel model = getCastedModel(tableToSort);
		int columnToSort = findColumnToSortBy(model);
		if (columnToSort >= 0)
		{
			sortTable(tableToSort, columnToSort);
			if (shouldReverseSort(model))
				sortTable(tableToSort, columnToSort);
		}
	}

	private boolean shouldReverseSort(AbstractThreatPerRowTableModel model) throws Exception
	{
		TableSettings tableSettings = findOrCreateTableSettings(model);
		String columnSortDirectionCode = tableSettings.getData(TableSettings.TAG_COLUMN_SORT_DIRECTION);
		
		return columnSortDirectionCode.equals(SortDirectionQuestion.DESCENDING_SORT_CODE);
	}

	private int findColumnToSortBy(AbstractThreatPerRowTableModel model) throws Exception
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
		AbstractThreatPerRowTableModel model = getCastedModel(tableToSort);
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
	
	private TableSettings findOrCreateTableSettings(AbstractThreatPerRowTableModel model)	throws Exception
	{
		String uniqueTableIdentifier = model.getUniqueTableModelIdentifier();
		return TableSettings.findOrCreate(getProject(), uniqueTableIdentifier);
	}	
	
	private void storeSameDataForAllTables(String columnSortTag, String columnSortDirection) throws Exception
	{
		for (TableWithRowHeightSaver table : tablesToSort)
		{
			AbstractThreatPerRowTableModel model = getCastedModel(table);
			TableSettings tableSettings = findOrCreateTableSettings(model);
			
			saveUsingCommand(tableSettings, TableSettings.TAG_COLUMN_SORT_TAG, columnSortTag);
			saveUsingCommand(tableSettings, TableSettings.TAG_COLUMN_SORT_DIRECTION, columnSortDirection);
		}
	}
	
	private void saveUsingCommand(TableSettings tableSettings, String tag, String value) throws Exception
	{
		CommandSetObjectData setColumnData = new CommandSetObjectData(tableSettings.getRef(), tag, value);
		getProject().executeCommand(setColumnData);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_COLUMN_SORT_TAG) ||
				event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_COLUMN_SORT_DIRECTION))
			{
				CommandSetObjectData setCommand = event.getSetCommand();
				TableSettings tableSettings = TableSettings.find(getProject(), setCommand.getObjectORef());
				findAndSortTableForTableSettings(tableSettings);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("An Error Occurred During Sorting."));
		}
	}

	private void findAndSortTableForTableSettings(TableSettings tableSettingsToUse) throws Exception
	{
		JTable table = findTableWithTableSettings(tableSettingsToUse);
		if (table == null)
			return;
		
		AbstractThreatPerRowTableModel model = getCastedModel(table);
		int columnToSortByForTable = findColumnToSortBy(model);
		if (columnToSortByForTable >= 0)
			sortTable(table, columnToSortByForTable);
	}
	
	private JTable findTableWithTableSettings(TableSettings tableSettingsToUse)
	{
		for(JTable table : tablesToSort)
		{
			if (getCastedModel(table).getUniqueTableModelIdentifier().equals(tableSettingsToUse.getUniqueIdentifier()))
				return table;
		}
		
		return null;
	}
	
	private AbstractThreatPerRowTableModel getCastedModel(JTable tableToUse)
	{
		return (AbstractThreatPerRowTableModel)tableToUse.getModel();
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
			AbstractThreatPerRowTableModel model = getCastedModel(tableClickedOn);
			TableSettings tableSettings = findOrCreateTableSettings(model);
			String columnSortTag = model.getColumnGroupCode(sortByTableColumn);
			String currentSortDirection = getSortDirectionCode(tableSettings, columnSortTag);
			
			saveColumnSortDataAsTransaction(columnSortTag, currentSortDirection);
		}

		private void saveColumnSortDataAsTransaction(String columnSortTag,	String currentSortDirection) throws Exception
		{
			getProject().executeBeginTransaction();
			try
			{
				storeSameDataForAllTables(columnSortTag, currentSortDirection);
			}
			finally 
			{
				getProject().executeEndTransaction();
			}
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
