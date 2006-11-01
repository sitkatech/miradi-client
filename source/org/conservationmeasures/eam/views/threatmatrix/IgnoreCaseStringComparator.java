/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.util.Comparator;

public class IgnoreCaseStringComparator implements  Comparator
{

	public IgnoreCaseStringComparator() 
	{
	}
	
	public int compare(Object object1, Object object2)
	{
		return object1.toString().compareToIgnoreCase(object2.toString());
	}

	String object;

}
