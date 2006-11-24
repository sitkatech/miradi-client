/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestCause extends ObjectTestCase
{
	public TestCause(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		CreateModelNodeParameter extraInfo = new CreateModelNodeParameter(Factor.TYPE_CAUSE);
		verifyFields(ObjectType.MODEL_NODE, extraInfo);
	}
	
	public void testExtraInfo() throws Exception
	{
		ModelNodeId idToCreate = new ModelNodeId(17);
		CreateModelNodeParameter extraInfo = new CreateModelNodeParameter(Factor.TYPE_CAUSE);
		Factor node = Factor.createConceptualModelObject(idToCreate, extraInfo);
		CreateModelNodeParameter gotExtraInfo = (CreateModelNodeParameter)node.getCreationExtraInfo();
		assertEquals(extraInfo.getNodeType(), gotExtraInfo.getNodeType());
	}
}
