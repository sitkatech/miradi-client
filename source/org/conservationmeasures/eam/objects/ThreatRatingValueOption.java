/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

import org.json.JSONObject;

public class ThreatRatingValueOption extends EAMObject
{
	public ThreatRatingValueOption(int idToUse)
	{
		this(idToUse, "Unknown", 0, Color.BLACK);
	}
	
	public ThreatRatingValueOption(int idToUse, String labelToUse, int numericToUse, Color colorToUse)
	{
		super(idToUse);
		label = labelToUse;
		numeric = numericToUse;
		color = colorToUse;
	}
	
	public ThreatRatingValueOption(JSONObject json)
	{
		super(json);
		label = json.getString(TAG_LABEL);
		numeric = json.getInt(TAG_NUMERIC);
		color = new Color(json.getInt(TAG_COLOR));
	}
	
	public int getType()
	{
		return ObjectType.THREAT_RATING_VALUE_OPTION;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public int getNumericValue()
	{
		return numeric;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	public void setData(String fieldTag, String dataValue)
	{
		if(TAG_LABEL.equals(fieldTag))
			label = dataValue;
		else if(TAG_NUMERIC.equals(fieldTag))
			numeric = Integer.parseInt(dataValue);
		else if(TAG_COLOR.equals(fieldTag))
			color = new Color(Integer.parseInt(dataValue));
		else
			throw new RuntimeException("Attempted to set data for unknown field: " + fieldTag);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_LABEL.equals(fieldTag))
			return getLabel();
		else if(TAG_NUMERIC.equals(fieldTag))
			return Integer.toString(getNumericValue());
		else if(TAG_COLOR.equals(fieldTag))
			return Integer.toString(getColor().getRGB());

		throw new RuntimeException("Attempted to get data for unknown field: " + fieldTag);
	}
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof ThreatRatingValueOption))
			return false;
		
		ThreatRatingValueOption other = (ThreatRatingValueOption)rawOther;
		return (other.getId() == getId());
	}
	
	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_LABEL, label);
		json.put(TAG_NUMERIC, numeric);
		json.put(TAG_COLOR, color.getRGB());
		
		return json;
	}
	
	final public static String TAG_LABEL = "Label";
	final public static String TAG_NUMERIC = "Numeric";
	final public static String TAG_COLOR = "Color";
	
	String label;
	int numeric;
	Color color;
}
