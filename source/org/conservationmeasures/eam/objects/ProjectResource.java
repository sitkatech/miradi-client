/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.json.JSONObject;
import org.martus.util.xml.XmlUtilities;

public class ProjectResource extends EAMBaseObject
{
	public ProjectResource(BaseId idToUse)
	{
		super(idToUse);
		initials = new StringData();
		name = new StringData();
		position = new StringData();
	}
	
	public ProjectResource(int idAsInt, JSONObject json)
	{
		super(new BaseId(idAsInt), json);
		initials = new StringData(json.optString(TAG_INITIALS, ""));
		name = new StringData(json.optString(TAG_NAME, ""));
		position = new StringData(json.optString(TAG_POSITION));
	}

	public int getType()
	{
		return ObjectType.PROJECT_RESOURCE;
	}

	public String getData(String fieldTag)
	{
		if(fieldTag.equals(TAG_INITIALS))
			return initials.get();
		if(fieldTag.equals(TAG_NAME))
			return name.get();
		if(fieldTag.equals(TAG_POSITION))
			return position.get();
		
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(fieldTag.equals(TAG_INITIALS))
			initials.set(dataValue);
		else if(fieldTag.equals(TAG_NAME))
			name.set(dataValue);
		else if(fieldTag.equals(TAG_POSITION))
			position.set(dataValue);
		else
			super.setData(fieldTag, dataValue);
	}

	public JSONObject toJson()
	{
		JSONObject json = super.toJson();
		json.put(TAG_INITIALS, initials.get());
		json.put(TAG_NAME, name.get());
		json.put(TAG_POSITION, position.get());
		return json;
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

	StringData initials;
	StringData name;
	StringData position;
}
