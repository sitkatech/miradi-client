/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

public class IgnoreCaseStringComparator implements Comparable, Comparator
{
	public IgnoreCaseStringComparator(int rowToUse, String string)
	{
		oldRow = rowToUse;
		object = string;
	}

	public IgnoreCaseStringComparator(int rowToUse, Object objectToUse)
	{
		oldRow = rowToUse;
		object = objectToUse.toString();
	}
	
	public int compare(Object object1, Object object2)
	{
		return object1.toString().compareToIgnoreCase(object2.toString());
	}

	public int compareTo(Object objectToUse)
	{
		return object.toString().compareToIgnoreCase(objectToUse.toString());
	}

	public boolean equals(Object objectToUse)
	{
		return object.toString().compareToIgnoreCase(objectToUse.toString())==0;
	}

	public int getOldRow()
	{
		return oldRow;
	}

	public String getObject()
	{
		return object;
	}

	public String toString()
	{
		return object.toString();
	}
	
	int oldRow;

	String object;

}
