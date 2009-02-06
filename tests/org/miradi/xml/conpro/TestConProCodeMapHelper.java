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
package org.miradi.xml.conpro;

import java.util.HashMap;

import org.miradi.main.EAMTestCase;

public class TestConProCodeMapHelper extends EAMTestCase
{
	public TestConProCodeMapHelper(String name)
	{
		super(name);
	}

	public void testReverseMap()
	{
		String VALUE_1 = "value1";
		String KEY_1 = "key1";		
		String VALUE_2 = "value2";
		String KEY_2 = "key2";
		
		HashMap map = new HashMap<String, String>();
		map.put(KEY_1, VALUE_1);
		map.put(KEY_2, VALUE_2);
		
		HashMap<String, String> reversedMap = ConProMiradiCodeMapHelper.reverseMap(map);
		assertEquals("wrong value?", KEY_1, reversedMap.get(VALUE_1));
		assertEquals("wrong value?", KEY_2, reversedMap.get(VALUE_2));
	}
}
