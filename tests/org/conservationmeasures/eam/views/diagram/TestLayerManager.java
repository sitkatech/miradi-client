/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.ConceptualModelFactor;
import org.conservationmeasures.eam.diagram.nodes.ConceptualModelIntervention;
import org.conservationmeasures.eam.diagram.nodes.ConceptualModelTarget;
import org.conservationmeasures.eam.diagram.nodes.DiagramFactor;
import org.conservationmeasures.eam.diagram.nodes.DiagramIntervention;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.DiagramTarget;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestLayerManager extends EAMTestCase
{
	public TestLayerManager(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		cmTarget = new ConceptualModelTarget();
		cmFactor = new ConceptualModelFactor(DiagramNode.TYPE_INDIRECT_FACTOR);
		cmIntervention = new ConceptualModelIntervention();
	}

	public void testDefaultAllVisible() throws Exception
	{
		LayerManager manager = new LayerManager();
		verifyVisibility("default visible", true, new DiagramIntervention(cmIntervention), manager);
		verifyVisibility("default visible", true, new DiagramFactor(cmFactor), manager);
		verifyVisibility("default visible", true, new DiagramTarget(cmTarget), manager);
		
		assertTrue("All layers not visible by default?", manager.areAllLayersVisible());
	}
	
	public void testHide() throws Exception
	{
		LayerManager manager = new LayerManager();
		manager.setVisibility(DiagramIntervention.class, false);
		verifyVisibility("hidden type", false, new DiagramIntervention(cmIntervention), manager);
		verifyVisibility("non-hidden type", true, new DiagramTarget(cmTarget), manager);
		assertFalse("All layers still visible?", manager.areAllLayersVisible());
		
		manager.setVisibility(DiagramIntervention.class, true);
		verifyVisibility("unhidden type", true, new DiagramTarget(cmTarget), manager);
		assertTrue("All layers not visible again?", manager.areAllLayersVisible());
	}

	private void verifyVisibility(String text, boolean expected, DiagramNode node, LayerManager manager)
	{
		assertEquals("type: " + text + " (" + node + ") ",expected, manager.isTypeVisible(node.getClass()));
		assertEquals("node: " + text + " (" + node + ") ",expected, manager.isVisible(node));
	}

	ConceptualModelTarget cmTarget;
	ConceptualModelFactor cmFactor;
	ConceptualModelIntervention cmIntervention;
}
