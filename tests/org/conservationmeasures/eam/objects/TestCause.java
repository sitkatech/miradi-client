/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

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
	
	//TODO rename method.  Factors no longer have extraInfo (CreateFactorParameter)
	public void testExtraInfo() throws Exception
	{
		FactorId idToCreate = new FactorId(17);
		Factor node = Factor.createConceptualModelObject(idToCreate, ObjectType.CAUSE);
		assertEquals(ObjectType.CAUSE, node.getType());
	}
}
