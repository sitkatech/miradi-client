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
