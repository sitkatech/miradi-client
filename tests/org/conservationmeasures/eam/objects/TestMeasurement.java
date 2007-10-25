package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestMeasurement extends ObjectTestCase
{
	public TestMeasurement(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.MEASUREMENT);
	}
}
