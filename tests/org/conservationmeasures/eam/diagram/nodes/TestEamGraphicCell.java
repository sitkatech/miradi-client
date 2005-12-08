/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestEamGraphicCell extends EAMTestCase
{
	public TestEamGraphicCell(String name) 
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		EAMGraphCell cell = new EAMGraphCell();
		assertFalse("is node?", cell.isNode());
		assertFalse("is linkage?", cell.isLinkage());
	}
}
