/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
