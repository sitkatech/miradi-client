package org.miradi.objects;

import org.miradi.objecthelpers.ObjectType;

public class TestGoal extends ObjectTestCase
{
	public TestGoal(String name)
	{
		super(name);
	}

	public void testFields() throws Exception
	{
		verifyFields(ObjectType.GOAL);
	}
	
}
