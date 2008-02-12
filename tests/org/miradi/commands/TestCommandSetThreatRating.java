/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.commands;

import org.miradi.commands.CommandSetThreatRating;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAMTestCase;

public class TestCommandSetThreatRating extends EAMTestCase
{
	public TestCommandSetThreatRating(String name)
	{
		super(name);
	}
	
	public void testGetReverseCommand() throws Exception
	{
		CommandSetThreatRating commandSetThreatRating = new CommandSetThreatRating(new FactorId(3), new FactorId(5), new BaseId(6), new BaseId(8));
		CommandSetThreatRating reverseCommand = (CommandSetThreatRating) commandSetThreatRating.getReverseCommand();
		
		assertEquals("not same threat id?", commandSetThreatRating.getThreatId(), reverseCommand.getThreatId());
		assertEquals("not same target id?", commandSetThreatRating.getTargetId(), reverseCommand.getTargetId());
		assertEquals("not same criterion id?", commandSetThreatRating.getCriterionId(), reverseCommand.getCriterionId());
		assertEquals("not same value?", commandSetThreatRating.previousValueId, reverseCommand.getValueId());
	}
}
