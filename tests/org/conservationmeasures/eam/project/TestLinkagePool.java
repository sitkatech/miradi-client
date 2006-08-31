/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeDirectThreat;
import org.conservationmeasures.eam.objectpools.LinkagePool;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
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
		LinkagePool pool = new LinkagePool();
		ConceptualModelNode node1 = ConceptualModelNode.createConceptualModelObject(idAssigner.takeNextId(), new NodeTypeDirectThreat());
		ConceptualModelNode node2 = ConceptualModelNode.createConceptualModelObject(idAssigner.takeNextId(), new NodeTypeDirectThreat());
		ConceptualModelNode node3 = ConceptualModelNode.createConceptualModelObject(idAssigner.takeNextId(), new NodeTypeDirectThreat());
		
		ConceptualModelLinkage linkage = new ConceptualModelLinkage(idAssigner.takeNextId(), node1.getId(), node2.getId());
		pool.put(linkage);
		
		assertTrue("Didn't find linkage 1->2?", pool.hasLinkage(linkage.getFromNodeId(), linkage.getToNodeId()));
		assertTrue("Didn't find linkage 2->1?", pool.hasLinkage(linkage.getToNodeId(), linkage.getFromNodeId()));
		assertFalse("Found linkage 1->3?", pool.hasLinkage(node1.getId(), node3.getId()));
	}
	
	IdAssigner idAssigner;
}
