/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeDirectThreat;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.martus.util.TestCaseEnhanced;

public class TestThreatMatrixModel extends TestCaseEnhanced
{
	public TestThreatMatrixModel(String name)
	{
		super(name);
	}
	
	protected void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		model = new ThreatMatrixTableModel(project);
	}

	protected void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}

	public void testGetThreatCount() throws Exception
	{
		assertEquals(0, model.getThreatCount());
		
		createThreat("one");
		createThreat("two");
		createThreat("three");
		assertEquals(3, model.getThreatCount());
	}
	
	public void testGetColumnCount() throws Exception
	{
		assertEquals(0, model.getTargetCount());
		createTarget("one");
		createTarget("two");
		createTarget("three");
		assertEquals(3, model.getTargetCount());
	}
	
	public void testGetThreatNames() throws Exception
	{
		String[] threatNames = new String[] {"one", "two"};
		for(int i = 0; i < threatNames.length; ++i)
			createThreat(threatNames[i]);
		
		for(int i = 0; i < threatNames.length; ++i)
		{
			Object thisName = model.getThreatName(i);
			assertEquals("bad threat name " + i + "? ", threatNames[i], thisName);
		}
	}
	
	public void testGetTargetNames() throws Exception
	{
		String targetNames[] = new String[] {"a", "b"};
		for(int i = 0; i < targetNames.length; ++i)
			createTarget(targetNames[i]);
		
		for(int i = 0; i < targetNames.length; ++i)
		{
			Object thisName = model.getTargetName(i);
			assertEquals("bad target name " + i + "? ", targetNames[i], thisName);
		}
	}
	
	public void testIsActiveCell() throws Exception
	{
		BaseId threat1 = createThreat("threat one");
		BaseId threat2 = createThreat("threat two");
		createThreat("threat three");
		BaseId target1 = createTarget("target one");
		BaseId target2 = createTarget("target two");
		createTarget("target three");
		
		project.insertLinkageAtId(new BaseId(), threat1, target1);
		project.insertLinkageAtId(new BaseId(), threat1, target2);
		project.insertLinkageAtId(new BaseId(), threat2, target2);

		assertFalse(model.isActiveCell(-1, -1));
		int row1 = 0;
		int row2 = row1 + 1;
		int row3 = row2 + 1;
		int col1 = 0;
		int col2 = col1 + 1;
		int col3 = col2 + 1;
		assertTrue(model.isActiveCell(row1, col1));
		assertTrue(model.isActiveCell(row1, col2));
		assertFalse(model.isActiveCell(row1, col3));
		assertFalse(model.isActiveCell(row2, col1));
		assertTrue(model.isActiveCell(row2, col2));
		assertFalse(model.isActiveCell(row2, col3));
		assertFalse(model.isActiveCell(row3, col1));
		assertFalse(model.isActiveCell(row3, col2));
		assertFalse(model.isActiveCell(row3, col3));
	}
	
	private BaseId createThreat(String name) throws Exception
	{
		return createNode(new NodeTypeDirectThreat(), name);
	}

	private BaseId createTarget(String name) throws Exception
	{
		return createNode(new NodeTypeTarget(), name);
	}

	private BaseId createNode(NodeType type, String name) throws Exception
	{
		BaseId id = project.insertNodeAtId(type, new BaseId());
		assertNotEquals("didn't fix id?", new BaseId(), id);
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		node.setLabel(name);
		return id;
	}
	
	ProjectForTesting project;
	ThreatMatrixTableModel model;
}
