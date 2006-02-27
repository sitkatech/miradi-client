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
			Object thisName = model.getThreatName(reservedRows + i);
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
			Object thisName = model.getTargetName(reservedColumns + i);
			assertEquals("bad target name " + i + "? ", targetNames[i], thisName);
		}
	}
	
	public void testIsActiveCell() throws Exception
	{
		int threat1 = createThreat("threat one");
		int threat2 = createThreat("threat two");
		createThreat("threat three");
		int target1 = createTarget("target one");
		int target2 = createTarget("target two");
		createTarget("target three");
		
		project.insertLinkageAtId(IdAssigner.INVALID_ID, threat1, target1);
		project.insertLinkageAtId(IdAssigner.INVALID_ID, threat1, target2);
		project.insertLinkageAtId(IdAssigner.INVALID_ID, threat2, target2);

		assertFalse(model.isActiveCell(reservedRows - 1, reservedColumns - 1));
		int row1 = reservedRows;
		int row2 = row1 + 1;
		int row3 = row2 + 1;
		int col1 = reservedColumns;
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
	
	private int createThreat(String name) throws Exception
	{
		return createNode(new NodeTypeDirectThreat(), name);
	}

	private int createTarget(String name) throws Exception
	{
		return createNode(new NodeTypeTarget(), name);
	}

	private int createNode(NodeType type, String name) throws Exception
	{
		int id = project.insertNodeAtId(type, IdAssigner.INVALID_ID);
		assertNotEquals("didn't fix id?", -1, id);
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		node.setName(name);
		return id;
	}
	
	static final int reservedRows = 2;
	static final int reservedColumns = 2;

	ProjectForTesting project;
	ThreatMatrixTableModel model;
}
