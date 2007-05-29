/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.main.EAMTestCase;

public class MapListTest  extends EAMTestCase
{
	public MapListTest(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		MapList map = new MapList();
		String value = "xxxxxxxxxxxxxxx"; 
		map.add("TAG1",value);
		assertEquals("Simple get/add failed?", value, map.get("TAG1"));
		map.add("TAG2",value);
		assertEquals("Simple get/add failed?", value, map.get("TAG2"));
	}
}
