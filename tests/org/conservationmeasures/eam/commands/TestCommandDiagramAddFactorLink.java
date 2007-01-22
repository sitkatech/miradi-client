/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestCommandDiagramAddFactorLink extends EAMTestCase
{
	public TestCommandDiagramAddFactorLink(String name)
	{
		super(name);
	}
	
	public void testGetReverseCommand() throws Exception
	{
		CommandDiagramAddFactorLink addFactorLink = new CommandDiagramAddFactorLink(new FactorLinkId(56)); 
		CommandDiagramRemoveFactorLink reverseCommand = (CommandDiagramRemoveFactorLink) addFactorLink.getReverseCommand();
		
		assertEquals("not same factor link id?", addFactorLink.getFactorLinkId(), reverseCommand.getFactorLinkId());
		assertEquals("not same diagram link id?", addFactorLink.getDiagramFactorLinkId(), reverseCommand.getDiagramFactorLinkId());
	}
}
