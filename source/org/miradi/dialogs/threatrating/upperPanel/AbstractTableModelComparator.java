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
package org.miradi.dialogs.threatrating.upperPanel;

import java.util.Comparator;

import javax.swing.table.TableModel;

import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.objects.BaseObject;

public abstract class AbstractTableModelComparator implements Comparator
{
	public AbstractTableModelComparator(TableModel modelToUse, int columnToSort)
	{
		model = modelToUse;
		columnToSortBy = columnToSort;	
	}
	
	protected Object getValue(int row)
	{
		return model.getValueAt(row, columnToSortBy);
	}
	
	protected int compareDetails(Integer row1, Integer row2, Object value1, Object value2)
	{
		int compareValue = compareStrings(value1, value2);
		if (compareValue != 0)
			return compareValue;
		
		return compareUsingRef(row1.intValue(), row2.intValue());
	}

	private int compareStrings(Object value1, Object value2)
	{
		return value1.toString().compareToIgnoreCase(value2.toString());
	}
	
	private int compareUsingRef(int row1, int row2)
	{
		RowColumnBaseObjectProvider provider = ((RowColumnBaseObjectProvider)model);
		BaseObject baseObject1 = provider.getBaseObjectForRowColumn(row1, 0);
		BaseObject baseObject2 = provider.getBaseObjectForRowColumn(row2, 0);
		if (baseObject1 == null || baseObject2 == null)
			return 0;
	
		return baseObject1.getRef().compareTo(baseObject2.getRef());
	}

	abstract public int compare(Object o1, Object o2);

	protected TableModel model;
	protected int columnToSortBy;
}
