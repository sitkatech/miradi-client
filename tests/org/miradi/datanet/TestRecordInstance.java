/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.datanet;


public class TestRecordInstance extends TestCaseWithSampleDatanet
{
	public TestRecordInstance(String name)
	{
		super(name);
	}

	public void testGetKey() throws Exception
	{
		RecordInstance first = createOwnerRecord();
		RecordInstance second = createOwnerRecord();
		assertNotEquals("Different keys equal?", first.getKey(), second.getKey());
		
		RecordInstance dupe1 = new RecordInstance(datanet, datanet.getRecordType(SampleDatanetSchema.OWNER), 100);
		RecordInstance dupe2 = new RecordInstance(datanet, datanet.getRecordType(SampleDatanetSchema.OWNER), 100);
		assertEquals("Identical keys not equal?", dupe1, dupe2);
		
		RecordInstance sameId1 = new RecordInstance(datanet, datanet.getRecordType(SampleDatanetSchema.OWNER), 100);
		RecordInstance sameId2 = new RecordInstance(datanet, datanet.getRecordType(SampleDatanetSchema.MEMBER), 100);
		assertNotEquals("Different types same id equal?", sameId1.getKey(), sameId2.getKey());
	}
	
	public void testSetGet() throws Exception
	{
		try
		{
			owner.getFieldData("unknown");
			fail("Should have thrown for unknown field");
		}
		catch(RecordInstance.UnknownFieldException ignoreExpected)
		{
			
		}
		assertEquals("", owner.getFieldData(SampleDatanetSchema.LABEL));
		assertEquals("", owner.getFieldData(SampleDatanetSchema.COMMENTS));
		
		try
		{
			owner.setFieldData("unknown", "whatever");
			fail("Should have thrown for unknown field");
		}
		catch(RecordInstance.UnknownFieldException ignoreExpected)
		{
			
		}
		String SAMPLE_DATA = "Sample date";
		owner.setFieldData(SampleDatanetSchema.LABEL, SAMPLE_DATA);
		assertEquals(SAMPLE_DATA, owner.getFieldData(SampleDatanetSchema.LABEL));
	}
	
	public void testEquals() throws Exception
	{
		String LABEL = "Label";
		
		RecordInstance owner2 = createOwnerRecord();
		assertNotEquals("Different record instances were equal?", owner, owner2);
		
		int ID = 100;
		RecordInstance similar1 = new RecordInstance(datanet, owner.getType(), ID);
		RecordInstance similar2 = new RecordInstance(datanet, owner.getType(), ID);
		similar1.setFieldData(LABEL, "blah");
		assertEquals("Same key not equal?", similar1, similar2);
		assertEquals("Same key different hashCode?", similar1.hashCode(), similar2.hashCode());

		RecordInstance different = new RecordInstance(datanet, member.getType(), ID);
		assertNotEquals("Same id different type were equal?", similar1, different);
	}
}
