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

import javax.swing.table.TableModel;

public class TableModelStringComparator extends AbstractTableModelComparator
{
	public TableModelStringComparator(TableModel modelToUse, int columnToSort)
	{
		super(modelToUse, columnToSort);
	}

	@Override
	public int compare(Integer row1, Integer row2)
	{
		Object value1 = getValue(row1.intValue());
		Object value2 = getValue(row2.intValue());
		
		Comparable[] sortValues1 = new Comparable[]{value1.toString(), getRefForRow(row1)};
		Comparable[] sortValues2 = new Comparable[]{value2.toString(), getRefForRow(row2)};
		
		return compareDetails(sortValues1, sortValues2);
	}
	
	protected int compareValues(Comparable element1, Comparable element2)
	{
		return element1.toString().compareToIgnoreCase(element2.toString());
	}
	
}
