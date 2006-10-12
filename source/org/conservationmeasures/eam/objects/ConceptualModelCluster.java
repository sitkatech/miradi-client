/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCluster;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.json.JSONObject;

public class ConceptualModelCluster extends ConceptualModelNode
{
	public ConceptualModelCluster(BaseId idToUse)
	{
		super(idToUse, DiagramNode.TYPE_CLUSTER);
		memberIds = new IdListData();
	}

	public ConceptualModelCluster(ModelNodeId idToUse, JSONObject json) throws ParseException
	{
		super(idToUse, DiagramNode.TYPE_CLUSTER, json);
		memberIds = new IdListData(json.optString(TAG_MEMBER_IDS));
	}
	
	public boolean isCluster()
	{
		return true;
	}
	
	public IdList getMemberIds()
	{
		return memberIds.getIdList();
	}

	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_MEMBER_IDS))
			return memberIds.get();
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_MEMBER_IDS))
			memberIds.set(dataValue);
		else
			super.setData(fieldTag, dataValue);
	}
	
	public JSONObject toJson()
	{
		JSONObject json = createBaseJsonObject(NodeTypeCluster.CLUSTER_TYPE);
		json.put(TAG_MEMBER_IDS, memberIds.get());
		return json;
	}

	public static final String TAG_MEMBER_IDS = "Members";
	
	IdListData memberIds;
}
