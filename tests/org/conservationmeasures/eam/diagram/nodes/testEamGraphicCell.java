/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class testEamGraphicCell extends EAMTestCase
{
	public testEamGraphicCell(String name) 
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		EAMGraphCell cell = new EAMGraphCell();
		assertEquals(Node.INVALID_ID, cell.getId());
		int id = 55;
		cell.setId(id);
		assertEquals(id, cell.getId());
	}
}
