/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.objects;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ViewData;

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
