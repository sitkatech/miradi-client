/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.datanet;

import org.conservationmeasures.eam.datanet.RecordType;
import org.martus.util.TestCaseEnhanced;

public class TestRecordType extends TestCaseEnhanced
{
	public TestRecordType(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		String RECORD_TYPE_NAME = "Factor";
		RecordType rt = new RecordType(RECORD_TYPE_NAME);
		assertEquals(RECORD_TYPE_NAME, rt.getName());
		
		String LABEL_FIELD_NAME = "Label";
		rt.addField(LABEL_FIELD_NAME, RecordType.STRING);
		rt.addField("Id", RecordType.SHORT_STRING);
		assertEquals(2, rt.getFieldCount());
		assertEquals(RecordType.STRING, rt.getFieldType(LABEL_FIELD_NAME));
	}
	
	public void testDuplicateField() throws Exception
	{
		RecordType rt = new RecordType("x");
		rt.addField("a", RecordType.STRING);
		try
		{
			rt.addField("a", RecordType.SHORT_STRING);
			fail("Should have throw for duplicate field");
		}
		catch(RecordType.FieldAlreadyExistsException ignoreExpected)
		{
			
		}
	}
	
	public void testFieldTypes() throws Exception
	{
		RecordType rt = new RecordType("x");
		try
		{
			rt.addField("unknown", "blah blah");
			fail("Should have thrown for bad field type");
		}
		catch(RecordType.UnknownFieldTypeException ignoreExpected)
		{
			
		}
		rt.addField("string", RecordType.STRING);
		rt.addField("shortstring", RecordType.SHORT_STRING);
		rt.addField("longstring", RecordType.LONG_STRING);
		rt.addField("integer", RecordType.INTEGER);
		rt.addField("float", RecordType.FLOAT);
		rt.addField("date", RecordType.DATE);
	}
	
}
