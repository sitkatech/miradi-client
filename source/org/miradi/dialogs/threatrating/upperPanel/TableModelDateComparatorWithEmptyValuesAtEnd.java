/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.utils.SortableTableModel;

public class TableModelDateComparatorWithEmptyValuesAtEnd extends TableModelStringComparator
{
	public TableModelDateComparatorWithEmptyValuesAtEnd(SortableTableModel modelToUse, int columnToSort)
	{
		super(modelToUse, columnToSort);
	}
	
	@Override
	protected int compareValues(Comparable element1, Comparable element2)
	{
		final boolean isElement1Empty = element1.toString().length() == 0;
		final boolean isElement2Empty = element2.toString().length() == 0;
		if (isElement1Empty && isElement2Empty)
			return 0;

		if (isElement1Empty)
			return 1;
		
		if (isElement2Empty)
			return -1;
		
		return element1.toString().compareToIgnoreCase(element2.toString());
	}
}
