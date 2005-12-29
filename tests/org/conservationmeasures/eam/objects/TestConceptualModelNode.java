/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.martus.util.TestCaseEnhanced;

public class TestConceptualModelNode extends TestCaseEnhanced
{
	public TestConceptualModelNode(String name)
	{
		super(name);
	}

	public void testJson()
	{
		ConceptualModelFactor factor = new ConceptualModelFactor(DiagramNode.TYPE_DIRECT_THREAT);
		factor.setNodePriority(ThreatPriority.createPriorityHigh());
		ConceptualModelFactor got = (ConceptualModelFactor)ConceptualModelNode.createFrom(factor.toJson());
		assertEquals("wrong type?", factor.getType(), got.getType());
		assertEquals("wrong id?", factor.getId(), got.getId());
		assertEquals("wrong priority?", factor.getThreatPriority(), got.getThreatPriority());
	}
}
