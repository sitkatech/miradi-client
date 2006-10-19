/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.BaseIdData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class EAMBaseObject implements EAMObject
{
	public EAMBaseObject(BaseId idToUse)
	{
		clear();
		setId(idToUse);
	}
	
	EAMBaseObject(BaseId idToUse, EnhancedJsonObject json)
	{
		clear();
		setId(idToUse);
		label.set(json.optString(TAG_LABEL));
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
		if(TAG_LABEL.equals(fieldTag))
			label.set(dataValue);
		else
			throw new RuntimeException("Attempted to set data for bad field: " + fieldTag);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_LABEL.equals(fieldTag))
			return label.get();
		
		throw new RuntimeException("Attempted to get data for bad field: " + fieldTag);
	}
	

	public BaseId getId()
	{
		return id.getId();
	}
	
	private void setId(BaseId newId)
	{
		id.setId(newId);
	}
	
	private void clear()
	{
		id = new BaseIdData();
		label = new StringData();
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_ID, id.get());
		json.put(TAG_LABEL, label.get());
		
		return json;
	}
	
	protected static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	
	public static final String DEFAULT_LABEL = "";

	BaseIdData id;
	StringData label;


}
