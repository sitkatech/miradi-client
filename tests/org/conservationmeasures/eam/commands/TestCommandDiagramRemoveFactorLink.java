/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestCommandDiagramRemoveFactorLink extends EAMTestCase
{
	public TestCommandDiagramRemoveFactorLink(String name)
	{
		super(name);
	}
	
	public void testGetReverseCommand() throws Exception
	{
		CommandDiagramRemoveFactorLink removeFactorLink = new CommandDiagramRemoveFactorLink(new DiagramFactorLinkId(34)); 
		CommandDiagramAddFactorLink reverseCommand = (CommandDiagramAddFactorLink) removeFactorLink.getReverseCommand();
		
		assertEquals("not same factor link id?", removeFactorLink.getFactorLinkId(), reverseCommand.getFactorLinkId());
		assertEquals("not same id?", removeFactorLink.getDiagramFactorLinkId(), reverseCommand.getDiagramFactorLinkId());
	}
}
