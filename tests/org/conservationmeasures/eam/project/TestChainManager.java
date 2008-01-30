/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.reports.ChainManager;

public class TestChainManager extends EAMTestCase
{
	public TestChainManager(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}
	
	public void testBasics()
	{
		// TODO: if ChainManager still exists, it would be nice to have tests for it
		new ChainManager(project);
	}
	
	ProjectForTesting project;
}
