/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
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
		
		project.insertNodeAtId(new NodeTypeDirectThreat(), IdAssigner.INVALID_ID);
		project.insertNodeAtId(new NodeTypeDirectThreat(), IdAssigner.INVALID_ID);
		project.insertNodeAtId(new NodeTypeDirectThreat(), IdAssigner.INVALID_ID);

		assertEquals(reservedRows + 3, model.getRowCount());
	}
	
	public void testGetColumnCount() throws Exception
	{
		assertEquals(reservedColumns, model.getColumnCount());
		project.insertNodeAtId(new NodeTypeTarget(), IdAssigner.INVALID_ID);
		assertEquals(reservedColumns + 1, model.getColumnCount());
	}
	
	public void testGetThreatNames() throws Exception
	{
		String[] threatNames = new String[] {"one", "two"};
		for(int i = 0; i < threatNames.length; ++i)
			createThreat(threatNames[i]);
		
		for(int i = 0; i < threatNames.length; ++i)
			assertEquals("bad threat name " + i + "? ", threatNames[i], model.getValueAt(reservedRows + i, 0));
	}
	
	private void createThreat(String name) throws Exception
	{
		int id = project.insertNodeAtId(new NodeTypeDirectThreat(), IdAssigner.INVALID_ID);
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		node.setText(name);
	}

	static final int reservedRows = 2;
	static final int reservedColumns = 2;

	ProjectForTesting project;
	ThreatMatrixTableModel model;
}
