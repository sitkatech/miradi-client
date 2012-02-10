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

import org.miradi.objecthelpers.ORef;
import org.miradi.utils.SortableTableModel;

public class TableModelStringComparator extends AbstractTableModelComparator
{
	public TableModelStringComparator(SortableTableModel modelToUse, int columnToSort)
	{
		super(modelToUse, columnToSort);
	}

	@Override
	public int compare(ORef ref1, ORef ref2)
	{
		Object value1 = getValue(ref1);
		Object value2 = getValue(ref2);
		
		if (value1 == null || value2 == null)
			return -1;
		
		Comparable[] sortValues1 = new Comparable[]{value1.toString(), ref1};
		Comparable[] sortValues2 = new Comparable[]{value2.toString(), ref2};
		
		return compareDetails(sortValues1, sortValues2);
	}
	
	@Override
	protected int compareValues(Comparable element1, Comparable element2)
	{
		return element1.toString().compareToIgnoreCase(element2.toString());
	}
	
}
