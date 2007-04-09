/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestInsertFactorLinkDoer extends EAMTestCase
{
	public TestInsertFactorLinkDoer(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		doer = new InsertFactorLinkDoer();
		doer.setProject(project);
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}
	
	public void testwouldCreateLinkageLoop() throws Exception
	{
		FactorId node1 = project.createNodeAndAddToDiagram(ObjectType.TARGET);
		FactorId node2 = project.createNodeAndAddToDiagram(ObjectType.TARGET);
		FactorId node3 = project.createNodeAndAddToDiagram(ObjectType.TARGET);
		
		CreateFactorLinkParameter parameter12 = new CreateFactorLinkParameter(node1, node2);
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, parameter12);
		
		CreateFactorLinkParameter parameter23 = new CreateFactorLinkParameter(node2, node3);
		project.createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, parameter23);
		
		DiagramModel model = project.getDiagramModel();
		assertTrue("Didnt catch loop?", doer.wouldCreateLinkageLoop(model, node3, node1));
		assertFalse("Prevented legal Link?", doer.wouldCreateLinkageLoop(model, node1, node3));
		assertTrue("Didnt catch link to itself?", doer.wouldCreateLinkageLoop(model, node1, node1));

	}
	
	public void testIsAvailable() throws Exception
	{
		try
		{
			assertFalse("Enabled when no nodes in the system?", doer.isAvailable());
			project.createNodeAndAddToDiagram(ObjectType.TARGET);
			assertFalse("Enabled when only 1 node?", doer.isAvailable());
			project.createNodeAndAddToDiagram(ObjectType.CAUSE);
			assertTrue("not enabled when 2 nodes?", doer.isAvailable());
		}
		finally
		{
			project.close();
		}
		assertFalse("enabled when no project open?", doer.isAvailable());
		
	}

	private ProjectForTesting project;
	private InsertFactorLinkDoer doer;

}
