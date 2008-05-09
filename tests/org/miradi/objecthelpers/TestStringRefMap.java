/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objecthelpers;

import java.util.Iterator;

import org.miradi.ids.BaseId;
import org.miradi.main.EAMTestCase;
import org.miradi.utils.EnhancedJsonObject;

public class TestStringRefMap extends EAMTestCase
{
	public TestStringRefMap(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		StringRefMap stringRefMap = createSampleStringRefMap();
		
		assertEquals("wrong string ref map size?", 1, stringRefMap.size());
		assertEquals("wrong string value for key?", stringRefMap.get(getSampleRef()), STRING_VALUE_SAMPLE_DATA);
		assertTrue("value not contained in string ref map?", stringRefMap.containsValue(STRING_VALUE_SAMPLE_DATA));
	}
	
	public void testEquals()
	{
		StringRefMap stringRefMap = createSampleStringRefMap();
		
		StringRefMap stringRefMap2 = new StringRefMap();
		ORef ref2 = new ORef(3, new BaseId(5));
		String stringValue2 = "second time";
		stringRefMap.add(ref2, stringValue2);
		
		assertFalse("should not be equal?", stringRefMap.equals(stringRefMap2));
		assertTrue("should be equal?", stringRefMap2.equals(stringRefMap2));
	}
	
	public void testToString() throws Exception
	{
		StringRefMap stringRefMap = createSampleStringRefMap();
		assertEquals("Can't rount trip?", stringRefMap, new StringRefMap(stringRefMap));
	}

	public void testJson()
	{
		StringRefMap stringRefMap = createSampleStringRefMap();
		EnhancedJsonObject json = stringRefMap.toJson();
		
		StringRefMap loaded = new StringRefMap(json);
		assertEquals("wrong size?", stringRefMap.size(), loaded.size());
		Iterator iterator = stringRefMap.toHashMap().keySet().iterator();
		while (iterator.hasNext())
		{
			ORef key = (ORef)iterator.next();
			assertEquals("wrong member?", stringRefMap.get(key), loaded.get(key));
		}
	}
	
	public void testFindKey()
	{
		StringRefMap stringRefMap = createSampleStringRefMap();
		assertEquals("wrong key", getSampleRef(), stringRefMap.findKey(STRING_VALUE_SAMPLE_DATA));
		assertEquals("wrong key", ORef.INVALID, stringRefMap.findKey("wrong non existing value"));
	}
	
	private StringRefMap createSampleStringRefMap()
	{
		StringRefMap stringRefMap = new StringRefMap();
		ORef ref = getSampleRef();
		stringRefMap.add(ref, STRING_VALUE_SAMPLE_DATA);
		
		return stringRefMap;
	}

	private ORef getSampleRef()
	{
		return new ORef(2, new BaseId(5));
	}
	
	public static final String STRING_VALUE_SAMPLE_DATA = "first itme";
}
