/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestConceptualModelFactor extends ObjectTestCase
{
	public TestConceptualModelFactor(String name)
	{
		super(name);
	}
	
	public void testExtraInfo() throws Exception
	{
		ModelNodeId idToCreate = new ModelNodeId(17);
		CreateModelNodeParameter extraInfo = new CreateModelNodeParameter(DiagramNode.TYPE_FACTOR);
		ConceptualModelNode node = ConceptualModelNode.createConceptualModelObject(idToCreate, extraInfo);
		CreateModelNodeParameter gotExtraInfo = (CreateModelNodeParameter)node.getCreationExtraInfo();
		assertEquals(extraInfo.getNodeType(), gotExtraInfo.getNodeType());
	}

	public void testData() throws Exception
	{
		CreateModelNodeParameter extraInfo = new CreateModelNodeParameter(new NodeTypeFactor());
		verifyTextFieldInModelNode(type, ConceptualModelFactor.TAG_TAXONOMY_CODE, extraInfo);
	}
	
	private static final int type = ObjectType.MODEL_NODE;

}
