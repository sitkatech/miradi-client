/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.project;

import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.StrategyPool;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;


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
