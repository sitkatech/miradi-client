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
import org.conservationmeasures.eam.project.IdAssigner;
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

	public void testGetRowCount() throws Exception
	{
		assertEquals(reservedRows, model.getRowCount());
		
		createThreat("one");
		createThreat("two");
		createThreat("three");
		assertEquals(reservedRows + 3, model.getRowCount());
	}
	
	public void testGetColumnCount() throws Exception
	{
		assertEquals(reservedColumns, model.getColumnCount());
		createTarget("one");
		createTarget("two");
		createTarget("three");
		assertEquals(reservedColumns + 3, model.getColumnCount());
	}
	
	public void testGetThreatNames() throws Exception
	{
		String[] threatNames = new String[] {"one", "two"};
		for(int i = 0; i < threatNames.length; ++i)
			createThreat(threatNames[i]);
		
		for(int i = 0; i < threatNames.length; ++i)
		{
			Object thisName = model.getValueAt(reservedRows + i, 0);
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
			Object thisName = model.getValueAt(0, reservedColumns + i);
			assertEquals("bad target name " + i + "? ", targetNames[i], thisName);
		}
	}
	
	private void createThreat(String name) throws Exception
	{
		createNode(new NodeTypeDirectThreat(), name);
	}

	private void createTarget(String name) throws Exception
	{
		createNode(new NodeTypeTarget(), name);
	}

	private void createNode(NodeType type, String name) throws Exception
	{
		int id = project.insertNodeAtId(type, IdAssigner.INVALID_ID);
		assertNotEquals("didn't fix id?", -1, id);
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		node.setName(name);
	}
	
	static final int reservedRows = 2;
	static final int reservedColumns = 2;

	ProjectForTesting project;
	ThreatMatrixTableModel model;
}
