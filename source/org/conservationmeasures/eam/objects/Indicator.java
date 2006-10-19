/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.util.HashMap;
import java.util.Iterator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.ObjectData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Indicator extends EAMBaseObject
{
	public Indicator(IndicatorId idToUse)
	{
		super(idToUse);
		clear();
	}

	public Indicator(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			setData(tag, json.optString(tag));
		}
	}
	
	void clear()
	{
		super.clear();
		shortLabel = new StringData();
		method = new StringData();
		resourceIds = new IdListData();
		location = new StringData();

		fields = new HashMap();
		fields.put(TAG_SHORT_LABEL, shortLabel);
		fields.put(TAG_METHOD, method);
		fields.put(TAG_RESOURCE_IDS, resourceIds);
		fields.put(TAG_LOCATION, location);
	}
	
	public int getType()
	{
		return ObjectType.INDICATOR;
	}

	public String getData(String fieldTag)
	{
		if(!fields.containsKey(fieldTag))
			return super.getData(fieldTag);

		return getField(fieldTag).get();
		
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(!fields.containsKey(fieldTag))
			super.setData(fieldTag, dataValue);
		else
			getField(fieldTag).set(dataValue);
	}

	private ObjectData getField(String fieldTag)
	{
		ObjectData data = (ObjectData)fields.get(fieldTag);
		return data;
	}
	
	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	public IdList getResourceIdList()
	{
		return resourceIds.getIdList();
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			ObjectData data = getField(tag);
			json.put(tag, data.get());
		}
		
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
	public static final String TAG_LOCATION = "Location";
	public static final String TAG_FUNDING_SOURCE = "FundingSource";

	StringData shortLabel;
	StringData method;
	IdListData resourceIds;
	StringData location;
	
	private HashMap fields;
}
