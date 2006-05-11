/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestViewData extends EAMTestCase
{
	public TestViewData(String name)
	{
		super(name);
	}

	public void testMode() throws Exception
	{
		ViewData vd = new ViewData(33);
		String sampleMode = "Brainstorm";
		vd.setData(ViewData.TAG_CURRENT_MODE, sampleMode);
		assertEquals("Set/get didn't work?", sampleMode, vd.getData(ViewData.TAG_CURRENT_MODE));
	}
	
	public void testBrainstormNodeIds() throws Exception
	{
		ViewData vd = new ViewData(33);
		IdList sampleIds = new IdList();
		sampleIds.add(7);
		sampleIds.add(41);
		vd.setData(ViewData.TAG_BRAINSTORM_NODE_IDS, sampleIds.toString());
		assertEquals("Set/get didn't work?", sampleIds, new IdList(vd.getData(ViewData.TAG_BRAINSTORM_NODE_IDS)));
	}
}
