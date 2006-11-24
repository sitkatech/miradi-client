/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestConceptualModelCause extends ObjectTestCase
{
	public TestConceptualModelCause(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		CreateModelNodeParameter extraInfo = new CreateModelNodeParameter(ConceptualModelNode.TYPE_CAUSE);
		verifyFields(ObjectType.MODEL_NODE, extraInfo);
	}
	
	public void testExtraInfo() throws Exception
	{
		ModelNodeId idToCreate = new ModelNodeId(17);
		CreateModelNodeParameter extraInfo = new CreateModelNodeParameter(ConceptualModelNode.TYPE_CAUSE);
		ConceptualModelNode node = ConceptualModelNode.createConceptualModelObject(idToCreate, extraInfo);
		CreateModelNodeParameter gotExtraInfo = (CreateModelNodeParameter)node.getCreationExtraInfo();
		assertEquals(extraInfo.getNodeType(), gotExtraInfo.getNodeType());
	}
}
