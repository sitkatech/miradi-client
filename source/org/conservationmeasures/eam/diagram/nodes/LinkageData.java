/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.text.ParseException;

import org.json.JSONObject;

public class LinkageData 
{
	public LinkageData(DiagramLinkage linkage) throws Exception
	{
		this(linkage.getId(), linkage.getFromNode().getId(), linkage.getToNode().getId());
	}
	
	public LinkageData(JSONObject jsonObject) throws ParseException 
	{
		// TODO: Verify that it's the right kind of object!
		// TODO: Maybe only copy fields that we know about?
		json = jsonObject;
	}
	
	public LinkageData(int id, int fromId, int toId)
	{
		json = new JSONObject();
		json.put(TYPE, LINKAGE);
		json.put(ID, id);
		json.put(FROMID, fromId);
		json.put(TOID, toId);
	}
	
	public int getId()
	{
		return json.getInt(ID);
	}
	
	public int getFromNodeId()
	{
		return json.getInt(FROMID);
	}
	
	public int getToNodeId()
	{
		return json.getInt(TOID);
	}
	
	public JSONObject toJson()
	{
		return json;
	}
	
	private static String TYPE = "Type";
	private static String ID = "Id";
	private static String FROMID = "FromId";
	private static String TOID = "ToId";
	
	private static String LINKAGE = "Linkage";
	
	private JSONObject json;
}
