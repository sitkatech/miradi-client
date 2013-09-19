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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.SortableTableModel;

public abstract class AbstractTableModelComparator implements Comparator<ORef>
{
	public AbstractTableModelComparator(SortableTableModel modelToUse, int columnToSort)
	{
		model = modelToUse;
		columnToSortBy = columnToSort;	
	}
	
	protected Object getValue(ORef ref)
	{
		int foundAt = findRowContainingRef(ref);
		if(foundAt < 0)
			return null;
		return model.getValueAt(foundAt, columnToSortBy);
	}

	// TODO: Should this become part of the RowColumnBaseObjectProvider interface?
	// or should we cache the ref->row index map in this class for speed?
	private int findRowContainingRef(ORef ref)
	{
		int foundAt = -1;
		for(int row = 0; row < model.getRowCount(); ++row)
		{
			if(model.getBaseObjectForRowColumn(row, 0).getRef().equals(ref))
			{
				foundAt = row;
				break;
			}
		}
		return foundAt;
	}
	
	protected int compareDetails(Comparable[] sortValues1, Comparable[] sortValues2)
	{
		if (sortValues1.length != sortValues2.length)
			throw new RuntimeException("Arrays being compared have different lengths");
		
		for (int index = 0; index < sortValues1.length; ++index)
		{
			Comparable element1 = sortValues1[index];
			Comparable element2 = sortValues2[index];
			int compareToValue = compareValues(element1, element2);
			if (compareToValue != 0)
				return compareToValue;
		}
		
		EAM.logWarning("Arrays had identical elements");
		return 0;
	}

	abstract protected int compareValues(Comparable element1, Comparable element2);
	
	abstract public int compare(ORef ref1, ORef ref2);

	protected SortableTableModel model;
	protected int columnToSortBy;
}
