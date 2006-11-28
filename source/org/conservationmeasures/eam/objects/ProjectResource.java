/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.NumberData;
import org.conservationmeasures.eam.objectdata.RatingData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ProjectResource extends EAMBaseObject
{
	public ProjectResource(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}
	
	public ProjectResource(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
	}

	public int getType()
	{
		return ObjectType.PROJECT_RESOURCE;
	}

	public String toString()
	{
		String result = initials.get();
		if(result.length() > 0)
			return result;
		
		result = name.get();
		if(result.length() > 0)
			return result;
		
		result = position.get();
		if(result.length() > 0)
			return result;
		
		return EAM.text("Label|(Undefined Resource)");
	}
	
	public void clear()
	{
		super.clear();
		
		initials = new StringData();
		name = new StringData();
		position = new StringData();
		phoneNumber = new StringData();
		email = new StringData();
		costPerUnit = new NumberData();
		costUnit = new RatingData();
		organization = new StringData();
		
		addField(TAG_INITIALS, initials);
		addField(TAG_NAME, name);
		addField(TAG_ORGANIZATION, organization);
		addField(TAG_POSITION, position);
		addField(TAG_PHONE_NUMBER, phoneNumber);
		addField(TAG_EMAIL, email);
		addField(TAG_COST_UNIT, costUnit);
		addField(TAG_COST_PER_UNIT, costPerUnit);
		
	}
	
	public static final String TAG_INITIALS = "Initials";
	public static final String TAG_NAME = "Name";
	public static final String TAG_POSITION = "Position";
	public static final String TAG_PHONE_NUMBER = "PhoneNumber";
	public static final String TAG_EMAIL = "Email";
	public static final String TAG_COST_PER_UNIT = "CostPerUnit";
	public static final String TAG_COST_UNIT = "CostUnit";
	public static final String TAG_ORGANIZATION = "Organization";

	StringData initials;
	StringData name;
	StringData position;
	StringData phoneNumber;
	StringData email;
	NumberData costPerUnit;
	RatingData costUnit;
	StringData organization;
}
