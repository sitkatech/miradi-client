/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.IdAssigner;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDiagramLinkage extends EAMTestCase
{
	public TestDiagramLinkage(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		cmIntervention = new ConceptualModelIntervention();
		cmTarget = new ConceptualModelTarget();
		
	}

	public void testBasics()
	{
		DiagramNode factor = DiagramNode.wrapConceptualModelObject(cmIntervention);
		DiagramNode target = DiagramNode.wrapConceptualModelObject(cmTarget);
		DiagramLinkage linkage = new DiagramLinkage(factor, target);
		assertEquals("didn't remember from?", factor, linkage.getFromNode());
		assertEquals("didn't remember to?", target, linkage.getToNode());

		assertEquals("source not the port of from?", factor.getPort(), linkage.getSource());
		assertEquals("target not the port of to?", target.getPort(), linkage.getTarget());
	}
	
	public void testIds()
	{
		DiagramNode factor = DiagramNode.wrapConceptualModelObject(cmIntervention);
		DiagramNode target = DiagramNode.wrapConceptualModelObject(cmTarget);
		DiagramLinkage linkage = new DiagramLinkage(factor, target);
		assertEquals(IdAssigner.INVALID_ID,linkage.getId());
		int id = 243;
		linkage.setId(id);
		assertEquals(id, linkage.getId());
	}
	
	public void testLinkNodes() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();
		
		CommandInsertNode insertIntervention = new CommandInsertNode(DiagramNode.TYPE_INTERVENTION);
		CommandInsertNode insertFactor = new CommandInsertNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		insertIntervention.execute(project);
		DiagramNode intervention = model.getNodeById(insertIntervention.getId());
		insertFactor.execute(project);
		DiagramNode factor = model.getNodeById(insertFactor.getId());
		int interventionId = intervention.getId();
		int factorId = factor.getId();
		CommandLinkNodes link = new CommandLinkNodes(interventionId, factorId);
		link.execute(project);
		assertNotNull("linkage not in model?", model.getLinkageById(link.getLinkageId()));
		
		project.close();
	}
	
	ConceptualModelIntervention cmIntervention;
	ConceptualModelTarget cmTarget;
}
