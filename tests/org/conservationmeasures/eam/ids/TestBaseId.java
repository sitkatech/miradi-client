/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.ids;

import org.conservationmeasures.eam.main.EAMTestCase;

public class TestBaseId extends EAMTestCase
{
	public TestBaseId(String name)
	{
		super(name);
	}
	
	public void testBaseId()
	{
		assertEquals("wrong id?", BaseId.INVALID, new BaseId(""));
	}
}
