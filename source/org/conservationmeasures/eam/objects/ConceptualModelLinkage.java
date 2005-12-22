/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.json.JSONObject;

public class ConceptualModelLinkage 
{
	public ConceptualModelLinkage(JSONObject jsonObject) throws ParseException 
	{
		// TODO: Verify that it's the right kind of object!
		// TODO: Maybe only copy fields that we know about?
		json = jsonObject;
	}
	
	public ConceptualModelLinkage(int id, int fromId, int toId)
	{
		json = new JSONObject();
		json.put(TAG_JSON_TYPE, JSON_TYPE_LINKAGE);
		json.put(TAG_ID, id);
		json.put(TAG_FROM_ID, fromId);
		json.put(TAG_TO_ID, toId);
	}
	
	public int getId()
	{
		return json.getInt(TAG_ID);
	}
	
	public int getFromNodeId()
	{
		return json.getInt(TAG_FROM_ID);
	}
	
	public int getToNodeId()
	{
		return json.getInt(TAG_TO_ID);
	}
	
	public JSONObject toJson()
	{
		return json;
	}
	
	private static String TAG_JSON_TYPE = "Type";
	private static String TAG_ID = "Id";
	private static String TAG_FROM_ID = "FromId";
	private static String TAG_TO_ID = "ToId";
	
	private static String JSON_TYPE_LINKAGE = "Linkage";
	
	private JSONObject json;
}
