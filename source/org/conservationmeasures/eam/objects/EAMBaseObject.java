/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.util.HashMap;
import java.util.Iterator;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.ObjectData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class EAMBaseObject implements EAMObject
{
	public EAMBaseObject(BaseId idToUse)
	{
		setId(idToUse);
		clear();
	}
	
	EAMBaseObject(BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		setId(idToUse);
		clear();
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			setData(tag, json.optString(tag));
		}
	}
	
	public static EAMObject createFromJson(int type, EnhancedJsonObject json) throws Exception
	{
		int idAsInt = json.getInt(TAG_ID);
		switch(type)
		{
			case ObjectType.RATING_CRITERION:
				return new RatingCriterion(idAsInt, json);
				
			case ObjectType.VALUE_OPTION:
				return new ValueOption(idAsInt, json);
				
			case ObjectType.TASK:
				return new Task(idAsInt, json);
			
			case ObjectType.MODEL_NODE:
				return ConceptualModelNode.createFrom(idAsInt, json);

			case ObjectType.VIEW_DATA:
				return new ViewData(idAsInt, json);
				
			case ObjectType.MODEL_LINKAGE:
				return new ConceptualModelLinkage(idAsInt, json);
				
			case ObjectType.PROJECT_RESOURCE:
				return new ProjectResource(idAsInt, json);
				
			case ObjectType.INDICATOR:
				return new Indicator(idAsInt, json);
				
			case ObjectType.OBJECTIVE:
				return new Objective(idAsInt, json);
				
			case ObjectType.GOAL:
				return new Goal(idAsInt, json);
				
			case ObjectType.PROJECT_METADATA:
				return new ProjectMetadata(idAsInt, json);
				
			default:
				throw new RuntimeException("Attempted to create unknown EAMObject type " + type);
		}
	}
	
	abstract public int getType();

	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof EAMObject))
			return false;
		
		EAMObject other = (EAMObject)rawOther;
		return other.getId().equals(getId());
	}
	
	public String getLabel()
	{
		return label.get();
	}
	
	public void setLabel(String newLabel)
	{
		label.set(newLabel);
	}
	
	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(TAG_ID.equals(fieldTag))
		{
			id = new BaseId(Integer.parseInt(dataValue));
			return;
		}
		
		if(!fields.containsKey(fieldTag))
			throw new RuntimeException("Attempted to set data for bad field: " + fieldTag);

		getField(fieldTag).set(dataValue);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_ID.equals(fieldTag))
			return id.toString();
		
		if(!fields.containsKey(fieldTag))
			throw new RuntimeException("Attempted to get data for bad field: " + fieldTag);

		return getField(fieldTag).get();
	}
	

	public BaseId getId()
	{
		return id;
	}
	
	private void setId(BaseId newId)
	{
		id = newId;
	}
	
	void clear()
	{
		label = new StringData();
		
		fields = new HashMap();
		fields.put(TAG_LABEL, label);

	}

	private ObjectData getField(String fieldTag)
	{
		ObjectData data = (ObjectData)fields.get(fieldTag);
		return data;
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_ID, id.asInt());
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			ObjectData data = getField(tag);
			json.put(tag, data.get());
		}
		
		return json;
	}
	
	protected static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	
	public static final String DEFAULT_LABEL = "";

	BaseId id;
	StringData label;

	private HashMap fields;
}
