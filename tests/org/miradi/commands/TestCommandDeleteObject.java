/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.commands;

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.ids.BaseId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ObjectType;

public class TestCommandDeleteObject extends EAMTestCase
{
	public TestCommandDeleteObject(String name)
	{
		super(name);
	}

	public void testReverseCommand() throws Exception
	{
		CommandDeleteObject deleteCommand = new CommandDeleteObject(ObjectType.TASK, new BaseId(9));
		CommandCreateObject reversedCommand = (CommandCreateObject)deleteCommand.getReverseCommand();

		assertEquals("not same type?", deleteCommand.getObjectType(), reversedCommand.getObjectType());
		assertEquals("not same id?", deleteCommand.getObjectId(), reversedCommand.getCreatedId());
	}
}
