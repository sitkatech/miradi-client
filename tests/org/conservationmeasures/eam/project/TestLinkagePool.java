/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objectpools.LinkagePool;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.martus.util.TestCaseEnhanced;

public class TestLinkagePool extends TestCaseEnhanced
{
	public TestLinkagePool(String name)
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
		LinkagePool pool = new LinkagePool(new LinkageMonitor());
		CreateFactorParameter parameter = new CreateFactorParameter(new FactorTypeCause());
		Factor node1 = Factor.createConceptualModelObject(takeNextModelNodeId(), parameter);
		Factor node2 = Factor.createConceptualModelObject(takeNextModelNodeId(), parameter);
		Factor node3 = Factor.createConceptualModelObject(takeNextModelNodeId(), parameter);
		
		FactorLinkId linkageId = new FactorLinkId(idAssigner.takeNextId().asInt());
		FactorLink linkage = new FactorLink(linkageId, node1.getModelNodeId(), node2.getModelNodeId());
		pool.put(linkage);
		
		assertTrue("Didn't find link 1->2?", pool.hasLinkage(linkage.getFromNodeId(), linkage.getToNodeId()));
		assertTrue("Didn't find link 2->1?", pool.hasLinkage(linkage.getToNodeId(), linkage.getFromNodeId()));
		assertFalse("Found link 1->3?", pool.hasLinkage((FactorId)node1.getId(), (FactorId)node3.getId()));
	}
	
	class LinkageMonitor implements LinkageListener
	{
		public void linkageWasCreated(FactorId linkFromId, FactorId linkToId)
		{
		}

		public void linkageWasDeleted(FactorId linkFromId, FactorId linkToId)
		{
		}		
	}
	
	private FactorId takeNextModelNodeId()
	{
		return new FactorId(idAssigner.takeNextId().asInt());
	}
	

	IdAssigner idAssigner;
}
