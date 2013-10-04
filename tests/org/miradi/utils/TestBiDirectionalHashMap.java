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

package org.miradi.utils;

import org.miradi.main.MiradiTestCase;

public class TestBiDirectionalHashMap extends MiradiTestCase
{
	public TestBiDirectionalHashMap(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		BiDirectionalHashMap map = new BiDirectionalHashMap();
		verifyBidirectionalMapContent(map, "1", "a");
		verifyBidirectionalMapContent(map, "1", "b");
		verifyBidirectionalMapContent(map, "2", "b");
	}

	private void verifyBidirectionalMapContent(BiDirectionalHashMap map, final String key, final String value) throws Exception
	{
		map.put(key, value);
		assertEquals("Incorrect map size?", 1, map.size());
		assertEquals("Incorrect value for key", value, map.getValue(key));
		assertEquals("Incorrect key for value", key, map.getKey(value));
	}
}
