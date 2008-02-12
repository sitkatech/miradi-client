/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.objects;

import org.miradi.objecthelpers.ObjectType;

public class TestCause extends ObjectTestCase
{
	public TestCause(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.CAUSE);
	}
}
