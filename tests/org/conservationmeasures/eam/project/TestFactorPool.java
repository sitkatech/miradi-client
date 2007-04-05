/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.martus.util.TestCaseEnhanced;

//FIXME this test has to be split into three tests, one for TargetPool/CausePool/StrategyPool
public class TestFactorPool extends TestCaseEnhanced
{
	public TestFactorPool(String name)
	{
		super(name);
	}
//	
//	public void setUp() throws Exception
//	{
//		super.setUp();
//		idAssigner = new IdAssigner();
//		pool = new FactorPool(idAssigner);
//
//		for(int i = 0; i < 2; ++i)
//		{
//			FactorId id = takeNextModelNodeId();
//			CreateFactorParameter parameter = new CreateFactorParameter(new FactorTypeCause());
//			Cause node = (Cause)Factor.
//					createConceptualModelObject(id, parameter);
//			node.increaseTargetCount();
//			pool.put(node);
//		}
//		for(int i = 0; i < 3; ++i)
//			addNodeToPool(new FactorTypeCause());
//		for(int i = 0; i < 4; ++i)
//			addNodeToPool(new FactorTypeTarget());
//	}
//	
	public void testBasics() throws Exception
	{
//		assertEquals("wrong direct threat count?", 2, pool.getDirectThreats().length);
//		assertEquals("wrong target count?", 4, pool.getTargets().length);
	}
//	
//	public void testGetStrategies() throws Exception
//	{
//		//FIXME this test has to be split into three tests, one for TargetPool/CausePool/StrategyPool
//		FactorId draftId = addNodeToPool(Factor.TYPE_STRATEGY);
//		FactorId nonDraftId = addNodeToPool(Factor.TYPE_STRATEGY);
//		Factor draft = pool.find(draftId);
//		draft.setData(Strategy.TAG_STATUS, Strategy.STATUS_DRAFT);
//		
//		Factor[] all = pool.getDraftAndNonDraftStrategies();
//		assertEquals("Wrong all count?", 2, all.length);
//		Vector allIds = new Vector();
//		allIds.add(all[0].getId());
//		allIds.add(all[1].getId());
//		assertContains("Missing draft?", draftId, allIds);
//		assertContains("Missing nonDraft?", nonDraftId, allIds);
//		
//		Factor[] drafts = pool.getDraftStrategies();
//		assertEquals("Not one draft?", 1, drafts.length);
//		assertEquals("Wrong draft?", draftId, drafts[0].getId());
//
//		Factor[] nonDrafts = pool.getNonDraftStrategies();
//		assertEquals("Not one non-draft?", 1, nonDrafts.length);
//		assertEquals("Wrong non-draft?", nonDraftId, nonDrafts[0].getId());
//		
//	}
//	
//	private FactorId addNodeToPool(FactorType type)
//	{
//		FactorId id = takeNextModelNodeId();
//		CreateFactorParameter parameter = new CreateFactorParameter(type);
//		Factor node = Factor.createConceptualModelObject(id, parameter);
//		pool.put(node);
//		return id;
//	}
//
//	private FactorId takeNextModelNodeId()
//	{
//		return new FactorId(idAssigner.takeNextId().asInt());
//	}
//	
//	IdAssigner idAssigner;
//	FactorPool pool;
}
