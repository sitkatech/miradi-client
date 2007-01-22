/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.views.diagram.DiagramView;

public class TestCommandSwitchView extends EAMTestCase
{
	public TestCommandSwitchView(String name)
	{
		super(name);
	}
	
	public void testGetReverseCommand() throws Exception
	{
		CommandSwitchView commandSwitchView = new CommandSwitchView(DiagramView.getViewName());
		CommandSwitchView reverseCommand = (CommandSwitchView) commandSwitchView.getReverseCommand();
		
		assertEquals("not same view?", commandSwitchView.getPreviousView(), reverseCommand.getDestinationView());
	}
}
