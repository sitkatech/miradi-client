/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

import org.json.JSONObject;

public class ThreatRatingValueOption
{
	public ThreatRatingValueOption(int idToUse)
	{
		this(idToUse, "Unknown", 0, Color.BLACK);
	}
	
	public ThreatRatingValueOption(int idToUse, String labelToUse, int numericToUse, Color colorToUse)
	{
		id = idToUse;
		label = labelToUse;
		numeric = numericToUse;
		color = colorToUse;
	}
	
	public ThreatRatingValueOption(JSONObject json)
	{
		id = json.getInt(TAG_ID);
		label = json.getString(TAG_LABEL);
		color = new Color(json.getInt(TAG_COLOR));
	}
	
	public int getId()
	{
		return id;
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
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof ThreatRatingValueOption))
			return false;
		
		ThreatRatingValueOption other = (ThreatRatingValueOption)rawOther;
		return (other.getId() == getId());
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put(TAG_ID, id);
		json.put(TAG_LABEL, label);
		json.put(TAG_COLOR, color.getRGB());
		
		return json;
	}
	
	private final static String TAG_ID = "Id";
	private final static String TAG_LABEL = "Label";
	private final static String TAG_COLOR = "Color";
	
	int id;
	String label;
	int numeric;
	Color color;
}
