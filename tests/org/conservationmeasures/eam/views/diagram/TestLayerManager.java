/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramFactor;
import org.conservationmeasures.eam.diagram.nodes.DiagramIntervention;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.DiagramTarget;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
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
		IdAssigner idAssigner = new IdAssigner();
		cmTarget = new ConceptualModelTarget(idAssigner.takeNextId());
		cmTarget.setLabel("Target");
		cmFactor = new ConceptualModelFactor(idAssigner.takeNextId(), DiagramNode.TYPE_INDIRECT_FACTOR);
		cmFactor.setLabel("Factor");
		cmIntervention = new ConceptualModelIntervention(idAssigner.takeNextId());
		cmIntervention.setLabel("Intervention");
		
		target = new DiagramTarget(cmTarget);
		factor = new DiagramFactor(cmFactor);
		intervention = new DiagramIntervention(cmIntervention);
	}

	public void testDefaultAllVisible() throws Exception
	{
		LayerManager manager = new LayerManager();
		verifyVisibility("default visible", true, intervention, manager);
		verifyVisibility("default visible", true, factor, manager);
		verifyVisibility("default visible", true, target, manager);
		
		assertTrue("All layers not visible by default?", manager.areAllNodesVisible());
	}
	
	public void testHide() throws Exception
	{
		LayerManager manager = new LayerManager();
		manager.setVisibility(DiagramIntervention.class, false);
		verifyVisibility("hidden type", false, new DiagramIntervention(cmIntervention), manager);
		verifyVisibility("non-hidden type", true, new DiagramTarget(cmTarget), manager);
		assertFalse("All layers still visible?", manager.areAllNodesVisible());
		
		manager.setVisibility(DiagramIntervention.class, true);
		verifyVisibility("unhidden type", true, new DiagramTarget(cmTarget), manager);
		assertTrue("All layers not visible again?", manager.areAllNodesVisible());
	}
	
	public void testHideIds() throws Exception
	{
		LayerManager manager = new LayerManager();
		assertTrue("all nodes not visible to start?", manager.areAllNodesVisible());
		IdList idsToHide = new IdList();
		idsToHide.add(target.getId());
		idsToHide.add(factor.getId());
		manager.setHiddenIds(idsToHide);
		assertFalse("thinks all nodes are visible?", manager.areAllNodesVisible());
		for(int i = 0; i < idsToHide.size(); ++i)
		{
			verifyNodeVisibility("hide ids", false, target, manager);
			verifyNodeVisibility("hide ids", false, factor, manager);
			verifyNodeVisibility("hide ids", true, intervention, manager);
		}
	}

	private void verifyVisibility(String text, boolean expected, DiagramNode node, LayerManager manager)
	{
		assertEquals("type: " + text + " (" + node + ") ",expected, manager.isTypeVisible(node.getClass()));
		verifyNodeVisibility(text, expected, node, manager);
	}

	private void verifyNodeVisibility(String text, boolean expected, DiagramNode node, LayerManager manager)
	{
		assertEquals("node: " + text + " (" + node.getLabel() + ") ",expected, manager.isVisible(node));
	}

	ConceptualModelTarget cmTarget;
	ConceptualModelFactor cmFactor;
	ConceptualModelIntervention cmIntervention;
	
	DiagramNode target;
	DiagramNode factor;
	DiagramNode intervention;
}
