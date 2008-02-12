/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.miradi.diagram;

import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.main.EAMTestCase;

public class TestEamGraphCell extends EAMTestCase
{
	public TestEamGraphCell(String name) 
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		EAMGraphCell cell = new EAMGraphCell();
		assertFalse("is node?", cell.isFactor());
		assertFalse("is link?", cell.isFactorLink());
	}
}
