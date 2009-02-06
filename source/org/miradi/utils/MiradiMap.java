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
package org.miradi.utils;

import java.util.HashMap;

public class MiradiMap extends HashMap
{
	public MiradiMap(Object[][] data) throws Exception
	{
		load(data);
	}

	private void load(Object[][] data) throws Exception
	{
		for (int i = 0; i < data.length; ++i)
		{
			if (data[i].length != 2)
				throw new Exception("Invalid data was used to load MiradiMap");
			
			put(data[i][0], data[i][1]);
		}
	}
}
