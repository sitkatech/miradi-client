/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
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
		FactorId threat1 = createThreat("threat one");
		FactorId threat2 = createThreat("threat two");
		createThreat("threat three");
		FactorId target1 = createTarget("target one");
		FactorId target2 = createTarget("target two");
		createTarget("target three");
		CreateFactorLinkParameter link1to1 = new CreateFactorLinkParameter(threat1, target1);
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, link1to1);
		CreateFactorLinkParameter link1to2 = new CreateFactorLinkParameter(threat1, target2);
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, link1to2);
		CreateFactorLinkParameter link2to2 = new CreateFactorLinkParameter(threat2, target2);
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, link2to2);
		
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
	
	private FactorId createThreat(String name) throws Exception
	{
		FactorId createdId = createNode(new FactorTypeCause(), name);
		((Cause)project.findNode(createdId)).increaseTargetCount();
		return createdId;
	}

	private FactorId createTarget(String name) throws Exception
	{
		return createNode(new FactorTypeTarget(), name);
	}

	private FactorId createNode(FactorType type, String name) throws Exception
	{
		FactorId id = (FactorId)project.createObject(ObjectType.FACTOR, BaseId.INVALID, new CreateFactorParameter(type));
		assertNotEquals("didn't fix id?", BaseId.INVALID, id);
		Factor node = project.findNode(id);
		node.setLabel(name);
		return id;
	}
	
	ProjectForTesting project;
	NonEditableThreatMatrixTableModel model;
}
