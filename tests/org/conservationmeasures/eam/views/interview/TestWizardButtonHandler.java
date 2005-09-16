/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandSetData;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestWizardButtonHandler extends EAMTestCase
{
	public TestWizardButtonHandler(String name)
	{
		super(name);
	}
	
	public void testCreateDataCommands() throws Exception
	{
		HashMap dataMap = new HashMap();
		String[] fieldNames = {"field 1", "field2", "f3", };
		String[] fieldData = {"one", "two", "three", };
		for(int i=0; i < fieldNames.length; ++i)
			dataMap.put(fieldNames[i], fieldData[i]);
		
		Vector commands = WizardButtonHandler.createDataCommands(dataMap);
		assertEquals("wrong number of commands?", fieldNames.length, commands.size());
		for(int i=0; i < commands.size(); ++i)
		{
			CommandSetData command = (CommandSetData)commands.get(i);
			assertEquals(fieldNames[i], command.getFieldName());
			assertEquals(fieldData[i], command.getFieldData());
		}
	}

}
