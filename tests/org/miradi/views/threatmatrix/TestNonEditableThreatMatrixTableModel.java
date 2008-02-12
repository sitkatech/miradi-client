/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.views.threatmatrix;

import org.martus.util.TestCaseEnhanced;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.project.ProjectForTesting;
import org.miradi.views.threatmatrix.ThreatMatrixTableModel;

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
		model = new ThreatMatrixTableModel(project);
	}

	protected void tearDown() throws Exception
	{
		project.close();
		project = null;
		super.tearDown();
	}

	public void testGetThreatCount() throws Exception
	{
		assertEquals(0, model.getThreatCount());
		
		createThreat("one");
		createThreat("two");
		createThreat("three");
		model.resetTargetAndThreats();
		assertEquals(3, model.getThreatCount());
	}
	
	public void testGetColumnCount() throws Exception
	{
		assertEquals(0, model.getTargetCount());
		createTarget("one");
		createTarget("two");
		createTarget("three");
		model.resetTargetAndThreats();
		assertEquals(3, model.getTargetCount());
	}
	
	public void testGetThreatNames() throws Exception
	{
		String[] threatNames = new String[] {"one", "two"};
		for(int i = 0; i < threatNames.length; ++i)
			createThreat(threatNames[i]);
		
		model.resetTargetAndThreats();
		
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
		
		model.resetTargetAndThreats();
		
		assertEquals("wrong lenght?", targetNames.length, model.getTargetCount());
		for(int i = 0; i < targetNames.length; ++i)
		{
			Object thisName = model.getTargetName(i);
			assertContains(thisName, targetNames);
		}
	}
	
	public void testIsActiveCell() throws Exception
	{
		DiagramFactor threat1 = createThreat("threat a");
		DiagramFactor threat2 = createThreat("threat b");
		createThreat("threat c");
		
		DiagramFactor target1 = createTarget("target A");
		DiagramFactor target2 = createTarget("target B");
		createTarget("target C");
		
		CreateFactorLinkParameter link1to1 = new CreateFactorLinkParameter(threat1.getWrappedORef(), target1.getWrappedORef());
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, link1to1);
		
		CreateFactorLinkParameter link1to2 = new CreateFactorLinkParameter(threat1.getWrappedORef(), target2.getWrappedORef());
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, link1to2);
		
		CreateFactorLinkParameter link2to2 = new CreateFactorLinkParameter(threat2.getWrappedORef(), target2.getWrappedORef());
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, link2to2);
		
		model.resetTargetAndThreats();
		
		assertEquals(threat2.getWrappedId(), model.getDirectThreats()[1].getId());
		assertEquals(target2.getWrappedId(), model.getTargets()[1].getId());
		
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
	
	
	private DiagramFactor createTarget(String name) throws Exception
	{
		return createNode(ObjectType.TARGET, name);
	}
	
	private DiagramFactor createThreat(String name) throws Exception
	{
		DiagramFactor threat = createNode(ObjectType.CAUSE, name);
		DiagramFactor target = createTarget(name);
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(threat.getWrappedORef(), target.getWrappedORef());
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, parameter);
		
		return threat;
	}

	private DiagramFactor createNode(int type, String name) throws Exception
	{
		DiagramFactor target = project.createDiagramFactorAndAddToDiagram(type);
		Factor targetNode = project.findNode(target.getWrappedId());
		targetNode.setLabel(name);
		return target;
	}

	ProjectForTesting project;
	ThreatMatrixTableModel model;
}
