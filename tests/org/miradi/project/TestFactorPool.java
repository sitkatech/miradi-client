/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.project;

import org.martus.util.TestCaseEnhanced;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objects.Factor;

public class TestFactorPool extends TestCaseEnhanced
{
	public TestFactorPool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		idAssigner = new IdAssigner();
		project = new ProjectForTesting(getName());
		
		for (int i = 0; i < FACTOR_COUNT; ++i)
		{
			addNewlyCreatedNodeToPool(getObjectType());
		}
	}	
	
	protected void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}

	public int getObjectType() throws Exception
	{
		throw new Exception();
	}
	
	public void testBasics() throws Exception
	{
		assertEquals("wrong target count?", FACTOR_COUNT, project.getPool(getObjectType()).getIds().length);
	}

	protected FactorId addNewlyCreatedNodeToPool(int type)
	{
		FactorId id = takeNextModelNodeId();		
		Factor node = Factor.createConceptualModelObject(project.getObjectManager(), id, type);
		project.getPool(type).put(node.getId(), node);
		
		return id;
	}

	private FactorId takeNextModelNodeId()
	{
		return new FactorId(idAssigner.takeNextId().asInt());
	}
	
	IdAssigner idAssigner;
	ProjectForTesting project;
	
	protected static final int FACTOR_COUNT = 3;
}
