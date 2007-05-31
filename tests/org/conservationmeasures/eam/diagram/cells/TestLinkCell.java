/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestLinkCell extends EAMTestCase
{
	public TestLinkCell(String name)
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
	}
	
	public void testGetNewBendPointList() throws Exception
	{
		fail();
	}
	
	ProjectForTesting project;

}
