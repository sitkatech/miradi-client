/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.project.IdAssigner;
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
		
		int type = ObjectType.TASK;
		CommandCreateObject cmd = new CommandCreateObject(type);
		assertEquals("already has an id?", IdAssigner.INVALID_ID, cmd.getCreatedId());
		cmd.execute(project);
		int createdId = cmd.getCreatedId();
		int highestId = project.getAnnotationIdAssigner().getHighestAssignedId();
		assertEquals("didn't assign an id?", highestId, createdId);
		cmd.undo(project);
		assertEquals("lost id?", highestId, cmd.getCreatedId());
		cmd.execute(project);
		assertEquals("didn't keep same id?", createdId, cmd.getCreatedId());
		project.close();
	}
}
