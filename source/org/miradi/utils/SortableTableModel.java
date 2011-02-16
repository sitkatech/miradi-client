/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.threatrating.upperPanel.TableModelStringComparator;
import org.miradi.questions.SortDirectionQuestion;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;


abstract public class SortableTableModel extends AbstractTableModel implements ColumnTagProvider, RowColumnBaseObjectProvider
{
	protected Vector<Integer> getSortedRowIndexes(int sortByTableColumn, String sortDirectionCode)
	{
		Vector<Integer> rows = new Vector<Integer>();
		for(int index = 0; index < getRowCount(); ++index)
		{
			rows.add(new Integer(index));
		}
		
		Collections.sort(rows, createComparator(sortByTableColumn));
		if (sortDirectionCode.equals(SortDirectionQuestion.REVERSED_SORT_ORDER_CODE))
			Collections.reverse(rows);
		
		return rows;
	}
	
	public String getColumnGroupCode(int column)
	{
		return getColumnTag(column);
	}

	public Comparator<Integer> createComparator(int columnToSortOn)
	{
		return new TableModelStringComparator(this, columnToSortOn);
	}
	
	public abstract String getUniqueTableModelIdentifier();

	public void setSortedRowIndexes(Vector<Integer> sortedRowIndexes)
	{
		throw new RuntimeException("This model does not support sorted indexes");
	}
	
	public String getWorkPlanBudgetMode()
	{
		return WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE;
	}
}
