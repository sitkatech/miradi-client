/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.json.JSONObject;

public class ViewData extends EAMObject
{
	public ViewData(int idToUse)
	{
		super(idToUse);
	}
	
	public ViewData(JSONObject json)
	{
		super(json);
	}

	public String getData(String fieldTag)
	{
		if(TAG_CURRENT_MODE.equals(fieldTag))
			return getCurrentMode();
		
		if(TAG_BRAINSTORM_NODE_IDS.equals(fieldTag))
			return getBrainstormNodeIds().toString();
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(TAG_CURRENT_MODE.equals(fieldTag))
			setCurrentMode((String)dataValue);
		else if(TAG_BRAINSTORM_NODE_IDS.equals(fieldTag))
			setBrainstormNodeIds(new IdList((String)dataValue));
		else
			super.setData(fieldTag, dataValue);
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

	public static final String TAG_CURRENT_MODE = "CurrentMode";
	public static final String TAG_BRAINSTORM_NODE_IDS = "BrainstormNodeIds";
	
	private String currentMode;
	private IdList brainstormNodeIds;
}
