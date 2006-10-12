/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;

public class ConceptualModelLinkage extends EAMBaseObject
{
	public ConceptualModelLinkage(BaseId id, ModelNodeId fromNodeId, ModelNodeId toNodeId)
	{
		super(id);
		setFromId(fromNodeId);
		setToId(toNodeId);
		stressLabel = "";
	}

	public ConceptualModelLinkage(int idAsInt, JSONObject jsonObject) throws ParseException 
	{
		super(new BaseId(idAsInt), jsonObject);
		fromId = new ModelNodeId(jsonObject.getInt(TAG_FROM_ID));
		toId = new ModelNodeId(jsonObject.getInt(TAG_TO_ID));
		stressLabel = jsonObject.optString(TAG_STRESS_LABEL, "");
	}
	
	public void setFromId(ModelNodeId fromNodeId)
	{
		fromId = fromNodeId;
	}
	
	public void setToId(ModelNodeId toNodeId)
	{
		toId = toNodeId;
	}

	public int getType()
	{
		return ObjectType.MODEL_LINKAGE;
	}
	
	public ModelNodeId getFromNodeId()
	{
		return fromId;
	}
	
	public ModelNodeId getToNodeId()
	{
		return toId;
	}
	
	public String getStressLabel()
	{
		return stressLabel;
	}
	
	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(TAG_STRESS_LABEL.equals(fieldTag))
			stressLabel = dataValue;
		else
			super.setData(fieldTag, dataValue);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_STRESS_LABEL.equals(fieldTag))
			return getStressLabel();
		
		return super.getData(fieldTag);
	}
	
	public ModelNodeId getNodeId(int direction)
	{
		if(direction == FROM)
			return getFromNodeId();
		if(direction == TO)
			return getToNodeId();
		throw new RuntimeException("Linkage: Unknown direction " + direction);
	}
	
	public ModelNodeId getOppositeNodeId(int direction)
	{
		if(direction == FROM)
			return getNodeId(TO);
		if(direction == TO)
			return getNodeId(FROM);
		throw new RuntimeException("Linkage: Unknown direction " + direction);
	}
	
	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_FROM_ID, fromId.asInt());
		json.put(TAG_TO_ID, toId.asInt());
		json.put(TAG_STRESS_LABEL, stressLabel);
		return json;
	}
	
	private static String TAG_FROM_ID = "FromId";
	private static String TAG_TO_ID = "ToId";
	public static String TAG_STRESS_LABEL = "StressLabel";
	
	public static final int FROM = 1;
	public static final int TO = 2;
	
	private ModelNodeId fromId;
	private ModelNodeId toId;
	private String stressLabel;
}
