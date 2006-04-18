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
		setData(TAG_LABEL, labelToUse);
		numeric = numericToUse;
		color = colorToUse;
	}
	
	public ThreatRatingValueOption(JSONObject json)
	{
		super(json);
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
		if(TAG_NUMERIC.equals(fieldTag))
			numeric = Integer.parseInt(dataValue);
		else if(TAG_COLOR.equals(fieldTag))
			color = new Color(Integer.parseInt(dataValue));
		else
			super.setData(fieldTag, dataValue);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_NUMERIC.equals(fieldTag))
			return Integer.toString(getNumericValue());
		else if(TAG_COLOR.equals(fieldTag))
			return Integer.toString(getColor().getRGB());

		return super.getData(fieldTag);
	}
	
	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_NUMERIC, numeric);
		json.put(TAG_COLOR, color.getRGB());
		
		return json;
	}
	
	final public static String TAG_NUMERIC = "Numeric";
	final public static String TAG_COLOR = "Color";
	
	int numeric;
	Color color;
}
