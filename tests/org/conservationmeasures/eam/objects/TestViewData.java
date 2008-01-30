/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.TestCaseWithProject;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestViewData extends TestCaseWithProject
{
	public TestViewData(String name)
	{
		super(name);
	}
	
	public void testCurrentTab() throws Exception
	{
		ViewData vd = new ViewData(getObjectManager(), new BaseId(33));
		assertEquals("", vd.getData(ViewData.TAG_CURRENT_TAB));
		int tab = 6;
		vd.setData(ViewData.TAG_CURRENT_TAB, Integer.toString(tab));
		assertEquals(6, new Integer(vd.getData(ViewData.TAG_CURRENT_TAB)).intValue());
		
		ViewData got = new ViewData(getObjectManager(), 55, vd.toJson());
		assertEquals(vd.getData(ViewData.TAG_CURRENT_TAB), got.getData(ViewData.TAG_CURRENT_TAB));
	}

	
	public void testCurrentSortData() throws Exception
	{
		
		ViewData vd1 = new ViewData(getObjectManager(), new BaseId(33));
		vd1.setData(ViewData.TAG_CURRENT_SORT_BY, "TAREGT");
		assertEquals("TAREGT", vd1.getData(ViewData.TAG_CURRENT_SORT_BY));

		ViewData vd2 = new ViewData(getObjectManager(), new BaseId(34));
		vd2.setData(ViewData.TAG_CURRENT_SORT_DIRECTION, "ASCENDING");
		assertEquals("ASCENDING", vd2.getData(ViewData.TAG_CURRENT_SORT_DIRECTION));
		
	}
	
	
	public void testMode() throws Exception
	{
		ViewData vd = new ViewData(getObjectManager(), new BaseId(33));
		String modeTag = ViewData.TAG_CURRENT_MODE;
		assertEquals("Didn't start with default mode?", "", vd.getData(modeTag));
		String sampleMode = "Brainstorm";
		vd.setData(modeTag, sampleMode);
		assertEquals("Set/get didn't work?", sampleMode, vd.getData(modeTag));
		
		ViewData got = (ViewData)BaseObject.createFromJson(getObjectManager(), vd.getType(), vd.toJson());
		assertEquals("json didn't preserve mode?", vd.getData(modeTag), got.getData(modeTag));
	}
	
	public void testBrainstormNodeIds() throws Exception
	{
		ViewData vd = new ViewData(getObjectManager(), new BaseId(33));
		String ORefsTag = ViewData.TAG_CHAIN_MODE_FACTOR_REFS;
		assertEquals("didn't start with empty id list?", 0, new ORefList(vd.getData(ORefsTag)).size());
		ORefList sampleORefs = createSampleORefList();
		vd.setData(ORefsTag, sampleORefs.toString());
		assertEquals("Set/get didn't work?", sampleORefs, new ORefList(vd.getData(ORefsTag)));

		ViewData got = (ViewData)BaseObject.createFromJson(getObjectManager(), vd.getType(), vd.toJson());
		assertEquals("json didn't preserve ids?", vd.getData(ORefsTag), got.getData(ORefsTag));
	}

	private ORefList createSampleORefList()
	{
		ORefList sampleRefs = new ORefList();
		sampleRefs.add(new ORef(ObjectType.TARGET, new BaseId(4)));
		sampleRefs.add(new ORef(ObjectType.TARGET, new BaseId(5)));
		
		return sampleRefs;
	}
	
	public void testBuildCommandsToInsertNode() throws Exception
	{
		ViewData vd = new ViewData(getObjectManager(), new BaseId(33));
		ORefList sampleORefs = createSampleORefList();
		vd.setData(ViewData.TAG_CHAIN_MODE_FACTOR_REFS, sampleORefs.toString());
		FactorId idToAdd = new FactorId(983);
		ORef oRefToAdd = new ORef(ObjectType.TARGET, idToAdd);
		Command[] inNormalMode = vd.buildCommandsToAddNode(oRefToAdd);
		assertEquals("added when not in brainstorm mode?", 0, inNormalMode.length);
		
		vd.setData(ViewData.TAG_CURRENT_MODE, ViewData.MODE_STRATEGY_BRAINSTORM);
		Command[] inBrainstormMode = vd.buildCommandsToAddNode(oRefToAdd);
		assertEquals("didn't add when in brainstorm mode?", 1, inBrainstormMode.length);
		CommandSetObjectData cmd = (CommandSetObjectData)inBrainstormMode[0];
		ORefList expected = new ORefList(sampleORefs);
		expected.add(oRefToAdd);
		assertEquals("command wrong id?", vd.getId(), cmd.getObjectId());
		assertEquals("command wrong field?", ViewData.TAG_CHAIN_MODE_FACTOR_REFS, cmd.getFieldTag());
		assertEquals("didn't create proper command?", expected.toString(), cmd.getDataValue());
	}
	
	public void testBuildCommandsToRemoveNode() throws Exception
	{
		ViewData vd = new ViewData(getObjectManager(), new BaseId(33));
		ORefList sampleORefs = createSampleORefList();
		vd.setData(ViewData.TAG_CHAIN_MODE_FACTOR_REFS, sampleORefs.toString());
		ORef oRefToRemove = sampleORefs.get(0);
		
		Command[] inNormalMode = vd.buildCommandsToRemoveNode(oRefToRemove);
		assertEquals("removed when not in brainstorm mode?", 0, inNormalMode.length);
		
		vd.setData(ViewData.TAG_CURRENT_MODE, ViewData.MODE_STRATEGY_BRAINSTORM);
		Command[] inBrainstormMode = vd.buildCommandsToRemoveNode(oRefToRemove);
		assertEquals("didn't remove when in brainstorm mode?", 1, inBrainstormMode.length);
		CommandSetObjectData cmd = (CommandSetObjectData)inBrainstormMode[0];
		ORefList expected = new ORefList(sampleORefs);
		expected.remove(oRefToRemove);
		assertEquals("command wrong id?", vd.getId(), cmd.getObjectId());
		assertEquals("command wrong field?", ViewData.TAG_CHAIN_MODE_FACTOR_REFS, cmd.getFieldTag());
		assertEquals("didn't create proper command?", expected.toString(), cmd.getDataValue());
	}
}
