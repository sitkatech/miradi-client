/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

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
