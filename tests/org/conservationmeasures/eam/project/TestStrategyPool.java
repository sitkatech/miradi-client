/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.StrategyPool;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;


public class TestStrategyPool extends TestFactorPool
{
	public TestStrategyPool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		pool = project.getStrategyPool();
	}
	
	public int getObjectType()
	{
		return ObjectType.STRATEGY;
	}
	
	public void testBasics() throws Exception
	{
		super.testBasics();
		assertEquals("wrong draft strategy count", 0, pool.getDraftStrategies().length);
	}
	
	public void testDraftStrategies() throws Exception
	{
		FactorId draftId = addNewlyCreatedNodeToPool(ObjectType.STRATEGY);
		FactorId nonDraftId = addNewlyCreatedNodeToPool(ObjectType.STRATEGY);
		Strategy draft = pool.find(draftId);
		Strategy nonDraft = pool.find(nonDraftId);
		draft.setData(Strategy.TAG_STATUS, Strategy.STATUS_DRAFT);
		
		Factor[] all = pool.getDraftAndNonDraftStrategies();
		assertEquals("Wrong all count?", 5, all.length);
		
		Factor[] draftStrategies = pool.getDraftStrategies();
		assertContains("Missing draft?", draft, draftStrategies);
		assertEquals("Not one draft?", 1, draftStrategies.length);
		assertEquals("Wrong draft?", draftId, draftStrategies[0].getId());

		Factor[] nonDrafts = pool.getNonDraftStrategies();
		assertEquals("Not one non-draft?", 4, nonDrafts.length);
		assertContains("didnt find non-draft strategy?", nonDraft, nonDrafts);
	}
	
	StrategyPool pool;
}
