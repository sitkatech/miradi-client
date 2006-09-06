/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestCommandCreateObject extends EAMTestCase
{
	public TestCommandCreateObject(String name)
	{
		super(name);
	}

	public void testRedo() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		
		try
		{
			int type = ObjectType.MODEL_NODE;
			CreateModelNodeParameter parameter = new CreateModelNodeParameter(new NodeTypeFactor());
			CommandCreateObject cmd = new CommandCreateObject(type, parameter);
			assertEquals("already has an id?", BaseId.INVALID, cmd.getCreatedId());
			assertEquals("didn't memorize extraInfo?", parameter, cmd.getParameter());
			cmd.execute(project);
			BaseId createdId = cmd.getCreatedId();
			int highestId = project.getNodeIdAssigner().getHighestAssignedId();
			assertEquals("didn't assign an id?", highestId, createdId.asInt());
			
			ConceptualModelNode node = project.findNode(createdId);
			assertTrue("Didn't construct with extraInfo?", node.isFactor());
			
			cmd.undo(project);
			assertEquals("lost id?", highestId, cmd.getCreatedId().asInt());
			cmd.execute(project);
			assertEquals("didn't keep same id?", createdId, cmd.getCreatedId());
		}
		finally
		{
			project.close();
		}
	}
}
