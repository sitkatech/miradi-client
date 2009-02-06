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

	protected int compareStrings(Object value1, Object value2)
	{
		return value1.toString().compareToIgnoreCase(value2.toString());
	}
	
	abstract public int compare(Object o1, Object o2);

	protected TableModel model;
	protected int columnToSortBy;
}
