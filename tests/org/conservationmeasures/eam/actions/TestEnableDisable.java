/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestEnableDisable extends EAMTestCase
{
	public TestEnableDisable(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		mainWindow = new MainWindow();
	}

	public void testInsertConnection()
	{
		ActionInsertConnection action = new ActionInsertConnection(mainWindow);
		assertFalse("Enabled when no project open?", action.shouldBeEnabled());
		EAM.logError("TestEnableDisable.testInsertConnection needs more tests");
	}

	MainWindow mainWindow;
}
