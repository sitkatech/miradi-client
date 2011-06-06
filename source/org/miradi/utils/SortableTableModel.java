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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.questions.SortDirectionQuestion;


abstract public class SortableTableModel extends AbstractTableModel implements ColumnTagProvider, RowColumnBaseObjectProvider
{
	protected ORefList getSortedRefs(int sortByTableColumn, String sortDirectionCode)
	{
		Vector<ORef> sortedRefs = getCurrentSortedRefs();
		
		Collections.sort(sortedRefs, createComparator(sortByTableColumn));
		if (sortDirectionCode.equals(SortDirectionQuestion.REVERSED_SORT_ORDER_CODE))
			Collections.reverse(sortedRefs);
		
		return new ORefList(sortedRefs);
	}

	public Vector<ORef> getCurrentSortedRefs()
	{
		Vector<ORef> sortedRefs = new Vector<ORef>();
		for(int row = 0; row < getRowCount(); ++row)
		{
			sortedRefs.add(getBaseObjectForRowColumn(row, 0).getRef());
		}
		return sortedRefs;
	}
	
	public String getColumnGroupCode(int column)
	{
		return getColumnTag(column);
	}

	public Comparator<ORef> createComparator(int columnToSortOn)
	{
		return new TableModelStringComparator(this, columnToSortOn);
	}
	
	public abstract String getUniqueTableModelIdentifier();

	public void setSortedRefs(ORefList sortedRowIndexes)
	{
		throw new RuntimeException("This model does not support sorted indexes");
	}
}
