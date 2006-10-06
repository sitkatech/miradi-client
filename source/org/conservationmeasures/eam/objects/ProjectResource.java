/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;
import org.martus.util.xml.XmlUtilities;

public class ProjectResource extends EAMBaseObject
{
	public ProjectResource(BaseId idToUse)
	{
		super(idToUse);
		initials = "";
		name = "";
		position = "";
	}
	
	public ProjectResource(int idAsInt, JSONObject json)
	{
		super(new BaseId(idAsInt), json);
		initials = json.optString(TAG_INITIALS, "");
		name = json.optString(TAG_NAME, "");
		position = json.optString(TAG_POSITION);
	}

	public int getType()
	{
		return ObjectType.PROJECT_RESOURCE;
	}

	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_INITIALS))
			return initials;
		if(fieldTag.equals(TAG_NAME))
			return name;
		if(fieldTag.equals(TAG_POSITION))
			return position;
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, Object dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_INITIALS))
			initials = (String)dataValue;
		else if(fieldTag.equals(TAG_NAME))
			name = (String)dataValue;
		else if(fieldTag.equals(TAG_POSITION))
			position = (String)dataValue;
		else
			super.setData(fieldTag, dataValue);
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_INITIALS, initials);
		json.put(TAG_NAME, name);
		json.put(TAG_POSITION, position);
		return json;
	}
	
	public String toString()
	{
		if(initials.length() > 0)
			return initials;
		if(name.length() > 0)
			return name;
		if(position.length() > 0)
			return position;
		return EAM.text("Label|(Undefined Resource)");
	}
	
	public static String getResourcesAsHtml(ProjectResource[] resources)
	{
		StringBuffer result = new StringBuffer();
		result.append("<html>");
		for(int i = 0; i < resources.length; ++i)
		{
			if(i > 0)
				result.append(", ");
			result.append(XmlUtilities.getXmlEncoded(resources[i].toString()));
		}
		result.append("</html>");
		
		return result.toString();
	}

	public static final String TAG_INITIALS = "Initials";
	public static final String TAG_NAME = "Name";
	public static final String TAG_POSITION = "Position";

	String initials;
	String name;
	String position;
}
