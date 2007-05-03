/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.CausePool;

public class TestCausePool extends TestFactorPool
{
	public TestCausePool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		pool = project.getCausePool();
		for(int i = 0; i < 3; ++i)
		{
			addNewlyCreatedNodeToPool(ObjectType.CAUSE);
		}
	}
	
	public void testBasics() throws Exception
	{
		assertEquals("wrong Cuase count?", 3, pool.getIds().length);
		assertEquals("wrong direct threat count?", 0, pool.getDirectThreats().length);
	}
	
	public void testDirectThreats() throws Exception
	{
		createThreat();
		assertEquals("wrong direct threat count?", 1, pool.getDirectThreats().length);
	}
	
	private FactorId createThreat() throws Exception
	{
		FactorId threatId = project.createFactor(ObjectType.CAUSE);
		FactorId targetId = project.createFactor(ObjectType.TARGET);
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(threatId, targetId);
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, parameter);
		
		return threatId;
	}

	CausePool pool;
}
