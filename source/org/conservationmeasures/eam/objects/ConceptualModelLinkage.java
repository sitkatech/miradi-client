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
	public ConceptualModelLinkage(JSONObject jsonObject) throws ParseException 
	{
		super(jsonObject);
		fromId = jsonObject.getInt(TAG_FROM_ID);
		toId = jsonObject.getInt(TAG_TO_ID);
	}
	
	public ConceptualModelLinkage(int id, int fromNodeId, int toNodeId)
	{
		super(id);
		setFromId(fromNodeId);
		setToId(toNodeId);
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
	
	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_FROM_ID, fromId);
		json.put(TAG_TO_ID, toId);
		return json;
	}
	
	private static String TAG_FROM_ID = "FromId";
	private static String TAG_TO_ID = "ToId";
	
	private int fromId;
	private int toId;
}
