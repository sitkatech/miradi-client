/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCause;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelCause;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.martus.util.TestCaseEnhanced;

public class TestNonEditableThreatMatrixTableModel extends TestCaseEnhanced
{
	public TestNonEditableThreatMatrixTableModel(String name)
	{
		super(name);
	}
	
	protected void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		model = new NonEditableThreatMatrixTableModel(project);
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
		model.resetMatrix();
		assertEquals(3, model.getThreatCount());
	}
	
	public void testGetColumnCount() throws Exception
	{
		assertEquals(0, model.getTargetCount());
		createTarget("one");
		createTarget("two");
		createTarget("three");
		model.resetMatrix();
		assertEquals(3, model.getTargetCount());
	}
	
	public void testGetThreatNames() throws Exception
	{
		String[] threatNames = new String[] {"one", "two"};
		for(int i = 0; i < threatNames.length; ++i)
			createThreat(threatNames[i]);
		
		model.resetMatrix();
		
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
		
		model.resetMatrix();
		
		for(int i = 0; i < targetNames.length; ++i)
		{
			Object thisName = model.getTargetName(i);
			assertEquals("bad target name " + i + "? ", targetNames[i], thisName);
		}
	}
	
	public void testIsActiveCell() throws Exception
	{
		ModelNodeId threat1 = createThreat("threat one");
		ModelNodeId threat2 = createThreat("threat two");
		createThreat("threat three");
		ModelNodeId target1 = createTarget("target one");
		ModelNodeId target2 = createTarget("target two");
		createTarget("target three");
		CreateModelLinkageParameter link1to1 = new CreateModelLinkageParameter(threat1, target1);
		project.createObject(ObjectType.MODEL_LINKAGE, BaseId.INVALID, link1to1);
		CreateModelLinkageParameter link1to2 = new CreateModelLinkageParameter(threat1, target2);
		project.createObject(ObjectType.MODEL_LINKAGE, BaseId.INVALID, link1to2);
		CreateModelLinkageParameter link2to2 = new CreateModelLinkageParameter(threat2, target2);
		project.createObject(ObjectType.MODEL_LINKAGE, BaseId.INVALID, link2to2);
		
		model.resetMatrix();
		
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
	
	private ModelNodeId createThreat(String name) throws Exception
	{
		ModelNodeId createdId = createNode(new NodeTypeCause(), name);
		((ConceptualModelCause)project.findNode(createdId)).increaseTargetCount();
		return createdId;
	}

	private ModelNodeId createTarget(String name) throws Exception
	{
		return createNode(new NodeTypeTarget(), name);
	}

	private ModelNodeId createNode(NodeType type, String name) throws Exception
	{
		ModelNodeId id = (ModelNodeId)project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, new CreateModelNodeParameter(type));
		assertNotEquals("didn't fix id?", BaseId.INVALID, id);
		ConceptualModelNode node = project.findNode(id);
		node.setLabel(name);
		return id;
	}
	
	ProjectForTesting project;
	NonEditableThreatMatrixTableModel model;
}
