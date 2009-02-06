/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
			
			Factor node = Factor.findFactor(project, cmd.getObjectRef());
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
