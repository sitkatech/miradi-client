/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
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
			FactorId id = takeNextModelNodeId();
			CreateModelNodeParameter parameter = new CreateModelNodeParameter(new FactorTypeCause());
			Cause node = (Cause)Factor.
					createConceptualModelObject(id, parameter);
			node.increaseTargetCount();
			pool.put(node);
		}
		for(int i = 0; i < 3; ++i)
			addNodeToPool(new FactorTypeCause());
		for(int i = 0; i < 4; ++i)
			addNodeToPool(new FactorTypeTarget());
	}
	
	private void addNodeToPool(FactorType type)
	{
		FactorId id = takeNextModelNodeId();
		CreateModelNodeParameter parameter = new CreateModelNodeParameter(type);
		Factor node = Factor.createConceptualModelObject(id, parameter);
		pool.put(node);
	}

	public void testBasics() throws Exception
	{
		assertEquals("wrong direct threat count?", 2, pool.getDirectThreats().length);
		assertEquals("wrong target count?", 4, pool.getTargets().length);
	}
	
	private FactorId takeNextModelNodeId()
	{
		return new FactorId(idAssigner.takeNextId().asInt());
	}
	
	IdAssigner idAssigner;
	NodePool pool;
}
