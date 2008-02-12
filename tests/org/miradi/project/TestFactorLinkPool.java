/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.project;

import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdAssigner;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.FactorLinkPool;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.project.FactorLinkListener;

public class TestFactorLinkPool extends TestCaseWithProject
{
	public TestFactorLinkPool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		idAssigner = new IdAssigner();
	}

	public void testHasLinkage() throws Exception
	{
		FactorLinkPool pool = new FactorLinkPool(idAssigner);
		Factor node1 = Factor.createConceptualModelObject(getObjectManager(), takeNextModelNodeId(), ObjectType.CAUSE);
		Factor node2 = Factor.createConceptualModelObject(getObjectManager(), takeNextModelNodeId(), ObjectType.CAUSE);
		Factor node3 = Factor.createConceptualModelObject(getObjectManager(), takeNextModelNodeId(), ObjectType.CAUSE);
		
		FactorLinkId linkageId = new FactorLinkId(idAssigner.takeNextId().asInt());
		FactorLink linkage = new FactorLink(getObjectManager(), linkageId, node1.getRef(), node2.getRef());
		pool.put(linkage);
		
		assertTrue("Didn't find link 1->2?", pool.isLinked(linkage.getFromFactorRef(), linkage.getToFactorRef()));
		assertTrue("Didn't find link 2->1?", pool.isLinked(linkage.getToFactorRef(), linkage.getFromFactorRef()));
		assertFalse("Found link 1->3?", pool.isLinked(node1.getRef(), node3.getRef()));
	}
	
	public void testGetDirectThreatTargetLinks() throws Exception
	{
		assertEquals("links found?", 0, getProject().getFactorLinkPool().getDirectThreatTargetLinks().size());
		
		getProject().createThreatTargetLink();
		assertEquals("wrong link count?", 1, getProject().getFactorLinkPool().getDirectThreatTargetLinks().size());
		
		getProject().creatThreatTargetBidirectionalLink();
		assertEquals("wrong link count?", 2, getProject().getFactorLinkPool().getDirectThreatTargetLinks().size());
	}
	
	class LinkageMonitor implements FactorLinkListener
	{
		public void factorLinkWasCreated(FactorId linkFromId, FactorId linkToId)
		{
		}

		public void factorLinkWasDeleted(FactorId linkFromId, FactorId linkToId)
		{
		}		
	}
	
	private FactorId takeNextModelNodeId()
	{
		return new FactorId(idAssigner.takeNextId().asInt());
	}
	

	private IdAssigner idAssigner;
}
