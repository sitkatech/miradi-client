/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.util.Vector;

public class IntVector
{
	public IntVector()
	{
		integers = new Vector();
	}
	
	public void add(int intToAdd)
	{
		integers.add(new Integer(intToAdd));
	}
	
	public void remove(int item)
	{
		integers.remove(new Integer(item));
	}
	
	public int get(int index)
	{
		return ((Integer) integers.get(index)).intValue();
	}
	
	public int size()
	{
		return integers.size();
	}
	
	public boolean contains(int item)
	{
		return integers.contains(new Integer(item));
	}
	
	Vector integers;
}
