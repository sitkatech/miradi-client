/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;

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
			CreateFactorParameter parameter = new CreateFactorParameter(new FactorTypeCause());
			CommandCreateObject cmd = new CommandCreateObject(type, parameter);
			assertEquals("already has an id?", BaseId.INVALID, cmd.getCreatedId());
			assertEquals("didn't memorize extraInfo?", parameter, cmd.getParameter());
			cmd.execute(project);
			FactorId createdId = new FactorId(cmd.getCreatedId().asInt());
			int highestId = project.getNodeIdAssigner().getHighestAssignedId();
			assertEquals("didn't assign an id?", highestId, createdId.asInt());
			
			Factor node = project.findNode(createdId);
			assertTrue("Didn't construct with extraInfo?", node.isCause());
			
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
