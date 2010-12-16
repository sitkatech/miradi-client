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

import java.awt.Dimension;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.DiagramFactorId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.utils.EnhancedJsonObject;

public class TestCommandSetFactorSize extends EAMTestCase
{
	public TestCommandSetFactorSize(String name)
	{
		super(name);
	}
	
	public void testGetReverseCommand() throws Exception
	{
		String previousSize = EnhancedJsonObject.convertFromDimension(new Dimension(1, 1));
		String currentSize = EnhancedJsonObject.convertFromDimension(new Dimension(2, 2));
		CommandSetObjectData commandSetSize = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, new DiagramFactorId(56), currentSize, previousSize);
		CommandSetObjectData reverseCommand = (CommandSetObjectData)commandSetSize.getReverseCommand();
		
		assertEquals("not same id?", commandSetSize.getObjectId(), reverseCommand.getObjectId());
		assertEquals("not same previous size?", commandSetSize.getPreviousDataValue(), reverseCommand.getDataValue());
		assertEquals("not same current size?", commandSetSize.getDataValue(), reverseCommand.getPreviousDataValue());		
	}
}
