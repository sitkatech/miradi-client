/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;

import org.martus.util.TestCaseEnhanced;

public class TestRecordInstance extends TestCaseEnhanced
{
	public TestRecordInstance(String name)
	{
		super(name);
	}

	public void testGetKey() throws Exception
	{
		RecordType rt = new RecordType("x");
		RecordInstance first = rt.create(1);
		RecordInstance second = rt.create(2);
		
		assertContains(rt.getName(), first.getKey());
		assertNotEquals(first.getKey(), second.getKey());
	}
	
	public void testSetGet() throws Exception
	{
		String FIRST = "first";
		String SECOND = "second";

		RecordType rt = new RecordType("x");
		rt.addField(FIRST, RecordType.STRING);
		rt.addField(SECOND, RecordType.STRING);
		RecordInstance ri = rt.create(3);
		
		try
		{
			ri.getFieldData("unknown");
			fail("Should have thrown for unknown field");
		}
		catch(RecordInstance.UnknownFieldException ignoreExpected)
		{
			
		}
		assertEquals("", ri.getFieldData(FIRST));
		assertEquals("", ri.getFieldData(SECOND));
		
		try
		{
			ri.setFieldData("unknown", "whatever");
			fail("Should have thrown for unknown field");
		}
		catch(RecordInstance.UnknownFieldException ignoreExpected)
		{
			
		}
		String SAMPLE_DATA = "Sample date";
		ri.setFieldData(FIRST, SAMPLE_DATA);
		assertEquals(SAMPLE_DATA, ri.getFieldData(FIRST));
	}
	
	public void testEquals() throws Exception
	{
		String LABEL = "Label";
		
		RecordType rt = new RecordType("x");
		rt.addField(LABEL, RecordType.STRING);
		RecordInstance first = rt.create(1);
		RecordInstance second = rt.create(2);
		assertNotEquals("Different record instances were equal?", first, second);

		RecordInstance dupe = rt.create(2);
		dupe.setFieldData(LABEL, "blah");
		assertEquals("Same key not equal?", second, dupe);
		assertEquals("Same key different hashCode?", second.hashCode(), dupe.hashCode());

		RecordType rt2 = new RecordType("y");
		RecordInstance other = rt2.create(2);
		assertNotEquals("Same id different type were equal?", second, other);
	}
}
