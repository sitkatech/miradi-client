/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeDirectThreat;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
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
			ConceptualModelFactor node = (ConceptualModelFactor)ConceptualModelNode.createConceptualModelObject(idAssigner.takeNextId(), NodeTypeDirectThreat.FACTOR_TYPE);
			node.increaseTargetCount();
			pool.put(node);
		}
		for(int i = 0; i < 3; ++i)
			addNodeToPool(new NodeTypeFactor());
		for(int i = 0; i < 4; ++i)
			addNodeToPool(new NodeTypeTarget());
	}
	
	private void addNodeToPool(NodeType type)
	{
		ConceptualModelNode node = ConceptualModelNode.createConceptualModelObject(idAssigner.takeNextId(), type.toString());
		pool.put(node);
	}

	public void testBasics() throws Exception
	{
		assertEquals("wrong direct threat count?", 2, pool.getDirectThreats().length);
		assertEquals("wrong target count?", 4, pool.getTargets().length);
	}
	
	IdAssigner idAssigner;
	NodePool pool;
}
