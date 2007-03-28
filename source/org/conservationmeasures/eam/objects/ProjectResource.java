/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.CodeListData;
import org.conservationmeasures.eam.objectdata.NumberData;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ProjectResource extends BaseObject
{
	public ProjectResource(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}
	
	public ProjectResource(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new ProjectResourceId(idAsInt), json);
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
	
	public double getCostPerUnit()
	{
		if (costPerUnit.toString().length() == 0)
			return 0;
		return Double.parseDouble(costPerUnit.toString());
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
		costUnit = new ChoiceData();
		organization = new StringData();
		roleCodes = new CodeListData();
		comments = new StringData();
		
		addField(TAG_INITIALS, initials);
		addField(TAG_NAME, name);
		addField(TAG_ORGANIZATION, organization);
		addField(TAG_POSITION, position);
		addField(TAG_PHONE_NUMBER, phoneNumber);
		addField(TAG_EMAIL, email);
		addField(TAG_COST_UNIT, costUnit);
		addField(TAG_COST_PER_UNIT, costPerUnit);
		addField(TAG_ROLE_CODES, roleCodes);
		addField(TAG_COMMENTS, comments);
		
	}
	
	public static final String TAG_INITIALS = "Initials";
	public static final String TAG_NAME = "Name";
	public static final String TAG_POSITION = "Position";
	public static final String TAG_PHONE_NUMBER = "PhoneNumber";
	public static final String TAG_EMAIL = "Email";
	public static final String TAG_COST_PER_UNIT = "CostPerUnit";
	public static final String TAG_COST_UNIT = "CostUnit";
	public static final String TAG_ORGANIZATION = "Organization";
	public static final String TAG_ROLE_CODES = "RoleCodes";
	public static final String TAG_COMMENTS = "Comments";

	StringData initials;
	StringData name;
	StringData position;
	StringData phoneNumber;
	StringData email;
	NumberData costPerUnit;
	ChoiceData costUnit;
	StringData organization;
	CodeListData roleCodes;
	StringData comments;
}
