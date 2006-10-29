/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

import org.conservationmeasures.eam.objects.ValueOption;

public class ColumnObject implements Comparable, Comparator
{
	public ColumnObject(int rowToUse, Object objectToUse)
	{
		row = rowToUse;
		object = objectToUse;
	}

	public int compare(Object object1, Object object2)
	{
		ValueOption value1 = (ValueOption) ((ColumnObject) object1).getObject();
		ValueOption value2 = (ValueOption) ((ColumnObject) object2).getObject();
		Integer s1 = new Integer((value1).getNumericValue());
		Integer s2 = new Integer((value2).getNumericValue());
		return s1.compareTo(s2);
	}

	public int compareTo(Object objectToUse)
	{
		ValueOption value1 = (ValueOption) getObject();
		ValueOption value2 = (ValueOption) ((ColumnObject) objectToUse).getObject();
		Integer s1 = new Integer((value1).getNumericValue());
		Integer s2 = new Integer((value2).getNumericValue());
		return s1.compareTo(s2);
	}

	public boolean equals(Object obj)
	{
		return false;
	}

	public int getOldRow()
	{
		return row;
	}

	public Object getObject()
	{
		return object;
	}

	int row;

	Object object;

}
