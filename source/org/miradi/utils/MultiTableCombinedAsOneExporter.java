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

import java.util.Vector;

import org.miradi.dialogs.planning.propertiesPanel.AboveBudgetColumnsBar;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;

public class MultiTableCombinedAsOneExporter extends AbstractTableExporter
{
	public MultiTableCombinedAsOneExporter(Project projectToUse)
	{
		super(projectToUse);
		
		clear();
	}

	public void clear()
	{
		tables = new Vector<AbstractTableExporter>();
	}
	
	public void addAsMasterTable(AbstractTableExporter table)
	{
		tables.add(MASTER_TABLE_INDEX, table);
	}
	
	public void addExportable(AbstractTableExporter table)
	{
		tables.add(table);
	}
	
	@Override
	public int getColumnCount()
	{
		int combinedCount = 0;
		for (int i = 0;  i < tables.size(); ++i)
		{
			combinedCount += tables.get(i).getColumnCount();
		}
		
		return combinedCount;
	}

	@Override
	public int getModelDepth(int row, int modelColumn)
	{
		if (tables.size() == 0)
			return 0;
		
		return tables.get(0).getModelDepth(row, modelColumn);
	}

	@Override
	public int getMaxDepthCount()
	{
		if (tables.size() == 0)
			return 0;
		
		return tables.get(0).getMaxDepthCount();	
	}

	@Override
	public int getRowCount()
	{
		if (tables.size() > 0) 
			return tables.get(0).getRowCount();
		
		return 0;
	}
	
	public String getColumnGroupName(int modelColumn)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(modelColumn);
		return tableAndColumnHolder.getTable().getColumnGroupName(tableAndColumnHolder.getColumn());
	}

	@Override
	public String getModelColumnName(int modelColumn)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(modelColumn);
		return tableAndColumnHolder.getTable().getModelColumnName(tableAndColumnHolder.getColumn());
	}
	
	@Override
	public ChoiceItem getModelChoiceItemAt(int row, int modelColumn)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(modelColumn);		
		return tableAndColumnHolder.getTable().getModelChoiceItemAt(row, tableAndColumnHolder.getColumn());
	}
	
	@Override
	public String getStyleTagAt(int row, int column)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(column);		
		return tableAndColumnHolder.getTable().getStyleTagAt(row, column);
	}

	@Override
	public int getRowType(int row)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(0);
		return  tableAndColumnHolder.getTable().getRowType(row);
	}

	@Override
	public String getModelTextAt(int row, int modelColumn)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(modelColumn);
		Object value = tableAndColumnHolder.getTable().getModelTextAt(row, tableAndColumnHolder.getColumn());
		return getSafeValue(value);
	}
			
	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		TableAndColumnHolder tableAndColumnHolder = getTableAndColumn(0);
		return tableAndColumnHolder.getTable().getBaseObjectForRow(row);
	}

	protected TableAndColumnHolder getTableAndColumn(int modelColumn)
	{
		int columnWithinTable = modelColumn;
		int thisColumnCount = 0;
		int tableStartingColumn = 0;
		for (int i = 0; i < tables.size(); ++i)
		{
			AbstractTableExporter thisTable = tables.get(i);
			thisColumnCount += thisTable.getColumnCount();
			if (thisColumnCount <= modelColumn)
			{
				columnWithinTable -= thisTable.getColumnCount();
				tableStartingColumn += thisTable.getColumnCount();
				continue;
			}
			
			return new TableAndColumnHolder(thisTable, columnWithinTable, tableStartingColumn);
		}
		
		throw new RuntimeException("Error occurred while exporting table.");
	}
	
	protected class TableAndColumnHolder
	{
		public TableAndColumnHolder(AbstractTableExporter tableToUse, int columnToUse, int tableStartingColumnToUse)
		{
			table = tableToUse;
			column = columnToUse;
			tableStartingColumn = tableStartingColumnToUse;
		}
		
		public int getColumn()
		{
			return column;
		}
		
		public AbstractTableExporter getTable()
		{
			return table;
		}
		
		public int getTableStartingColumn()
		{
			return tableStartingColumn;
		}
		
		private AbstractTableExporter table;
		private int column;
		private int tableStartingColumn;
	}
	
	@Override
	public ORefList getAllRefs(int objectType)
	{
		return getMasterTable().getAllRefs(objectType);
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		return getMasterTable().getAllTypes();
	}
	
	private TableExporter getMasterTable()
	{
		return getTables().get(MASTER_TABLE_INDEX);
	}
	
	protected Vector<AbstractTableExporter> getTables()
	{
		return tables;
	}
	
	public int convertToModelColumn(int tableColumn)
	{
		int pretendModelColumn = tableColumn;
		TableAndColumnHolder tableAndColumn = getTableAndColumn(pretendModelColumn);
		AbstractTableExporter exporter = tableAndColumn.getTable();
		int relativeTableColumn = tableAndColumn.getColumn();
		int relativeModelColumn = exporter.convertToModelColumn(relativeTableColumn);
		int absoluteModelColumn = relativeModelColumn + tableAndColumn.getTableStartingColumn();
		
		return absoluteModelColumn;
	}
	
	@Override
	public String getAboveColumnHeaderText(int tableColumn)
	{
		int modelColumn = convertToModelColumn(tableColumn);
		String columnGroupName = getColumnGroupName(modelColumn);
		if (WorkPlanColumnConfigurationQuestion.getAllPossibleWorkUnitsColumnGroups().contains(columnGroupName))
			return AboveBudgetColumnsBar.getWorkUnitsAboveColumnLabel();
		
		if (WorkPlanColumnConfigurationQuestion.getAllPossibleExpensesColumnGroups().contains(columnGroupName))
			return AboveBudgetColumnsBar.getExpensesAboveColumnLabel();
		
		if (WorkPlanColumnConfigurationQuestion.getAllPossibleBudgetTotalsColumnGroups().contains(columnGroupName))
			return AboveBudgetColumnsBar.getBudgetTotalsAboveColumnLabel();
		
		return "";
	}

	private Vector<AbstractTableExporter> tables;
	private static final int MASTER_TABLE_INDEX = 0;
}
