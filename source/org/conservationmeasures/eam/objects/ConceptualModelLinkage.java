/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.json.JSONObject;

public class ConceptualModelLinkage extends EAMObject
{
	public ConceptualModelLinkage(int id, int fromNodeId, int toNodeId)
	{
		super(id);
		setFromId(fromNodeId);
		setToId(toNodeId);
		stressLabel = "";
	}

	public ConceptualModelLinkage(JSONObject jsonObject) throws ParseException 
	{
		super(jsonObject);
		fromId = jsonObject.getInt(TAG_FROM_ID);
		toId = jsonObject.getInt(TAG_TO_ID);
		stressLabel = jsonObject.optString(TAG_STRESS_LABEL, "");
	}
	
	public void setFromId(int fromNodeId)
	{
		fromId = fromNodeId;
	}
	
	public void setToId(int toNodeId)
	{
		toId = toNodeId;
	}

	public int getType()
	{
		return ObjectType.MODEL_LINKAGE;
	}
	
	public int getFromNodeId()
	{
		return fromId;
	}
	
	public int getToNodeId()
	{
		return toId;
	}
	
	public String getStressLabel()
	{
		return stressLabel;
	}
	
	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(TAG_STRESS_LABEL.equals(fieldTag))
			stressLabel = (String)dataValue;
		else
			super.setData(fieldTag, dataValue);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_STRESS_LABEL.equals(fieldTag))
			return getStressLabel();
		
		return super.getData(fieldTag);
	}
	
	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_FROM_ID, fromId);
		json.put(TAG_TO_ID, toId);
		json.put(TAG_STRESS_LABEL, stressLabel);
		return json;
	}
	
	private static String TAG_FROM_ID = "FromId";
	private static String TAG_TO_ID = "ToId";
	public static String TAG_STRESS_LABEL = "StressLabel";
	
	private int fromId;
	private int toId;
	private String stressLabel;
}
