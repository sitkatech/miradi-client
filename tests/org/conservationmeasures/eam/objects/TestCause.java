/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestCause extends ObjectTestCase
{
	public TestCause(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		CreateFactorParameter extraInfo = new CreateFactorParameter(Factor.TYPE_CAUSE);
		verifyFields(ObjectType.FACTOR, extraInfo);
	}
	
	public void testExtraInfo() throws Exception
	{
		FactorId idToCreate = new FactorId(17);
		CreateFactorParameter extraInfo = new CreateFactorParameter(Factor.TYPE_CAUSE);
		Factor node = Factor.createConceptualModelObject(idToCreate, extraInfo);
		CreateFactorParameter gotExtraInfo = (CreateFactorParameter)node.getCreationExtraInfo();
		assertEquals(extraInfo.getNodeType(), gotExtraInfo.getNodeType());
	}
}
