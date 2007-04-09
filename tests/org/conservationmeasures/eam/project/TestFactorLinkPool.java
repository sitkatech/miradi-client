/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.martus.util.TestCaseEnhanced;

public class TestFactorLinkPool extends TestCaseEnhanced
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
		FactorLinkPool pool = new FactorLinkPool(idAssigner, new LinkageMonitor());
		Factor node1 = Factor.createConceptualModelObject(takeNextModelNodeId(), ObjectType.CAUSE);
		Factor node2 = Factor.createConceptualModelObject(takeNextModelNodeId(), ObjectType.CAUSE);
		Factor node3 = Factor.createConceptualModelObject(takeNextModelNodeId(), ObjectType.CAUSE);
		
		FactorLinkId linkageId = new FactorLinkId(idAssigner.takeNextId().asInt());
		FactorLink linkage = new FactorLink(linkageId, node1.getFactorId(), node2.getFactorId());
		pool.put(linkage);
		
		assertTrue("Didn't find link 1->2?", pool.isLinked(linkage.getFromFactorId(), linkage.getToFactorId()));
		assertTrue("Didn't find link 2->1?", pool.isLinked(linkage.getToFactorId(), linkage.getFromFactorId()));
		assertFalse("Found link 1->3?", pool.isLinked((FactorId)node1.getId(), (FactorId)node3.getId()));
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
	

	IdAssigner idAssigner;
}
