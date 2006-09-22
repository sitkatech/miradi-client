package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestGoal extends ObjectTestCase
{
	public TestGoal(String name)
	{
		super(name);
	}

	public void testData() throws Exception
	{
		verifyTextField(ObjectType.GOAL, Goal.TAG_SHORT_LABEL);
		verifyTextField(ObjectType.GOAL, Goal.TAG_FULL_TEXT);
	}

}
