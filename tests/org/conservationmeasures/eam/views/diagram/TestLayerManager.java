/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramCause;
import org.conservationmeasures.eam.diagram.nodes.DiagramStrategy;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.DiagramTarget;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
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
		idAssigner = new IdAssigner();
		cmTarget = new Target(takeNextModelNodeId());
		cmTarget.setLabel("Target");
		cmFactor = new Cause(takeNextModelNodeId());
		cmFactor.setLabel("Factor");
		cmIntervention = new Strategy(takeNextModelNodeId());
		cmIntervention.setLabel("Strategy");
		
		DiagramNodeId targetNodeId = new DiagramNodeId(44);
		target = new DiagramTarget(targetNodeId, cmTarget);
		DiagramNodeId factorNodeId = new DiagramNodeId(48);
		factor = new DiagramCause(factorNodeId, cmFactor);
		DiagramNodeId interventionNodeId = new DiagramNodeId(99);
		intervention = new DiagramStrategy(interventionNodeId, cmIntervention);
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
		DiagramNodeId nodeId1 = new DiagramNodeId(44);
		DiagramNodeId nodeId2 = new DiagramNodeId(77);
		LayerManager manager = new LayerManager();
		manager.setVisibility(DiagramStrategy.class, false);
		verifyVisibility("hidden type", false, new DiagramStrategy(nodeId1, cmIntervention), manager);
		verifyVisibility("non-hidden type", true, new DiagramTarget(nodeId2, cmTarget), manager);
		assertFalse("All layers still visible?", manager.areAllNodesVisible());
		
		DiagramNodeId nodeId3 = new DiagramNodeId(99);
		manager.setVisibility(DiagramStrategy.class, true);
		verifyVisibility("unhidden type", true, new DiagramTarget(nodeId3, cmTarget), manager);
		assertTrue("All layers not visible again?", manager.areAllNodesVisible());
	}
	
	public void testHideIds() throws Exception
	{
		LayerManager manager = new LayerManager();
		assertTrue("all nodes not visible to start?", manager.areAllNodesVisible());
		IdList idsToHide = new IdList();
		idsToHide.add(target.getDiagramNodeId());
		idsToHide.add(factor.getDiagramNodeId());
		manager.setHiddenIds(idsToHide);
		assertFalse("thinks all nodes are visible?", manager.areAllNodesVisible());
		for(int i = 0; i < idsToHide.size(); ++i)
		{
			verifyNodeVisibility("hide ids", false, target, manager);
			verifyNodeVisibility("hide ids", false, factor, manager);
			verifyNodeVisibility("hide ids", true, intervention, manager);
		}
	}
	
	public void testDesires() throws Exception
	{
		LayerManager manager = new LayerManager();
		assertTrue("Desires not visible by default?", manager.areDesiresVisible());
		manager.setDesiresVisible(false);
		assertFalse("Didn't set invisible?", manager.areDesiresVisible());
		manager.setDesiresVisible(true);
		assertTrue("Didn't set visible?", manager.areDesiresVisible());
	}

	public void testIndicators() throws Exception
	{
		LayerManager manager = new LayerManager();
		assertTrue("Indicators not visible by default?", manager.areIndicatorsVisible());
		manager.setIndicatorsVisible(false);
		assertFalse("Didn't set invisible?", manager.areIndicatorsVisible());
		manager.setIndicatorsVisible(true);
		assertTrue("Didn't set visible?", manager.areIndicatorsVisible());
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
	
	private ModelNodeId takeNextModelNodeId()
	{
		return new ModelNodeId(idAssigner.takeNextId().asInt());
	}
	
	IdAssigner idAssigner;

	Target cmTarget;
	Cause cmFactor;
	Strategy cmIntervention;
	
	DiagramNode target;
	DiagramNode factor;
	DiagramNode intervention;
}
