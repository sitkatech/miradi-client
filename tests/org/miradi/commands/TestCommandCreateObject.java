/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.commands;

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;

public class TestCommandCreateObject extends EAMTestCase
{
	public TestCommandCreateObject(String name)
	{
		super(name);
	}

	public void testGetReverseCommand() throws Exception
	{
		CommandCreateObject createCommand = new CommandCreateObject(ObjectType.TASK);
		createCommand.setCreatedId(new BaseId(11));

		CommandDeleteObject reversedCommand = (CommandDeleteObject)createCommand.getReverseCommand();

		assertEquals("not same id?", createCommand.getCreatedId(), reversedCommand.getObjectId());
		assertEquals("not same type", createCommand.getObjectType(), reversedCommand.getObjectType());
	}
	
	public void testRedo() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		
		try
		{
			int type = ObjectType.CAUSE;
			CommandCreateObject cmd = new CommandCreateObject(type);
			assertEquals("already has an id?", BaseId.INVALID, cmd.getCreatedId());
			cmd.execute(project);
			FactorId createdId = new FactorId(cmd.getCreatedId().asInt());
			int highestId = project.getNodeIdAssigner().getHighestAssignedId();
			assertEquals("didn't assign an id?", highestId, createdId.asInt());
			
			Factor node = project.findNode(createdId);
			assertTrue("Didn't construct with extraInfo?", node.isCause());
			
			assertEquals("lost id?", highestId, cmd.getCreatedId().asInt());
			assertEquals("didn't keep same id?", createdId, cmd.getCreatedId());
		}
		finally
		{
			project.close();
		}
	}
}
