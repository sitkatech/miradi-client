/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCause;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objects.ConceptualModelCause;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.martus.util.TestCaseEnhanced;

public class TestNodePool extends TestCaseEnhanced
{
	public TestNodePool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		idAssigner = new IdAssigner();
		pool = new NodePool();

		for(int i = 0; i < 2; ++i)
		{
			ModelNodeId id = takeNextModelNodeId();
			CreateModelNodeParameter parameter = new CreateModelNodeParameter(new NodeTypeCause());
			ConceptualModelCause node = (ConceptualModelCause)ConceptualModelNode.
					createConceptualModelObject(id, parameter);
			node.increaseTargetCount();
			pool.put(node);
		}
		for(int i = 0; i < 3; ++i)
			addNodeToPool(new NodeTypeCause());
		for(int i = 0; i < 4; ++i)
			addNodeToPool(new NodeTypeTarget());
	}
	
	private void addNodeToPool(NodeType type)
	{
		ModelNodeId id = takeNextModelNodeId();
		CreateModelNodeParameter parameter = new CreateModelNodeParameter(type);
		ConceptualModelNode node = ConceptualModelNode.createConceptualModelObject(id, parameter);
		pool.put(node);
	}

	public void testBasics() throws Exception
	{
		assertEquals("wrong direct threat count?", 2, pool.getDirectThreats().length);
		assertEquals("wrong target count?", 4, pool.getTargets().length);
	}
	
	private ModelNodeId takeNextModelNodeId()
	{
		return new ModelNodeId(idAssigner.takeNextId().asInt());
	}
	
	IdAssigner idAssigner;
	NodePool pool;
}
