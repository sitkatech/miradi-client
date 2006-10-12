/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;

public class Indicator extends EAMBaseObject
{
	public Indicator(IndicatorId idToUse)
	{
		super(idToUse);
		shortLabel = new StringData();
		method = new StringData();
		resourceIds = new IdList();
	}
	
	public Indicator(int idAsInt, JSONObject json) throws ParseException
	{
		super(new BaseId(idAsInt), json);
		shortLabel = new StringData(json.optString(TAG_SHORT_LABEL));
		method = new StringData(json.optString(TAG_METHOD));
		resourceIds = new IdList(json.optString(TAG_RESOURCE_IDS, "{}"));
	}
	
	public int getType()
	{
		return ObjectType.INDICATOR;
	}

	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_SHORT_LABEL))
			return getShortLabel();
		if(fieldTag.equals(TAG_METHOD))
			return method.get();
		if(fieldTag.equals(TAG_RESOURCE_IDS))
			return getResourceIdList().toString();
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_SHORT_LABEL))
			shortLabel.set(dataValue);
		else if(fieldTag.equals(TAG_METHOD))
			method.set(dataValue);
		else if(fieldTag.equals(TAG_RESOURCE_IDS))
			resourceIds = new IdList(dataValue);
		else
			super.setData(fieldTag, dataValue);
	}
	
	public String getShortLabel()
	{
		return shortLabel.get();
	}
	
	public IdList getResourceIdList()
	{
		return resourceIds;
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_SHORT_LABEL, getShortLabel());
		json.put(TAG_METHOD, method.get());
		json.put(TAG_RESOURCE_IDS, resourceIds.toString());
		
		return json;
	}
	
	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return shortLabel + ": " + getLabel();
	}

	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_METHOD = "Method";
	public static final String TAG_RESOURCE_IDS = "ResourceIds";

	StringData shortLabel;
	StringData method;
	IdList resourceIds;
}
