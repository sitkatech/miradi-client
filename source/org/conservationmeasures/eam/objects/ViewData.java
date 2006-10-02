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
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;

public class ViewData extends EAMBaseObject
{
	public ViewData(BaseId idToUse)
	{
		super(idToUse);
		currentMode = "";
		brainstormNodeIds = new IdList();
	}
	
	public ViewData(JSONObject json) throws ParseException
	{
		super(json);
		currentMode = json.optString(TAG_CURRENT_MODE);
		brainstormNodeIds = new IdList(json.optString(TAG_BRAINSTORM_NODE_IDS, "{}"));
	}

	public String getData(String fieldTag)
	{
		if(TAG_CURRENT_MODE.equals(fieldTag))
			return getCurrentMode();
		
		if(TAG_CURRENT_TAB.equals(fieldTag))
			return Integer.toString(getCurrentTab());
		
		if(TAG_BRAINSTORM_NODE_IDS.equals(fieldTag))
			return getBrainstormNodeIds().toString();
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(TAG_CURRENT_MODE.equals(fieldTag))
			setCurrentMode((String)dataValue);
		else if(TAG_CURRENT_TAB.equals(fieldTag))
			setCurrentTab(new Integer((String)dataValue).intValue());
		else if(TAG_BRAINSTORM_NODE_IDS.equals(fieldTag))
			setBrainstormNodeIds(new IdList((String)dataValue));
		else
			super.setData(fieldTag, dataValue);
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
	
	public void setCurrentTab(int newTab)
	{
		currentTab = newTab;
	}
	
	private int getCurrentTab()
	{
		return currentTab;
	}

	private void setCurrentMode(String currentMode)
	{
		this.currentMode = currentMode;
	}

	private String getCurrentMode()
	{
		return currentMode;
	}

	private void setBrainstormNodeIds(IdList brainstormNodeIds)
	{
		this.brainstormNodeIds = brainstormNodeIds;
	}

	private IdList getBrainstormNodeIds()
	{
		return brainstormNodeIds;
	}

	public int getType()
	{
		return ObjectType.VIEW_DATA;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_CURRENT_MODE, getCurrentMode());
		json.put(TAG_BRAINSTORM_NODE_IDS, getBrainstormNodeIds().toString());
		return json;
	}

	public static final String TAG_CURRENT_MODE = "CurrentMode";
	public static final String TAG_BRAINSTORM_NODE_IDS = "BrainstormNodeIds";
	public static final String TAG_CURRENT_TAB = "CurrentTab";
	
	public static final String MODE_DEFAULT = "";
	public static final String MODE_STRATEGY_BRAINSTORM = "StrategyBrainstorm";

	private int currentTab;
	private String currentMode;
	private IdList brainstormNodeIds;
}
