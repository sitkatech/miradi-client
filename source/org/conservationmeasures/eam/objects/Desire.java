/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class Desire extends EAMBaseObject
{
	public Desire(BaseId idToUse)
	{
		super(idToUse);
		shortLabel = new StringData();
		fullText = new StringData();
	}

	public Desire(BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, json);
		shortLabel = new StringData(json.optString(TAG_SHORT_LABEL));
		fullText = new StringData(json.optString(TAG_FULL_TEXT));
	}

	abstract public int getType();

	public String getShortLabel()
	{
		return shortLabel.get();
	}

	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_SHORT_LABEL))
			return getShortLabel();
		if(fieldTag.equals(TAG_FULL_TEXT))
			return fullText.get();
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_SHORT_LABEL))
			shortLabel.set(dataValue);
		else if(fieldTag.equals(TAG_FULL_TEXT))
			fullText.set(dataValue);
		else
			super.setData(fieldTag, dataValue);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		json.put(TAG_SHORT_LABEL, getShortLabel());
		json.put(TAG_FULL_TEXT, fullText.get());
		
		return json;
	}
	
	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return shortLabel + "." + getLabel();
	}

	
	public final static String TAG_SHORT_LABEL = "ShortLabel";
	public final static String TAG_FULL_TEXT = "FullText";

	StringData shortLabel;
	StringData fullText;

}
