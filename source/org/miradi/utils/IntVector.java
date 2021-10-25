/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.utils;

import java.util.Vector;

public class IntVector
{
	public IntVector()
	{
		integers = new Vector<Integer>();
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
		return integers.get(index).intValue();
	}
	
	public int size()
	{
		return integers.size();
	}
	
	public boolean contains(int item)
	{
		return integers.contains(new Integer(item));
	}
	
	public int[] asIntArray()
	{
		int[] selection = new int[size()];
		for (int index = 0; index < size(); ++index)
		{
			selection[index] = get(index);
		}
	
		return selection;
	}

	private Vector<Integer> integers;
}
