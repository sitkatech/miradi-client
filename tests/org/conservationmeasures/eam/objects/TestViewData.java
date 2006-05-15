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
		String modeTag = ViewData.TAG_CURRENT_MODE;
		assertEquals("Didn't start with default mode?", "", vd.getData(modeTag));
		String sampleMode = "Brainstorm";
		vd.setData(modeTag, sampleMode);
		assertEquals("Set/get didn't work?", sampleMode, vd.getData(modeTag));
		
		ViewData got = new ViewData(vd.toJson());
		assertEquals("json didn't preserve mode?", vd.getData(modeTag), got.getData(modeTag));
	}
	
	public void testBrainstormNodeIds() throws Exception
	{
		ViewData vd = new ViewData(33);
		String idsTag = ViewData.TAG_BRAINSTORM_NODE_IDS;
		assertEquals("didn't start with empty id list?", 0, new IdList(vd.getData(idsTag)).size());
		IdList sampleIds = new IdList();
		sampleIds.add(7);
		sampleIds.add(41);
		vd.setData(idsTag, sampleIds.toString());
		assertEquals("Set/get didn't work?", sampleIds, new IdList(vd.getData(idsTag)));

		ViewData got = new ViewData(vd.toJson());
		assertEquals("json didn't preserve ids?", vd.getData(idsTag), got.getData(idsTag));
	}
}
