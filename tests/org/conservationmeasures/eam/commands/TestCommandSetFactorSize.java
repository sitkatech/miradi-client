/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import java.awt.Dimension;

import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestCommandSetFactorSize extends EAMTestCase
{
	public TestCommandSetFactorSize(String name)
	{
		super(name);
	}
	
	public void testGetReverseCommand() throws Exception
	{
		Dimension previousSize = new Dimension(1, 1);
		Dimension currentSize = new Dimension(2, 2);
		CommandSetFactorSize commandSetSize = new CommandSetFactorSize(new DiagramFactorId(56), currentSize, previousSize);
		CommandSetFactorSize reverseCommand = (CommandSetFactorSize)commandSetSize.getReverseCommand();
		
		assertEquals("not same id?", commandSetSize.getId(), reverseCommand.getId());
		assertEquals("not same previous size?", commandSetSize.getPreviousSize(), reverseCommand.getCurrentSize());
		assertEquals("not same current size?", commandSetSize.getCurrentSize(), reverseCommand.getPreviousSize());		
	}
}
