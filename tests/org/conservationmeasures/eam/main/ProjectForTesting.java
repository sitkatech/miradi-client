/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;



public class ProjectForTesting extends Project
{
	public ProjectForTesting(String testName) throws Exception
	{
		getDatabase().openMemoryDatabase(testName);
		loadCommandsFromDatabase();
	}
}
