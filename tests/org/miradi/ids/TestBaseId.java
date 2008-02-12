/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.ids;

import org.miradi.ids.BaseId;
import org.miradi.main.EAMTestCase;

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
