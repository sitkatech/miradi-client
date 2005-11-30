/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.types.NodeType;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestLayerManager extends EAMTestCase
{
	public TestLayerManager(String name)
	{
		super(name);
	}

	public void testDefaultAllVisible() throws Exception
	{
		LayerManager manager = new LayerManager();
		verifyVisibility("default visible", true, DiagramNode.TYPE_INTERVENTION, manager);
		verifyVisibility("default visible", true, DiagramNode.TYPE_DIRECT_THREAT, manager);
		verifyVisibility("default visible", true, DiagramNode.TYPE_INDIRECT_FACTOR, manager);
		verifyVisibility("default visible", true, DiagramNode.TYPE_STRESS, manager);
		verifyVisibility("default visible", true, DiagramNode.TYPE_TARGET, manager);
		
		assertTrue("All layers not visible by default?", manager.areAllLayersVisible());
	}
	
	public void testHide() throws Exception
	{
		LayerManager manager = new LayerManager();
		manager.setVisibility(DiagramNode.TYPE_INTERVENTION, false);
		verifyVisibility("hidden type", false, DiagramNode.TYPE_INTERVENTION, manager);
		verifyVisibility("non-hidden type", true, DiagramNode.TYPE_STRESS, manager);
		assertFalse("All layers still visible?", manager.areAllLayersVisible());
		
		manager.setVisibility(DiagramNode.TYPE_INTERVENTION, true);
		verifyVisibility("unhidden type", true, DiagramNode.TYPE_INTERVENTION, manager);
		assertTrue("All layers not visible again?", manager.areAllLayersVisible());
	}

	private void verifyVisibility(String text, boolean expected, NodeType type, LayerManager manager)
	{
		assertEquals("type: " + text + " (" + type + ") ",expected, manager.isTypeVisible(type));
		DiagramNode node = DiagramNode.createDiagramNode(type);
		assertEquals("node: " + text + " (" + type + ") ",expected, manager.isVisible(node));
	}
}
