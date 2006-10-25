/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.IntegerData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ViewData extends EAMBaseObject
{
	public ViewData(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}

	public ViewData(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
	}

	public Command[] buildCommandsToAddNode(BaseId idToAdd) throws ParseException
	{
		if(getCurrentMode().equals(MODE_DEFAULT))
			return new Command[0];
		
		CommandSetObjectData cmd = CommandSetObjectData.createAppendIdCommand(this, TAG_BRAINSTORM_NODE_IDS, idToAdd);
		return new Command[] {cmd};
	}

	public Command[] buildCommandsToRemoveNode(BaseId idToRemove) throws ParseException
	{
		if(getCurrentMode().equals(MODE_DEFAULT))
			return new Command[0];
		
		CommandSetObjectData cmd = CommandSetObjectData.createRemoveIdCommand(this, TAG_BRAINSTORM_NODE_IDS, idToRemove);
		return new Command[] {cmd};
	}
	
	public void setCurrentTab(int newTab) throws Exception
	{
		currentTab.set(Integer.toString(newTab));
	}
	
	public int getCurrentTab()
	{
		return currentTab.asInt();
	}

	private String getCurrentMode()
	{
		return currentMode.get();
	}

	public int getType()
	{
		return ObjectType.VIEW_DATA;
	}
	
	void clear()
	{
		super.clear();
		currentMode = new StringData();
		brainstormNodeIds = new IdListData();
		currentTab = new IntegerData();
		
		addField(TAG_CURRENT_MODE, currentMode);
		addField(TAG_BRAINSTORM_NODE_IDS, brainstormNodeIds);
		addField(TAG_CURRENT_TAB, currentTab);
	}
	
	public static final String TAG_CURRENT_MODE = "CurrentMode";
	public static final String TAG_BRAINSTORM_NODE_IDS = "BrainstormNodeIds";
	public static final String TAG_CURRENT_TAB = "CurrentTab";
	
	public static final String MODE_DEFAULT = "";
	public static final String MODE_STRATEGY_BRAINSTORM = "StrategyBrainstorm";

	private IntegerData currentTab;
	private StringData currentMode;
	private IdListData brainstormNodeIds;
}
