/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.json.JSONObject;

public class ConceptualModelCluster extends ConceptualModelNode
{
	public ConceptualModelCluster(BaseId idToUse)
	{
		super(idToUse, DiagramNode.TYPE_CLUSTER);
		setMembers(new IdList());
	}

	public ConceptualModelCluster(JSONObject json) throws ParseException
	{
		super(DiagramNode.TYPE_CLUSTER, json);
		setMembers(new IdList(json.optString(TAG_MEMBER_IDS, "{}")));
	}
	
	public boolean isCluster()
	{
		return true;
	}
	
	public IdList getMemberIds()
	{
		return memberIds;
	}

	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_MEMBER_IDS))
			return getMemberIds().toString();
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_MEMBER_IDS))
			setMembers(new IdList((String)dataValue));
		else
			super.setData(fieldTag, dataValue);
	}
	
	public void setMembers(IdList members)
	{
		EAM.logVerbose("Cluster " + getId() + " setMembers: " + members);
		memberIds = members;
	}

	public JSONObject toJson()
	{
		JSONObject json = createBaseJsonObject(CLUSTER_TYPE);
		json.put(TAG_MEMBER_IDS, getMemberIds().toString());
		return json;
	}

	public static final String TAG_MEMBER_IDS = "Members";
	
	IdList memberIds;
}
